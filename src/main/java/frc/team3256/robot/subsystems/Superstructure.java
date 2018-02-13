package frc.team3256.robot.subsystems;

public class Superstructure {

    private static Superstructure instance;

    private Intake intake;
    private Elevator elevator;
    private ElevatorCarriage carriage;

    private SystemState currentState;
    private WantedState wantedState;
    private boolean stateChanged;

    private static Superstructure getInstance(){
        return instance == null ? instance = new Superstructure() : instance;
    }

    private Superstructure(){
        intake = Intake.getInstance();
        elevator = Elevator.getInstance();
        carriage = ElevatorCarriage.getInstance();
    }

    public enum SystemState{
        STOWED_OPEN,
        STOWED_CLOSED,
        DEPLOYED_OPEN,
        DEPLOYED_CLOSED,
        STOWED_AND_IDLE,
        HOLDING_POSITION,
        INTAKING,
        EXHAUSTING,
        UNJAMMING,
        RAISING_ELEVATOR,
        LOWERING_ELEVATOR,
        SCORING
    }

    public enum WantedState{
        WANTS_TO_STOW,
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


    public void update(){
        SystemState newState;
        switch(currentState){
            case STOWED_OPEN:
                break;
            case STOWED_CLOSED:
                break;
            case DEPLOYED_OPEN:
                break;
            case DEPLOYED_CLOSED:
                break;
            case STOWED_AND_IDLE:
                break;
            case HOLDING_POSITION:
                break;
            case INTAKING:
                break;
            case EXHAUSTING:
                break;
            case UNJAMMING:
                break;
            case RAISING_ELEVATOR:
                break;
            case LOWERING_ELEVATOR:
                break;
            case SCORING:
                break;
        }

    }

    private SystemState handleStowedOpen(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }
        return defaultStateTransfer();
    }

    private SystemState handleStowedClosed(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }
        return defaultStateTransfer();
    }

    private SystemState handleDeployedOpen(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }
        return defaultStateTransfer();
    }

    private SystemState handleStowedAndIdle(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.IDLE);
        }
        return defaultStateTransfer();
    }

    private SystemState handleHoldingPosition(){
        if (stateChanged){
            elevator.setWantedState(Elevator.WantedState.HOLD);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }
        return defaultStateTransfer();
    }

    private SystemState handleIntake(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_RECEIVE);
        }
        return defaultStateTransfer();
    }

    private SystemState handleExhaust(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
        }
        return defaultStateTransfer();
    }

    private SystemState handleUnjam(){
        if (stateChanged){
            intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        }
        return defaultStateTransfer();
    }

    private SystemState handleRaise(){
        if (stateChanged){
            elevator.setWantedState(Elevator.WantedState.MANUAL_UP);
        }
        return defaultStateTransfer();
    }



    private SystemState defaultStateTransfer(){
        switch(wantedState){
            case WANTS_TO_STOW:
                return SystemState.STOWED_AND_IDLE;

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
                return SystemState.SCORING;

            case WANTS_TO_SCORE_LOW_SCALE:
                return SystemState.SCORING;

            case WANTS_TO_SCORE_HIGH_SCALE:
                return SystemState.SCORING;

            case WANTS_TO_SCORE_FORWARD:
                return SystemState.SCORING;

            case WANTS_TO_SCORE_BACKWARD:
                return SystemState.SCORING;

            case WANTS_TO_SQUEEZE:
                return SystemState.HOLDING_POSITION;

            case WANTS_TO_RAISE:
                return SystemState.RAISING_ELEVATOR;

            case WANTS_TO_LOWER:
                return SystemState.LOWERING_ELEVATOR;

            default:
                return SystemState.STOWED_AND_IDLE;
        }

    }
    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

}
