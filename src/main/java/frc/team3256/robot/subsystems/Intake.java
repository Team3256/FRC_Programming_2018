package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.SharpIR;
import frc.team3256.robot.Constants;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Intake extends SubsystemBase implements Loop{

    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid flopperActuator, pivotActuator;
    private SharpIR ballDetector;

    private SystemState currentState;
    private SystemState previousState;
    private WantedState wantedState;
    private boolean stateChanged;
    private double unjamTimeStart;
    private SystemState unjamPreviousState;

    private double kLeftIntakePower = Constants.kLeftIntakePower;
    private double kRightIntakePower = Constants.kRightIntakePower;
    private double kIntakeExhaustPower = Constants.kIntakeExhaustPower;
    private double kUnjamMaxDuration = Constants.kUnjamMaxDuration;

    private static Intake instance;

    public enum SystemState {
        INTAKING, //running the intake
        EXHAUSTING, //running the intake backwards
        UNJAMMING, //momentarily stopping the intake to unjam the power cube
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
        //Operator -> Whenever intake/exhaust/unjam buttons are released
        IDLE,
        //Operator -> Deploy button
        WANTS_TO_DEPLOY,
        //Operator -> Stow button
        WANTS_TO_STOW,
        //Operator -> Open button
        WANTS_TO_OPEN
    }


    private Intake(){
        leftIntake = new VictorSP(Constants.kLeftIntakePort); //port to be determined
        rightIntake = new VictorSP(Constants.kRightIntakePort);
        flopperActuator = new DoubleSolenoid(Constants.kIntakeFlopForward, Constants.kIntakeFlopReverse);
        pivotActuator = new DoubleSolenoid(Constants.kIntakePivotForward, Constants.kIntakePivotReverse);
        ballDetector = new SharpIR(Constants.kIntakeSharpIR, Constants.kIntakeSharpIRMinVoltage, Constants.kIntakeSharpIRMaxVoltage);
    }

    public static Intake getInstance(){
        return instance == null ? instance = new Intake(): instance;
    }

    @Override
    public void init(double timestamp) {
        currentState = SystemState.STOWED_CLOSED;
        previousState = SystemState.STOWED_CLOSED;
        stateChanged = true;
    }

     @Override
    public void update(double timestamp) {
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
            System.out.println("\tPREV_STATE:" + previousState + "\tCURR_STATE:" + currentState + "\tNEW_STATE:" + newState);
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
        //If we have a cube, then we stop intaking, and set the state to DEPLOYED_CLOSED
        if (hasCube()){
            setIntake(0,0);
            return SystemState.DEPLOYED_CLOSED;
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
        //Otherwise, we can still unjam, so stop the intake and check what the new wanted state is
        else {
            setIntake(0,0);
            return defaultStateTransfer();
        }
    }

    private SystemState handleDeployedClosed(){
        if (stateChanged){
            deployIntake();
            closeFlopper();
        }
        return defaultStateTransfer();
    }

    private SystemState handleDeployedOpen(){
        if (stateChanged){
            deployIntake();
            openFlopper();
        }
        return defaultStateTransfer();
    }

    private SystemState handleStowedClosed(){
        if (stateChanged){
            stowIntake();
            closeFlopper();
        }
        return defaultStateTransfer();
    }

    private SystemState handleStowedOpen(){
        if (stateChanged){
            stowIntake();
            openFlopper();
        }
        return defaultStateTransfer();
    }

    private void closeFlopper(){
        flopperActuator.set(DoubleSolenoid.Value.kReverse);
    }

    private void openFlopper(){
        flopperActuator.set(DoubleSolenoid.Value.kForward);
    }

    private void deployIntake(){
       pivotActuator.set(DoubleSolenoid.Value.kForward);
    }

    private void stowIntake(){
       pivotActuator.set(DoubleSolenoid.Value.kReverse);
    }

    private void setIntake(double left, double right){
       leftIntake.set(left);
       rightIntake.set(-right);
    }

    private boolean hasCube(){
        return ballDetector.isTriggered();
    }

    //default WantedState -> SystemState
    private SystemState defaultStateTransfer(){
        switch (wantedState){
            case WANTS_TO_INTAKE:
                return SystemState.INTAKING;

            case WANTS_TO_EXHAUST:
                return SystemState.EXHAUSTING;

            case WANTS_TO_UNJAM:
                return SystemState.UNJAMMING;

            case IDLE:
                //if we are intaking, exhausting, unjaming, or already deployed and closed, the intake is closed,
                //so we want the next state to be DEPLOYED_CLOSED
                if (currentState == SystemState.EXHAUSTING || currentState == SystemState.INTAKING ||
                        currentState == SystemState.UNJAMMING || currentState == SystemState.DEPLOYED_CLOSED){
                    return SystemState.DEPLOYED_CLOSED;
                }
                //Otherwise, return the respective state that we are in
                else if (currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if (currentState == SystemState.STOWED_CLOSED){
                    return SystemState.STOWED_CLOSED;
                }
                else if (currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_OPEN;
                }

            //if we want to deploy, check if we are currently open or closed, then set the respective state
            case WANTS_TO_DEPLOY:
                //if we are currently open, return DEPLOYED_OPEN
                if (currentState == SystemState.STOWED_OPEN || currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                //otherwise, we are currently closed, so return DEPLOYED_CLOSED
                else return SystemState.DEPLOYED_CLOSED;

            //if we want to stow, check if we are currently open or closed, then set the respective state
            case WANTS_TO_STOW:
                //if we are currently open, return STOWED_OPEN
                if (currentState == SystemState.DEPLOYED_OPEN || currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_OPEN;
                }
                //otherwise, we are currently closed, so return STOWED_CLOSED
                else return SystemState.STOWED_CLOSED;

            //if we want to open the intake, we need to check which state we are currently in
            case WANTS_TO_OPEN:
                //if we are currently stowed, then set the state to STOWED_OPEN (we want to open the intake while stowed)
                if (currentState == SystemState.STOWED_CLOSED || currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_OPEN;
                }
                //otherwise, we are currently deployed, so set the state to DEPLOYED_OPEN
                else return SystemState.DEPLOYED_OPEN;

            //default: Safest position (Intake is stowed inside the robot)
            default:
                return SystemState.STOWED_CLOSED;
        }
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    @Override
    public void outputToDashboard() {

    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }
}