package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.LED;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.SharpIR;
import frc.team3256.robot.Constants;

public class Intake extends SubsystemBase implements Loop {

    private LED led = LED.getInstance();

    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid flopperActuator, pivotActuator;
    private SharpIR cubeDetector;

    private SystemState currentState = SystemState.STOWED_CLOSED;
    private SystemState previousState = currentState;
    private WantedState wantedState;
    private WantedState prevWantedState;
    private boolean stateChanged;
    private boolean wantedStateChanged = true;
    private double unjamTimeStart;
    private SystemState unjamPreviousState;
    private boolean wantsToToggle = false;

    private boolean firstRun = true;

    private double kLeftIntakePower = Constants.kLeftIntakePower;
    private double kRightIntakePower = Constants.kRightIntakePower;
    private double kIntakeExhaustPower = Constants.kIntakeExhaustPower;
    private double kUnjamMaxDuration = Constants.kUnjamMaxDuration;

    private static Intake instance;

    public enum SystemState {
        INTAKING, //running the intake
        EXHAUSTING, //running the intake backwards
        UNJAMMING, //momentarily reversing the intake to unjam the power cube
        DEPLOYED_CLOSED, //intake is closed and idle
        DEPLOYED_OPEN, //intake is open and idle
        STOWED_CLOSED, //intake is stowed, idle, and closed
        STOWED_OPEN //intake is stowed, idle, and open
    }

    public enum WantedState{
        //Operator -> Intake button
        WANTS_TO_INTAKE,
        //Operator -> Exhaust button
        WANTS_TO_EXHAUST,
        //Operator -> Unjam button
        WANTS_TO_UNJAM,
        //Operator -> Whenever no buttons are pressed
        IDLE,
        //Operator -> Toggle pivot button
        WANTS_TO_TOGGLE_PIVOT,
        //Operator -> Toggle flop button
        WANTS_TO_TOGGLE_FLOP,
        WANTS_TO_OPEN_FLOP,
        //Force deploy for autonomous
        WANTS_TO_DEPLOY,
        //Force stow for autonomous
        WANTS_TO_STOW
    }

    private Intake() {
        leftIntake = new VictorSP(Constants.kLeftIntakePort);
        rightIntake = new VictorSP(Constants.kRightIntakePort);

        flopperActuator = new DoubleSolenoid(Constants.kIntakeFlopForward, Constants.kIntakeFlopReverse);
        pivotActuator = new DoubleSolenoid(Constants.kIntakePivotForward, Constants.kIntakePivotReverse);

        cubeDetector = new SharpIR(Constants.kIntakeSharpIR);

        leftIntake.setInverted(true);
        rightIntake.setInverted(false);
    }

    public static Intake getInstance(){
        return instance == null ? instance = new Intake(): instance;
    }

    @Override
    public void init(double timestamp) {
        prevWantedState = WantedState.IDLE;
        wantedState = WantedState.IDLE;
        stateChanged = true;
    }

