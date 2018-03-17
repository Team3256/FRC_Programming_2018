package frc.team3256.robot.subsystems;

import frc.team3256.lib.Loop;

public class Superstructure extends SubsystemBase implements Loop{
    private static Superstructure instance;

    private Intake intake;
    private Carriage carriage;
    private Elevator elevator;

    private SystemState currentState;
    private SystemState newState;
    private WantedState wantedState;
    private Elevator.WantedState elevatorPreset;
    private Carriage.WantedState carriagePreset;
    private boolean stateChanged;

    public static Superstructure getInstance(){
        return instance == null ? instance = new Superstructure() : instance;
    }

    private Superstructure(){
        intake = Intake.getInstance();
        carriage = Carriage.getInstance();
        elevator = Elevator.getInstance();
    }

    public enum SystemState{
        //Bringing in the cube
        INTAKING,
        //Spitting out cube
        EXHAUSTING,
        //Shooting the block out hard
        SCORING_FAST,
        //Shooting the block out not as hard
        SCORING_SLOW,
        //Just drop the block
        SCORING_OPEN,
        CLOSED_LOOP,
        MANUAL_CONTROL,
        HOMING,
        IDLE
    }

    public enum WantedState{
        WANTS_TO_HOME,
        INTAKE_LOW,
        INTAKE_MID,
        INTAKE_HIGH,
        EXHAUST,
        SCORE_FAST,
        SCORE_SLOW,
        SCORE_DROP,
        SWITCH_FRONT_PRESET,
        SWITCH_BACK_PRESET,
        SCALE_LOW_FRONT_PRESET,
        SCALE_HIGH_FRONT_PRESET,
        SCALE_LOW_BACK_PRESET,
        SCALE_HIGH_BACK_PRESET,
        STOW,
        MANUAL_UP,
        MANUAL_DOWN,
        MANUAL_IN,
        MANUAL_OUT,
    }

    @Override
    public void init(double timestamp) {
        currentState = SystemState.IDLE;
    }

