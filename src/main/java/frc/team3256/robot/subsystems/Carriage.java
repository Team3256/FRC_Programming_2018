package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.Loop;
import frc.team3256.robot.Constants;

public class Carriage extends SubsystemBase implements Loop{

    private VictorSP rollerLeft, rollerRight;
    private DoubleSolenoid squeezeSolenoid;

    private SystemState currentState;
    private WantedState wantedState = WantedState.WANTS_TO_SQUEEZE_IDLE;
    private boolean stateChanged;

    private static Carriage instance;

    private Carriage() {
        rollerLeft = new VictorSP(Constants.kCarriageRollerLeft);
        rollerRight = new VictorSP(Constants.kCarriageRollerRight);

        squeezeSolenoid = new DoubleSolenoid(Constants.kCarriageSqueezeForward, Constants.kCarriageSqueezeReverse);

        rollerLeft.setInverted(false);
        rollerRight.setInverted(true);
    }

    public static Carriage getInstance(){
        return instance == null ? instance = new Carriage(): instance;
    }

    public enum SystemState {
        RECEIVING_FROM_INTAKE, //Self-explanatory
        SCORING_FORWARD, //Run rollers forward
        SCORING_BACKWARD, //Run rollers backward
        SCORING_FORWARD_SLOW,
        SCORING_BACKWARD_SLOW,
        SCORING_BACKWARD_AUTO,
        SMASHING_BACKWARD,
        SCORING_FORWARD_AUTO,
        SQUEEZING_IDLE, //Actuators squeeze cube in place
        OPEN_IDLE, //Actuators stay open
        EXHAUSTING
    }

    public enum WantedState {
        //Operator -> When we are intaking
        WANTS_TO_RECEIVE,
        //Operator -> Score forward button
        WANTS_TO_SCORE_FORWARD,
        WANTS_TO_SCORE_FORWARD_AUTO,
        //Operator -> Score backward button
        WANTS_TO_SCORE_BACKWARD,
        //Operator -> Whenever robot has cube
        WANTS_TO_SCORE_BACKWARD_AUTO,
        WANTS_TO_SMASH_BACKWARD,
        WANTS_TO_SCORE_FORWARD_SLOW,
        WANTS_TO_SCORE_BACKWARD_SLOW,
        WANTS_TO_SQUEEZE_IDLE,
        //When we are unjamming, open and idle
        WANTS_TO_OPEN_IDLE,
        WANTS_TO_EXHAUST
    }

    @Override
    public void init(double timestamp){
        currentState = SystemState.OPEN_IDLE;
        stateChanged = true;
    }

    @Override
    public void update(double timestamp) {
        SystemState newState;
        switch(currentState){
            case RECEIVING_FROM_INTAKE:
                newState = handleReceiveFromIntake();
                break;
            case SCORING_FORWARD:
                newState = handleScoreForward();
                break;
            case SCORING_FORWARD_AUTO:
                newState = handleScoreForwardAuto();
                break;
            case SCORING_BACKWARD:
                newState = handleScoreBackward();
                break;
            case SCORING_FORWARD_SLOW:
                newState = handleScoreForwardSlow();
                break;
            case SCORING_BACKWARD_SLOW:
                newState = handleScoreBackwardSlow();
                break;
            case SCORING_BACKWARD_AUTO:
                newState = handleScoreBackwardAuto();
                break;
            case SMASHING_BACKWARD:
                newState = handleSmashBackward();
                break;
            case SQUEEZING_IDLE:
                newState = handleSqueezeIdle();
                break;
            case EXHAUSTING:
                newState = handleExhaust();
                break;
            case OPEN_IDLE: default:
                newState = handleOpenIdle();
                break;

        }
        //State transfer
        if (newState != currentState){
            System.out.println("CURR_STATE:" + currentState + "\tNEW_STATE:" + newState);
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;

    }

    private SystemState handleReceiveFromIntake(){
        if (stateChanged){
            open();
        }
        runMotors(Constants.kCarriageReceivePower);
        return defaultStateTransfer();
    }

    private SystemState handleScoreForward(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreForwardPower);
        return defaultStateTransfer();
    }

    private SystemState handleScoreForwardSlow(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreForwardSlowPower);
        return defaultStateTransfer();
    }

    private SystemState handleScoreForwardAuto(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreForwardAutoPower);
        return defaultStateTransfer();
    }

    private SystemState handleScoreBackward(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreBackwardPower);
        return defaultStateTransfer();
    }

    private SystemState handleScoreBackwardSlow(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreBackwardSlowPower);
        return defaultStateTransfer();
    }

    private SystemState handleSmashBackward(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageSmashBackwardPower);
        return defaultStateTransfer();
    }

    private SystemState handleScoreBackwardAuto(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreBackwardAutoPower);
        return defaultStateTransfer();
    }

    private SystemState handleExhaust() {
        if (stateChanged) {
            squeeze();
        }
        runMotors(Constants.kCarriageExhaustPower);
        return defaultStateTransfer();
    }

    private SystemState handleSqueezeIdle(){
        if (stateChanged){
            squeeze();
        }
        runMotors(0);
        return defaultStateTransfer();
    }

    private SystemState handleOpenIdle(){
        if (stateChanged){
            open();
        }
        runMotors(0);
        return defaultStateTransfer();
    }

    //default WantedState -> SystemState
    private SystemState defaultStateTransfer(){
        switch(wantedState){
            case WANTS_TO_RECEIVE:
                return SystemState.RECEIVING_FROM_INTAKE;

            case WANTS_TO_SCORE_FORWARD:
                return SystemState.SCORING_FORWARD;

            case WANTS_TO_SCORE_BACKWARD:
                return SystemState.SCORING_BACKWARD;
            case WANTS_TO_SCORE_FORWARD_SLOW:
                return SystemState.SCORING_FORWARD_SLOW;
            case WANTS_TO_SCORE_BACKWARD_SLOW:
                return SystemState.SCORING_BACKWARD_SLOW;
            case WANTS_TO_SCORE_FORWARD_AUTO:
                return SystemState.SCORING_FORWARD_AUTO;
            case WANTS_TO_SCORE_BACKWARD_AUTO:
                return SystemState.SCORING_BACKWARD_AUTO;
            case WANTS_TO_SMASH_BACKWARD:
                return SystemState.SMASHING_BACKWARD;
            case WANTS_TO_SQUEEZE_IDLE:
                return SystemState.SQUEEZING_IDLE;
            case WANTS_TO_OPEN_IDLE:
                return SystemState.OPEN_IDLE;
            case WANTS_TO_EXHAUST:
                return SystemState.EXHAUSTING;

            default:
                return SystemState.OPEN_IDLE;
        }
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    private void squeeze(){
        squeezeSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    private void open(){
        squeezeSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    private void runMotors(double power){
        rollerLeft.set(power);
        rollerRight.set(power);
    }

    @Override
    public void end(double timestamp) {

    }

    @Override
    public void outputToDashboard() {
        /*
        SmartDashboard.putString("Current State: ", currentState.toString());
        SmartDashboard.putString("Wanted State: ", wantedState.toString());
        SmartDashboard.putBoolean("State Changed? ", stateChanged);
        SmartDashboard.putNumber("Left Motor Power: ", rollerLeft.getSpeed());
        SmartDashboard.putNumber("Right Motor Power: ", rollerRight.getSpeed());
        SmartDashboard.putString("Solenoid State: ", squeezeSolenoid.get().toString());
        */
    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }
}