    @Override
    public void update(double timestamp) {
        if (hasCube()){
            led.green();
        }
        else if (wantedState == WantedState.WANTS_TO_INTAKE){
            led.red();
        }
        else {
            led.turnOff();
    }
        if (firstRun) {
            wantedStateChanged = true;
            firstRun = false;
        }
        else if (prevWantedState != wantedState){
            wantedStateChanged = true;
            prevWantedState = wantedState;
        }
        else wantedStateChanged = false;

        if (!wantedStateChanged && wantsToToggle) return;
            SystemState newState;
            switch(currentState) {
                case INTAKING:
                newState = handleIntake();
                break;
            case EXHAUSTING:
                newState = handleExhaust();
                break;
            case UNJAMMING:
                newState = handleUnjam(timestamp);
                break;
            case DEPLOYED_OPEN:
                newState = handleDeployedOpen();
                break;
            case DEPLOYED_CLOSED:
                newState = handleDeployedClosed();
                break;
            case STOWED_OPEN:
                newState = handleStowedOpen();
                break;
            case STOWED_CLOSED: default:
                newState = handleStowedClosed();
                break;
        }
        //State transfer
        if (newState != currentState){
            System.out.println("\tPREV_STATE:" + previousState + "\tCURR_STATE:" + currentState +
                    "\tNEW_STATE:" + newState);
            previousState = currentState;
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;
     }

    @Override
    public void end(double timestamp) {

    }

    private SystemState handleIntake(){
        if (stateChanged){
            deployIntake();
            closeFlopper();
        }

        else{
            setIntake(kLeftIntakePower, kRightIntakePower);
        }
        return defaultStateTransfer();
    }

    private SystemState handleExhaust(){
        if (stateChanged){
            deployIntake();
            closeFlopper();
        }
        setIntake(kIntakeExhaustPower, kIntakeExhaustPower);
        return defaultStateTransfer();
    }

    private SystemState handleUnjam(double timestamp){
        if (stateChanged){
            unjamTimeStart = timestamp;
            unjamPreviousState = previousState;
        }
        //If we have been unjamming for over the max unjam duration needed
        //Switch the system state to the state that we were at before unjamming
        if (timestamp - unjamTimeStart > kUnjamMaxDuration){
            return unjamPreviousState;
        }
        //Otherwise, we can still unjam, so return UNJAMMING state
        else {
            setIntake(Constants.kIntakeUnjamPower,Constants.kIntakeUnjamPower);
            return SystemState.UNJAMMING;
        }
    }

    private SystemState handleDeployedClosed(){
        if (stateChanged){
            deployIntake();
            closeFlopper();
        }
        setIntake(0,0);
        return defaultStateTransfer();
    }

    private SystemState handleDeployedOpen(){
        if (stateChanged){
            deployIntake();
            openFlopper();
        }
        setIntake(0,0);
        return defaultStateTransfer();
    }

    private SystemState handleStowedClosed(){
        if (stateChanged){
            stowIntake();
            closeFlopper();
        }
        setIntake(0,0);
        return defaultStateTransfer();
    }

    private SystemState handleStowedOpen(){
        if (stateChanged){
            stowIntake();
            openFlopper();
        }
        setIntake(0,0);
        return defaultStateTransfer();
    }

    private void closeFlopper(){
        flopperActuator.set(DoubleSolenoid.Value.kReverse);
    }

    public void openFlopper(){
        flopperActuator.set(DoubleSolenoid.Value.kForward);
    }

    public void deployIntake(){
       pivotActuator.set(DoubleSolenoid.Value.kReverse);
    }

    private void stowIntake(){
       pivotActuator.set(DoubleSolenoid.Value.kForward);
    }

    public void setIntake(double left, double right){
       leftIntake.set(left);
       rightIntake.set(right);
    }

    public boolean hasCube(){
        return cubeDetector.isTriggered();
    }

    public double getVoltage(){
        return cubeDetector.getAvgVoltage();
    }

    //default WantedState -> SystemState
    private SystemState defaultStateTransfer(){
        switch (wantedState){
            case WANTS_TO_INTAKE:
                wantsToToggle = false;
                return SystemState.INTAKING;

            case WANTS_TO_EXHAUST:
                wantsToToggle = false;
                return SystemState.EXHAUSTING;

            case WANTS_TO_UNJAM:
                wantsToToggle = false;
                return SystemState.UNJAMMING;

            case WANTS_TO_OPEN_FLOP:
                if (currentState == SystemState.STOWED_OPEN || currentState == SystemState.STOWED_CLOSED){
                    return SystemState.DEPLOYED_OPEN;
                }
                else{
                    return SystemState.DEPLOYED_OPEN;
                }
            case IDLE:
                wantsToToggle = false;

                //if we are intaking, exhausting, unjamming, or already deployed and closed, the intake is closed,
                //so we want the next state to be DEPLOYED_CLOSED
                if (currentState == SystemState.EXHAUSTING || currentState == SystemState.INTAKING ||
                        currentState == SystemState.UNJAMMING || currentState == SystemState.DEPLOYED_CLOSED
                        || currentState == SystemState.DEPLOYED_OPEN) {
                    if (hasCube()) return SystemState.DEPLOYED_CLOSED;
                    return SystemState.DEPLOYED_OPEN;
                }
                else if (currentState == SystemState.STOWED_CLOSED || currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_CLOSED;
                }

                //Toggles stowed & deployed
            case WANTS_TO_TOGGLE_PIVOT:
                wantsToToggle = true;
                if(currentState == SystemState.STOWED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if(currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.STOWED_OPEN;
                }
                else if(currentState == SystemState.STOWED_CLOSED){
                    return SystemState.DEPLOYED_CLOSED;
                }
                else if(currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.INTAKING ||
                        currentState == SystemState.EXHAUSTING || currentState == SystemState.UNJAMMING){
                    return SystemState.STOWED_CLOSED;
                }

                //Toggles opened and closed
            case WANTS_TO_TOGGLE_FLOP:
                wantsToToggle = true;
                if(currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_CLOSED;
                }
                else if(currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.INTAKING ||
                        currentState == SystemState.EXHAUSTING || currentState == SystemState.UNJAMMING){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if(currentState == SystemState.STOWED_CLOSED){
                    return SystemState.STOWED_OPEN;
                }
                else if(currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_CLOSED;
                }
            case WANTS_TO_DEPLOY:
                wantsToToggle = false;
                if(currentState == SystemState.STOWED_OPEN || currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if(currentState == SystemState.STOWED_CLOSED || currentState == SystemState.DEPLOYED_CLOSED){
                    return SystemState.DEPLOYED_CLOSED;
                }
            case WANTS_TO_STOW:
                wantsToToggle = false;
                if(currentState == SystemState.DEPLOYED_OPEN || currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_OPEN;
                }
                else if(currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.STOWED_CLOSED){
                    return SystemState.STOWED_CLOSED;
                }
            //default: Safest position (Intake is stowed inside the robot)
            default:
                return SystemState.STOWED_CLOSED;
        }
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    public SystemState getCurrentState(){
        return currentState;
    }

    public WantedState getWantedState(){
        return wantedState;
    }

    @Override
    public void outputToDashboard() {
        /*
        SmartDashboard.putString("Current State: ", currentState.toString());
        SmartDashboard.putString("Wanted State: ", wantedState.toString());
        SmartDashboard.putString("Previous State: ", previousState.toString());
        SmartDashboard.putBoolean("State Changed? ", stateChanged);
        SmartDashboard.putNumber("Left Motor Power: ", leftIntake.getSpeed());
        SmartDashboard.putNumber("Right Motor Power: ", rightIntake.getSpeed());
        SmartDashboard.putString("Flopper State: ", flopperActuator.get().toString());
        SmartDashboard.putString("Pivot State: ", pivotActuator.get().toString());
        SmartDashboard.putBoolean("Cube detected? ", cubeDetector.isTriggered());
        */
    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }
}