package frc.team3256.robot.subsystems;

import frc.team3256.lib.Loop;
import frc.team3256.robot.Constants;

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
        }
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

    private SystemState handleIntaking(){
        if (!(elevator.atClosedLoopTarget() || carriage.atClosedLoopTarget())){
            boolean useClosedLoop = false;
            switch(wantedState){
                case INTAKE_HIGH:
                    //TODO: add and assign elevator intake high pos
                    carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                    useClosedLoop = true;
                    break;
                case INTAKE_MID:
                    //TODO: add and assign elevator intake mid pos
                    carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                    useClosedLoop = true;
                    break;
                case INTAKE_LOW:
                    elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_POS;
                    carriagePreset = Carriage.WantedState.INTAKE_PRESET;
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
        intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        return defaultStateTransfer();
    }

    private SystemState defaultStateTransfer(){
        boolean useClosedLoop = false;
        switch(wantedState){
            case INTAKE_HIGH:
                //TODO: add and assign elevator intake high pos
                carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                useClosedLoop = true;
                break;
            case INTAKE_MID:
                //TODO: add and assign elevator intake mid pos
                carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                useClosedLoop = true;
                break;
            case INTAKE_LOW: case EXHAUST:
                elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_POS;
                carriagePreset = Carriage.WantedState.INTAKE_PRESET;
                useClosedLoop = true;
                break;
            case SWITCH_FRONT_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_SWITCH_POS;
                carriagePreset = Carriage.WantedState.STOW_PRESET;
                useClosedLoop = true;
                break;
            case SWITCH_BACK_PRESET:
                elevatorPreset = Elevator.WantedState.WANTS_TO_SWITCH_POS;
                carriagePreset = Carriage.WantedState.FRONT_SCORE_PRESET;
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
                elevatorPreset = Elevator.WantedState.WANTS_TO_INTAKE_POS;
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
