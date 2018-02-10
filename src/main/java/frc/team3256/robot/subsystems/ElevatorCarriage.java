package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.Loop;
import frc.team3256.robot.Constants;

public class ElevatorCarriage extends SubsystemBase implements Loop{

    private VictorSP rollerLeft, rollerRight;
    private DoubleSolenoid squeezeSolenoid;

    private SystemState currentState;
    private WantedState wantedState;
    private boolean stateChanged;


    private static ElevatorCarriage elevatorCarriage;

    private ElevatorCarriage() {
        rollerLeft = new VictorSP(Constants.kCarriageRollerLeft);
        rollerRight = new VictorSP(Constants.kCarriageRollerRight);
        squeezeSolenoid = new DoubleSolenoid(Constants.kCarriageSqueezeForward, Constants.kCarriageSqueezeReverse);
    }

    public static ElevatorCarriage getInstance(){
        return elevatorCarriage == null ? elevatorCarriage = new ElevatorCarriage(): elevatorCarriage;
    }

    public enum SystemState {
        RECEIVING_FROM_INTAKE, //Self-explanatory
        SCORING_FORWARD, //Run rollers forward
        SCORING_BACKWARD, //Run rollers backward
        SQUEEZING_IDLE, //Actuators squeeze cube in place
        OPEN_IDLE //Actuators stay open
    }

    public enum WantedState {
        //Operator -> Whenever no buttons are pressed
        WANTS_TO_RECEIVE,
        //Operator -> Score forward button
        WANTS_TO_SCORE_FORWARD,
        //Operator -> Score backward button
        WANTS_TO_SCORE_BACKWARD,
        //Operator -> Whenever robot has cube
        WANTS_TO_SQUEEZE_IDLE,
        //Operator -> Manual open
        WANTS_TO_OPEN
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
            case SCORING_BACKWARD:
                newState = handleScoreBackward();
                break;
            case SQUEEZING_IDLE:
                newState = handleSqueezeIdle();
                break;
            case OPEN_IDLE: default:
                newState = handleOpenIdle();
                break;

        }
        //State transfer
        if (newState != currentState){
            System.out.println("\tCURR_STATE:" + currentState + "\tNEW_STATE:" + newState);
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;

    }

    private SystemState handleReceiveFromIntake(){
        if (stateChanged){
            squeeze();
        }
        //If we have a cube, then we squeeze
        if (hasCube()){
            runMotors(0);
            return SystemState.SQUEEZING_IDLE;
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

    private SystemState handleScoreBackward(){
        if (stateChanged){
            squeeze();
        }
        runMotors(Constants.kCarriageScoreBackwardPower);
        return defaultStateTransfer();
    }

    private SystemState handleSqueezeIdle(){
        if (stateChanged){
            squeeze();
        }
        return defaultStateTransfer();
    }

    private SystemState handleOpenIdle(){
        if (stateChanged){
            open();
        }
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

            case WANTS_TO_SQUEEZE_IDLE:
                return SystemState.SQUEEZING_IDLE;

            case WANTS_TO_OPEN:
                return SystemState.OPEN_IDLE;

            default:
                return SystemState.OPEN_IDLE;

        }
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    public void squeeze(){
        squeezeSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    public void open(){
        squeezeSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    public void runMotors(double power){
        rollerLeft.set(power);
        rollerRight.set(power);
    }

    public boolean hasCube(){
        return false;//Instance().hasCube();
    }

    @Override
    public void end(double timestamp) {

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