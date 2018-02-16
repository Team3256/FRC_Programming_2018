package frc.team3256.robot.subsystems;

import frc.team3256.lib.Loop;

public class Superstructure extends SubsystemBase implements Loop{
    private static Superstructure instance;

    private Intake intake;
    private Elevator elevator;
    private ElevatorCarriage carriage;

    private SystemState currentState;
    private SystemState newState;
    private WantedState wantedState;
    private boolean stateChanged;

    private static Superstructure getInstance() {
        return instance == null ? instance = new Superstructure() : instance;
    }

    private Superstructure() {
        intake = Intake.getInstance();
        elevator = Elevator.getInstance();
        carriage = ElevatorCarriage.getInstance();
    }

    public enum SystemState {
        STOWED_OPEN,
        STOWED_CLOSED,
        DEPLOYED_OPEN,
        DEPLOYED_CLOSED,
        HOLDING_POSITION,
        INTAKING,
        EXHAUSTING,
        UNJAMMING,
        RAISING_ELEVATOR_MANUAL,
        LOWERING_ELEVATOR_MANUAL,
        SCORING_SWITCH,
        SCORING_LOW,
        SCORING_HIGH,
        SCORING_FORWARD,
        SCORING_BACKWARD
    }

    public enum WantedState {
        WANTS_TO_INTAKE,
        WANTS_TO_EXHAUST,
        WANTS_TO_UNJAM,
        WANTS_TO_TOGGLE_PIVOT,
        WANTS_TO_TOGGLE_FLOP,
        WANTS_TO_SCORE_SWITCH,
        WANTS_TO_SCORE_LOW_SCALE,
        WANTS_TO_SCORE_HIGH_SCALE,
        WANTS_TO_SCORE_FORWARD,
        WANTS_TO_SCORE_BACKWARD,
        WANTS_TO_SQUEEZE,
        WANTS_TO_RAISE,
        WANTS_TO_LOWER
    }

    @Override
    public void init(double timestamp){
        currentState = SystemState.STOWED_CLOSED;
        stateChanged = true;
    }

    @Override
    public void update(double timestamp) {
        switch (currentState) {
            case STOWED_OPEN:
                newState = handleStowedOpen();
                break;
            case STOWED_CLOSED: default:
                newState = handleStowedClosed();
                break;
            case DEPLOYED_OPEN:
                newState = handleDeployedOpen();
                break;
            case DEPLOYED_CLOSED:
                newState = handleDeployedClosed();
                break;
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
            case SCORING_SWITCH:
                newState = handleScoreSwitch();
                break;
            case SCORING_LOW:
                newState = handleScoreLow();
                break;
            case SCORING_HIGH:
                newState = handleScoreHigh();
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
            stateChanged = true;
        }
        else stateChanged = false;
    }

    private SystemState handleStowedOpen() {
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        return defaultStateTransfer();
    }

    private SystemState handleStowedClosed() {
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        return defaultStateTransfer();
    }

    private SystemState handleDeployedOpen() {
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        return defaultStateTransfer();
    }

    private SystemState handleDeployedClosed(){
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        return defaultStateTransfer();
    }

    private SystemState handleHoldingPosition() {
        if (stateChanged) {
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        elevator.setWantedState(Elevator.WantedState.HOLD);
        return defaultStateTransfer();
    }

    private SystemState handleIntake() {
        if (stateChanged) {
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_RECEIVE);
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        return defaultStateTransfer();
    }

    private SystemState handleExhaust() {
        if (stateChanged) {
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
        return defaultStateTransfer();
    }

    private SystemState handleUnjam() {
        intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        return defaultStateTransfer();
    }

    private SystemState handleManualRaise() {
        elevator.setWantedState(Elevator.WantedState.MANUAL_UP);
        return defaultStateTransfer();
    }

    private SystemState handleManualLower() {
            elevator.setWantedState(Elevator.WantedState.MANUAL_DOWN);
        return defaultStateTransfer();
    }

    private SystemState handleScoreSwitch() {
            elevator.setWantedState(Elevator.WantedState.SWITCH);
        return defaultStateTransfer();
    }

    private SystemState handleScoreLow() {
            elevator.setWantedState(Elevator.WantedState.LOW_SCALE);
        return defaultStateTransfer();
    }

    private SystemState handleScoreHigh(){
            elevator.setWantedState(Elevator.WantedState.HIGH_SCALE);
        return defaultStateTransfer();
    }

    private SystemState handleScoreForward() {
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_FORWARD);
        return defaultStateTransfer();
    }

    private SystemState handleScoreBackward(){
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_BACKWARD);
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

            case WANTS_TO_TOGGLE_PIVOT:
                if(currentState == SystemState.STOWED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if (currentState == SystemState.STOWED_CLOSED){
                    return SystemState.DEPLOYED_CLOSED;
                }
                else if (currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.STOWED_OPEN;
                }
                else if (currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.INTAKING ||
                        currentState == SystemState.EXHAUSTING || currentState == SystemState.UNJAMMING){
                    return SystemState.STOWED_CLOSED;
                }

            case WANTS_TO_TOGGLE_FLOP:
                if (currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_CLOSED;
                }
                else if (currentState == SystemState.STOWED_CLOSED){
                    return SystemState.STOWED_OPEN;
                }
                else if (currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_CLOSED;
                }
                else if (currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.INTAKING ||
                        currentState == SystemState.EXHAUSTING || currentState == SystemState.UNJAMMING){
                    return SystemState.DEPLOYED_OPEN;
                }

            case WANTS_TO_SCORE_SWITCH:
                return SystemState.SCORING_SWITCH;

            case WANTS_TO_SCORE_LOW_SCALE:
                return SystemState.SCORING_LOW;

            case WANTS_TO_SCORE_HIGH_SCALE:
                return SystemState.SCORING_HIGH;

            case WANTS_TO_SCORE_FORWARD:
                return SystemState.SCORING_FORWARD;

            case WANTS_TO_SCORE_BACKWARD:
                return SystemState.SCORING_BACKWARD;

            case WANTS_TO_SQUEEZE:
                return SystemState.HOLDING_POSITION;

            case WANTS_TO_RAISE:
                return SystemState.RAISING_ELEVATOR_MANUAL;

            case WANTS_TO_LOWER:
                return SystemState.LOWERING_ELEVATOR_MANUAL;

            default:
                return SystemState.STOWED_CLOSED;
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

    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }

}
