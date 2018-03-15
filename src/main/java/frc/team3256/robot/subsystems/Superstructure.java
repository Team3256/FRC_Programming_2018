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
    private boolean stateChanged;

    public static Superstructure getInstance(){
        return instance == null ? instance = new Superstructure() : instance;
    }

    public enum SystemState{
        //Intaking cube off the ground
        INTAKING_LOW,
        //Intaking cube stacked on first level
        INTAKING_MID,
        //Intaking cube stacked on second level
        INTAKING_HIGH,
        //Spitting out cube
        EXHAUSTING,
        //Shooting the block out hard
        SCORING_FAST,
        //Shooting the block out not as hard
        SCORING_SLOW,
        //Just drop the block
        SCORING_OPEN,
        //Intake is idle, carriage is pivoting to target
        CLOSED_LOOP_PIVOTING,
        //Intake is idle, elevator is moving up to target
        CLOSED_LOOP_UP,
        //Intake is idle, elevator is moving down to target
        CLOSED_LOOP_DOWN,
        //Intake is idle, and elevator and pivot are holding position
        IDLE_AND_HOLD
    }

    public enum WantedState{
        INTAKE_LOW,
        INTAKE_MID,
        INTAKE_HIGH,
        EXHAUST,
        SCORE_FAST,
        SCORE_SLOW,
        SWITCH_FRONT,
        SWITCH_BACK,
        SCALE_LOW_FRONT,
        SCALE_MID_FRONT,
        SCALE_HIGH_FRONT,
        SCALE_LOW_BACK,
        SCALE_MID_BACK,
        SCALE_HIGH_BACK,
        STOW
    }

    @Override
    public void init(double timestamp) {
        currentState = SystemState.IDLE_AND_HOLD;
    }

    @Override
    public void update(double timestamp) {
        switch(currentState){
            case INTAKING_LOW:
                newState = handleIntakingLow();
                break;
            case INTAKING_MID:
                newState = handleIntakingMid();
                break;
            case INTAKING_HIGH:
                newState = handleIntakingHigh();
                break;
            case EXHAUSTING:
                newState = handleExhausting();
                break;
            case SCORING_FAST:
                newState = handleScoreFast();
                break;
            case SCORING_SLOW:
                newState = handleScoreSlow();
                break;
            case SCORING_OPEN:
                newState = handleScoreOpen();
                break;
            case CLOSED_LOOP_PIVOTING:
                newState = handleClosedLoopPivoting();
                break;
            case CLOSED_LOOP_UP:
                newState = handleClosedLoopUp();
                break;
            case CLOSED_LOOP_DOWN:
                newState = handleClosedLoopDown();
                break;
            case IDLE_AND_HOLD:
                newState = handleIdleAndHold();
                break;
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

    public SystemState handleIntakingLow(){
        if (!elevator.atClosedLoopTarget()){
            elevator.setWantedState(Elevator.WantedState.WANTS_TO_INTAKE_POS);
        }
        else if (!carriage.atClosedLoopTarget()){
            carriage.setWantedState(Carriage.WantedState.INTAKE_PRESET);
        }
        else{
            intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        }
        switch(wantedState){
            case INTAKE_LOW:
                return SystemState.INTAKING_LOW;
            case INTAKE_MID:
                return SystemState.INTAKING_MID;
            case INTAKE_HIGH:
                return SystemState.INTAKING_HIGH;
            case SWITCH_FRONT:
                if (elevator.getHeight() > )
        }
    }

    public SystemState handleIntakingMid(){

    }

    public SystemState handleIntakingHigh(){

    }

    public SystemState handleExhausting(){

    }

    public SystemState handleScoreFast(){

    }

    public SystemState handleScoreSlow(){

    }

    public SystemState handleScoreOpen(){

    }

    public SystemState handleClosedLoopPivoting(){

    }

    public SystemState handleClosedLoopUp(){

    }

    public SystemState handleClosedLoopDown(){

    }

    public SystemState handleIdleAndHold(){

    }

    public SystemState defaultStateTransfer(){
        switch(wantedState){
            case INTAKE_LOW:
                return SystemState.INTAKING_LOW;
            case INTAKE_MID:
                return SystemState.INTAKING_MID;
            case INTAKE_HIGH:
                return SystemState.INTAKING_HIGH;
            case EXHAUST:
                return SystemState.EXHAUSTING;
            case SCORE_FAST:
                return SystemState.SCORING_FAST;
            case SCORE_SLOW:
                return SystemState.SCORING_SLOW;
            case SWITCH_FRONT:
                return Sy
            case SWITCH_BACK:
                return SystemState.;
            case SCALE_LOW_FRONT:
                return SystemState.;
            case SCALE_LOW_BACK:
                return SystemState.;
            case SCALE_HIGH_FRONT:
                return SystemState.;
            case SCALE_MID_BACK:
                return SystemState.;
            case SCALE_MID_FRONT:
                return SystemState.;
            case SCALE_HIGH_BACK:
                return SystemState.;
            case STOW:
                return SystemState.;
            default:
                return SystemState.IDLE_AND_HOLD;
        }
    }

    public SystemState getCurrentState() { return currentState; }

    public WantedState getWantedState() { return wantedState; }

}
