package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.lib.Loop;
import frc.team3256.robot.Constants;

public class Superstructure extends SubsystemBase implements Loop{
    private static Superstructure instance;

    private Intake intake;
    private Elevator elevator;
    private Carriage carriage;

    private SystemState currentState;
    private SystemState newState;
    private WantedState wantedState;
    private boolean stateChanged;
    private double timeWhenStateChanged = 0;

    public static Superstructure getInstance() {
        return instance == null ? instance = new Superstructure() : instance;
    }

    private Superstructure() {
        intake = Intake.getInstance();
        elevator = Elevator.getInstance();
        carriage = Carriage.getInstance();
    }

    public enum SystemState {
        HOLDING_POSITION,
        INTAKING,
        EXHAUSTING,
        UNJAMMING,
        RAISING_ELEVATOR_MANUAL,
        LOWERING_ELEVATOR_MANUAL,
        SWITCH_POSITION,
        LOW_SCALE_POSITION,
        MID_SCALE_POSITION,
        HIGH_SCALE_POSITION,
        SCORING_FORWARD,
        SCORING_BACKWARD,
    }

    public enum WantedState {
        WANTS_TO_INTAKE,
        WANTS_TO_EXHAUST,
        WANTS_TO_UNJAM,
        WANTS_TO_SCORE_SWITCH,
        WANTS_TO_SCORE_LOW_SCALE,
        WANTS_TO_SCORE_MID_SCALE,
        WANTS_TO_SCORE_HIGH_SCALE,
        WANTS_TO_SCORE_FORWARD,
        WANTS_TO_SCORE_BACKWARD,
        WANTS_TO_SQUEEZE_HOLD,
        WANTS_TO_RAISE_MANUAL,
        WANTS_TO_LOWER_MANUAL,
    }

    @Override
    public void init(double timestamp){
        currentState = SystemState.HOLDING_POSITION;
        stateChanged = true;
    }

    @Override
    public void update(double timestamp) {
        switch (currentState) {
            case HOLDING_POSITION:
                newState = handleHoldingPosition();
                break;
            case INTAKING:
                newState = handleIntake();
                break;
            case EXHAUSTING:
                newState = handleExhaust();
                break;
            case UNJAMMING:
                newState = handleUnjam();
                break;
            case RAISING_ELEVATOR_MANUAL:
                newState = handleManualRaise();
                break;
            case LOWERING_ELEVATOR_MANUAL:
                newState = handleManualLower();
                break;
            case SWITCH_POSITION:
                newState = handleScoreSwitch(timestamp);
                break;
            case LOW_SCALE_POSITION:
                newState = handleScoreScaleLow(timestamp);
                break;
            case MID_SCALE_POSITION:
                newState = handleScoreScaleMid(timestamp);
                break;
            case HIGH_SCALE_POSITION:
                newState = handleScoreScaleHigh(timestamp);
                break;
            case SCORING_FORWARD:
                newState = handleScoreForward();
                break;
            case SCORING_BACKWARD:
                newState = handleScoreBackward();
                break;
        }
        if (newState != currentState){
            currentState = newState;
            timeWhenStateChanged = timestamp;
            stateChanged = true;
        }
        else stateChanged = false;
    }