    @Override
    public void update(double timestamp) {
        switch(currentState){
            case INTAKING:
                newState = handleIntaking();
                break;
            case EXHAUSTING:
                newState = handleExhaust();
                break;
            case SCORING_FAST:
                newState = handleScoreFast();
                break;
            case SCORING_SLOW:
                newState = handleScoreSlow();
                break;
            case SCORING_OPEN:
                newState = handleScoreDrop();
                break;
            case HOMING:
                newState = handleHoming();
                break;
            case MANUAL_CONTROL:
                newState = handleManualControl();
                break;
            case CLOSED_LOOP:
                newState = handleClosedLoop();
                break;
            case IDLE:
                newState = handleIdle();
                break;
        }
        if(newState != currentState){
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;
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

    private SystemState handleClosedLoop(){
        if (elevator.atClosedLoopTarget() && carriage.atClosedLoopTarget()){
            switch(wantedState){
                case INTAKE_HIGH: case INTAKE_LOW: case INTAKE_MID:
                    return SystemState.INTAKING;
                case EXHAUST:
                    return SystemState.EXHAUSTING;
                case SCORE_FAST:
                    return SystemState.SCORING_FAST;
                case SCORE_SLOW:
                    return SystemState.SCORING_SLOW;
                case SCORE_DROP:
                    return SystemState.SCORING_OPEN;
            }
        }
        elevator.setWantedState(elevatorPreset);
        carriage.setWantedState(carriagePreset);
        return defaultStateTransfer();
    }

    private SystemState handleIntaking(){
        if (!(elevator.atClosedLoopTarget() || carriage.atClosedLoopTarget())){
            return defaultStateTransfer();
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        return defaultStateTransfer();
    }

    private SystemState handleExhaust(){
        if(!(elevator.atClosedLoopTarget() || carriage.atClosedLoopTarget())){
            return defaultStateTransfer();
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
        return defaultStateTransfer();
    }

    private SystemState handleScoreFast(){
        if(!(elevator.atClosedLoopTarget() || carriage.atClosedLoopTarget())){
            return defaultStateTransfer();
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_SCORE);
        return defaultStateTransfer();
    }

    private SystemState handleScoreSlow(){
        if(!(elevator.atClosedLoopTarget() || carriage.atClosedLoopTarget())){
            return defaultStateTransfer();
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_SCORE_SLOW);
        return defaultStateTransfer();
    }

    private SystemState handleScoreDrop(){
        if(!(elevator.atClosedLoopTarget() || carriage.atClosedLoopTarget())){
            return defaultStateTransfer();
        }
        intake.setWantedState(Intake.WantedState.WANTS_TO_OPEN);
        return defaultStateTransfer();
    }

    private SystemState handleHoming(){
        elevator.setWantedState(Elevator.WantedState.WANTS_TO_HOME);
        carriage.setWantedState(Carriage.WantedState.WANTS_TO_HOME);
        return defaultStateTransfer();
    }

    private SystemState handleManualControl(){
        elevator.setWantedState(elevatorPreset);
        carriage.setWantedState(carriagePreset);
        return defaultStateTransfer();
    }

    private SystemState handleIdle(){
        intake.setWantedState(Intake.WantedState.IDLE);
        elevator.setWantedState(Elevator.WantedState.WANTS_TO_HOLD);
        carriage.setWantedState(Carriage.WantedState.WANTS_TO_HOLD);
        return defaultStateTransfer();
    }

    private SystemState defaultStateTransfer(){
        boolean useClosedLoop;
        switch(wantedState){
            case WANTS_TO_HOME:
                return SystemState.HOMING;
            case INTAKE_HIGH:
                elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_HIGH_POS;
                carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                useClosedLoop = true;
                break;
            case INTAKE_MID:
                elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_MID_POS;
                carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                useClosedLoop = true;
                break;
            case INTAKE_LOW: case EXHAUST:
                elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_LOW_POS;
                carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                useClosedLoop = true;
                break;
            case SWITCH_FRONT_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_SWITCH_POS;
                carriagePreset = Carriage.WantedState.FRONT_SCORE_PRESET;
                useClosedLoop = true;
                break;
            case SWITCH_BACK_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_SWITCH_POS;
                carriagePreset = Carriage.WantedState.STOW_PRESET;
                useClosedLoop = true;
                break;
            case SCALE_LOW_FRONT_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_LOW_SCALE_POS;
                carriagePreset = Carriage.WantedState.FRONT_SCORE_PRESET;
                useClosedLoop = true;
                break;
            case SCALE_HIGH_FRONT_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_HIGH_SCALE_POS;
                carriagePreset = Carriage.WantedState.FRONT_SCORE_PRESET;
                useClosedLoop = true;
                break;
            case SCALE_LOW_BACK_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_LOW_SCALE_POS;
                carriagePreset = Carriage.WantedState.BACK_SCORE_PRESET;
                useClosedLoop = true;
                break;
            case SCALE_HIGH_BACK_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_HIGH_SCALE_POS;
                carriagePreset = Carriage.WantedState.BACK_SCORE_PRESET;
                useClosedLoop = true;
                break;
            case STOW:
                elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_LOW_POS;
                carriagePreset = Carriage.WantedState.STOW_PRESET;
                useClosedLoop = true;
                break;
            case MANUAL_UP:
                elevatorPreset = Elevator.WantedState.WANTS_TO_MANUAL_UP;
                carriagePreset = Carriage.WantedState.WANTS_TO_HOLD;
                useClosedLoop = false;
                break;
            case MANUAL_DOWN:
                elevatorPreset = Elevator.WantedState.WANTS_TO_MANUAL_DOWN;
                carriagePreset = Carriage.WantedState.WANTS_TO_HOLD;
                useClosedLoop = false;
                break;
            case MANUAL_IN:
                carriagePreset = Carriage.WantedState.WANTS_TO_MANUAL_REVERSE;
                elevatorPreset = Elevator.WantedState.WANTS_TO_HOLD;
                useClosedLoop = false;
                break;
            case MANUAL_OUT:
                carriagePreset = Carriage.WantedState.WANTS_TO_MANUAL_FORWARD;
                elevatorPreset = Elevator.WantedState.WANTS_TO_HOLD;
                useClosedLoop = false;
                break;
            default:
                elevatorPreset = Elevator.WantedState.WANTS_TO_HOLD;
                carriagePreset = Carriage.WantedState.WANTS_TO_HOLD;
                useClosedLoop = false;
                break;
        }
        if (useClosedLoop){
            return SystemState.CLOSED_LOOP;
        }
        else{
            return SystemState.MANUAL_CONTROL;
        }
    }

    
    public SystemState getCurrentState() { return currentState; }

    public WantedState getWantedState() { return wantedState; }

}
