package frc.team3256.robot.subsystems;

public class Elevator {


    private SystemState currentState;
    private WantedState wantedState;
    
    
    private boolean isCalibrated = false;
    private boolean stateChanged;

    private double m_closedLoopTarget;
    private boolean m_usingClosedLoop;

    private static Elevator instance;
    public static Elevator getInstance() {
        return instance == null ? instance = new Elevator() : instance;
    }


    private Elevator() {

    }

    public void setOpenLoop(){
        tempflag = false;
    }

    public void holdPosition(){
        //TODO: Make this the absolute position
    }

    boolean tempflag = false;

    public void setTargetPosition(double targetHeight){
        if (!isCalibrated)return;
        System.out.println("OUTPUT VOLTAGE: stuff");
        if (!tempflag) {
            tempflag = true;
        }
    }

    public enum SystemState{
        FAST_UP,
        FAST_DOWN,
        HOLD,
        MANUAL_UP,
        MANUAL_DOWN,
    }

    public enum WantedState{
        HIGH_SCALE,
        LOW_SCALE,
        HOLD,
        SWITCH,
        MANUAL_UP,
        MANUAL_DOWN,
        INTAKE_POS,
    }


    public void init(double timestamp) {
        //setting current state to something, may or may not include in actual robot code
        //Is needed in order for error to not be thrown, auto defaults to HOLD for some reason
        //TODO: Figure out how to fix this ↓
        currentState = SystemState.FAST_DOWN;
        stateChanged = true;
    }

    
    public void update(double timestamp){
        SystemState newState = SystemState.HOLD;
        switch(currentState){
            case HOLD:
                newState = handleHold();
                break;
            case FAST_UP:
                newState = handleFastUp();
                break;
            case FAST_DOWN:
                newState = handleFastDown();
                break;
            case MANUAL_UP:
                newState = handleManualControlUp();
                break;
            case MANUAL_DOWN:
                newState = handleManualControlDown();
                break;
        }
        //State Transfer
        if(newState != currentState){
            currentState = newState;
            System.out.println("\tCURR_STATE:" + currentState + "\tNEW_STATE:" + newState);
            stateChanged = true;
        }
        else stateChanged = false;
    }

    
    public void end(double timestamp) {

    }

    public boolean atClosedLoopTarget(){
        if (!m_usingClosedLoop) return false;
        return (Math.abs(getHeight() - m_closedLoopTarget) < 100000);
    }

    private SystemState handleHold(){
        setTargetPosition(getHeight());
        return defaultStateTransfer();
    }

    private SystemState handleFastUp(){
        if(isCalibrated){
            if (atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            setTargetPosition(m_closedLoopTarget);
            return SystemState.FAST_UP;
        }
        return defaultStateTransfer();
    }

    private SystemState handleFastDown(){
        if(isCalibrated){
            if(atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            setTargetPosition(m_closedLoopTarget);
            return SystemState.FAST_DOWN;
        }
        return defaultStateTransfer();
    }

    private SystemState handleManualControlUp(){
        setOpenLoop();
        return defaultStateTransfer();
    }

    private SystemState handleManualControlDown() {
        setOpenLoop();
        return defaultStateTransfer();
    }

    private double sensorUnitsToHeight(double ticks) {
        return (ticks/4096.0)*100;
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    private SystemState defaultStateTransfer(){
        SystemState rv = SystemState.HOLD;
        switch (wantedState){
            case HIGH_SCALE:
                if(stateChanged) {
                    m_closedLoopTarget = 23874234;
                }
                m_usingClosedLoop = true;
                break;
            case LOW_SCALE:
                if(stateChanged) {
                    m_closedLoopTarget = 1;
                }
                m_usingClosedLoop = true;
                break;
            case HOLD:
                if(stateChanged) {
                    m_closedLoopTarget = getHeight();
                }
                m_usingClosedLoop = true;
                break;
            case SWITCH:
                if(stateChanged) {
                    m_closedLoopTarget = 1;
                }
                m_usingClosedLoop = true;
                break;
            case MANUAL_UP:
                m_usingClosedLoop = false;
                break;
            case MANUAL_DOWN:
                m_usingClosedLoop = false;
                break;
            case INTAKE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = 2;
                }
                m_usingClosedLoop = true;
                break;
            default:
                rv = SystemState.HOLD;
                break;
        }
        if(m_closedLoopTarget > getHeight() && m_usingClosedLoop) {
            rv = SystemState.FAST_UP;
        }
        else if (m_closedLoopTarget < getHeight() && m_usingClosedLoop){
            rv = SystemState.FAST_DOWN;
        }
        return rv;
    }

    public boolean isCalibrated(){
        return isCalibrated;
    }

    public boolean isTriggered(){
        return false;
    }

    public double getHeight() {
        return 30;
    }

    public SystemState getCurrentState() {
        return currentState;
    }
}