    private SystemState handleHoldingPosition() {
        if (stateChanged) {
            //default squeeze carriage always
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        elevator.setWantedState(Elevator.WantedState.HOLD);
        return defaultStateTransfer();
    }

    private SystemState handleIntake() {
        if (stateChanged) {
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
        }
<<<<<<< HEAD
        if (elevator.getHeight() < Constants.kIntakePreset + 1.0){ //---------- if at a safe height to intakeeee
=======
        if (elevator.getHeight() < Constants.kIntakePreset){ //---------- if at a safe height to intake
>>>>>>> c89afd96a3da24ef1916659bdc8a62e310dacf7a
            intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_RECEIVE);
        }
        return defaultStateTransfer();
    }

    private SystemState handleExhaust() {
        if (stateChanged) {
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
        }
        carriage.setWantedState(Carriage.WantedState.WANTS_TO_EXHAUST);
        intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
        return defaultStateTransfer();
    }

    private SystemState handleUnjam() {
        intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        return defaultStateTransfer();
    }

    private SystemState handleManualRaise() {
        if(stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        elevator.setWantedState(Elevator.WantedState.MANUAL_UP);
        return defaultStateTransfer();
    }

    private SystemState handleManualLower() {
        if(stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        elevator.setWantedState(Elevator.WantedState.MANUAL_DOWN);
        return defaultStateTransfer();
    }

    private SystemState handleScoreSwitch(double currTime) {
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        if (currTime - timeWhenStateChanged > Constants.kElevatorRaiseDelayTime){
            elevator.setWantedState(Elevator.WantedState.SWITCH_POS);
        }
        return defaultStateTransfer();
    }

    private SystemState handleScoreScaleLow(double currTime) {
        if (stateChanged) {
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        if(currTime - timeWhenStateChanged > Constants.kElevatorRaiseDelayTime){
            elevator.setWantedState(Elevator.WantedState.LOW_SCALE_POS);
        }
        return defaultStateTransfer();
    }

    private SystemState handleScoreScaleMid(double currTime){
        if(stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        if(currTime - timeWhenStateChanged > Constants.kElevatorRaiseDelayTime){
            elevator.setWantedState(Elevator.WantedState.MID_SCALE_POS);
        }
        return defaultStateTransfer();
    }

    private SystemState handleScoreScaleHigh(double currTime){
        if(stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        if(currTime - timeWhenStateChanged > Constants.kElevatorRaiseDelayTime){
            elevator.setWantedState(Elevator.WantedState.HIGH_SCALE_POS);
        }
        return defaultStateTransfer();
    }

    private SystemState handleScoreForward() {
        if(elevator.getHeight() > Constants.kElevatorScoreFrontMinHeight){
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SCORE_FORWARD);
        }
        else carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        return defaultStateTransfer();
    }

    private SystemState handleScoreBackward(){
        if(elevator.getHeight() > Constants.kElevatorScoreRearMinHeight){
            carriage.setWantedState(Carriage.WantedState.WANTS_TO_SCORE_BACKWARD);
        }
        else carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        return defaultStateTransfer();
    }

    private SystemState defaultStateTransfer(){
        switch(wantedState){
            case WANTS_TO_INTAKE:
                return SystemState.INTAKING;

            case WANTS_TO_EXHAUST:
                return SystemState.EXHAUSTING;

            case WANTS_TO_UNJAM:
                return SystemState.UNJAMMING;

            case WANTS_TO_SCORE_SWITCH:
                return SystemState.SWITCH_POSITION;

            case WANTS_TO_SCORE_LOW_SCALE:
                return SystemState.LOW_SCALE_POSITION;

            case WANTS_TO_SCORE_MID_SCALE:
                return SystemState.MID_SCALE_POSITION;

            case WANTS_TO_SCORE_HIGH_SCALE:
                return SystemState.HIGH_SCALE_POSITION;

            case WANTS_TO_SCORE_FORWARD:
                return SystemState.SCORING_FORWARD;

            case WANTS_TO_SCORE_BACKWARD:
                return SystemState.SCORING_BACKWARD;

            case WANTS_TO_SQUEEZE_HOLD:
                return SystemState.HOLDING_POSITION;

            case WANTS_TO_RAISE_MANUAL:
                return SystemState.RAISING_ELEVATOR_MANUAL;

            case WANTS_TO_LOWER_MANUAL:
                return SystemState.LOWERING_ELEVATOR_MANUAL;

            default:
                return SystemState.HOLDING_POSITION;
        }
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    @Override
    public void end(double timestamp) {

    }

    @Override
    public void outputToDashboard() {
        SmartDashboard.putString("Current State: ", currentState.toString());
        SmartDashboard.putString("New State: ", newState.toString());
        SmartDashboard.putString("Wanted State: ", wantedState.toString());
        SmartDashboard.putBoolean("State Changed? ", stateChanged);
    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }

}
