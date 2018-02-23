package frc.team3256.robot.subsystems;

public class Intake {
    private SystemState currentState;
    private SystemState previousState;
    private WantedState wantedState;
    private WantedState prevWantedState;
    private boolean stateChanged;
    private boolean wantedStateChanged = true;
    private double unjamTimeStart;
    private SystemState unjamPreviousState;
    private boolean wantsToToggle = false;

    private boolean firstRun = true;

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
        //Operator -> Whenever no buttons are pressed
        IDLE,
        WANTS_TO_TOGGLE_PIVOT,
        WANTS_TO_TOGGLE_FLOP,
        WANTS_TO_DEPLOY
    }

    private Intake() {
    }

    public static Intake getInstance(){
        return instance == null ? instance = new Intake(): instance;
    }

    
    public void init(double timestamp) {
        currentState = SystemState.DEPLOYED_CLOSED;
        previousState = SystemState.DEPLOYED_CLOSED;
        prevWantedState = WantedState.IDLE;
        wantedState = WantedState.IDLE;
        stateChanged = true;
    }

    
    public void update(double timestamp) {

        System.out.println("\tPREV_WANTED_STATE:" + prevWantedState + "\tCURR_WANTED_STATE:" + wantedState + "\tWANTS_TO_TOGGLE:" + wantsToToggle);
        if (firstRun) {
            wantedStateChanged = true;
            firstRun = false;
        }
        else if (prevWantedState != wantedState){
            wantedStateChanged = true;
            prevWantedState = wantedState;
            System.out.println("prev != wanted");
        }
        else wantedStateChanged = false;

        if (!wantedStateChanged && wantsToToggle) return;
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

        System.out.println("\tPREV_STATE:" + previousState + "\tCURR_STATE:" + currentState +
                "\tNEW_STATE:" + newState);
        if (newState != currentState){
            previousState = currentState;
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;
    }

    private SystemState handleIntake(){
        if (stateChanged){
        }
        //If we have a cube, then we stop intaking, and set the state to DEPLOYED_CLOSED
        if (hasCube()){
            return SystemState.DEPLOYED_CLOSED;
        }
        return defaultStateTransfer();
    }

    private SystemState handleExhaust(){
        if (stateChanged){
        }
        return defaultStateTransfer();
    }

    private SystemState handleUnjam(double timestamp){
        if (stateChanged){
            unjamTimeStart = timestamp;
            unjamPreviousState = previousState;
        }
        //If we have been unjamming for over the max unjam duration needed
        //Switch the system state to the state that we were at before unjamming
        if (timestamp - unjamTimeStart > 0){
            return unjamPreviousState;
        }
        //Otherwise, we can still unjam, so stop the intake and check what the new wanted state is
        else {
            return defaultStateTransfer();
        }
    }

    private SystemState handleDeployedClosed(){
        if (stateChanged){
        }
        return defaultStateTransfer();
    }

    private SystemState handleDeployedOpen(){
        if (stateChanged){
        }
        return defaultStateTransfer();
    }

    private SystemState handleStowedClosed(){
        if (stateChanged){
        }
        return defaultStateTransfer();
    }

    private SystemState handleStowedOpen(){
        if (stateChanged){
        }
        return defaultStateTransfer();
    }

    public boolean hasCube(){
        return false;//cubeDetector.isTriggered();
    }

    //default WantedState -> SystemState
    private SystemState defaultStateTransfer(){
        switch (wantedState){
            case WANTS_TO_INTAKE:
                wantsToToggle = false;
                return SystemState.INTAKING;

            case WANTS_TO_EXHAUST:
                wantsToToggle = false;
                return SystemState.EXHAUSTING;

            case WANTS_TO_UNJAM:
                wantsToToggle = false;
                return SystemState.UNJAMMING;

            case IDLE:
                wantsToToggle = false;
                //if we are intaking, exhausting, unjamming, or already deployed and closed, the intake is closed,
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

                //Toggles stowed & deployed
            case WANTS_TO_TOGGLE_PIVOT:
                System.out.println("PIVOTING");
                wantsToToggle = true;
                if(currentState == SystemState.STOWED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if(currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.STOWED_OPEN;
                }
                else if(currentState == SystemState.STOWED_CLOSED){
                    return SystemState.DEPLOYED_CLOSED;
                }
                else if(currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.INTAKING ||
                        currentState == SystemState.EXHAUSTING || currentState == SystemState.UNJAMMING){
                    return SystemState.STOWED_CLOSED;
                }

                //Toggles opened and closed
            case WANTS_TO_TOGGLE_FLOP:
                System.out.println("FLOPPING");
                wantsToToggle = true;
                if(currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_CLOSED;
                }
                else if(currentState == SystemState.DEPLOYED_CLOSED || currentState == SystemState.INTAKING ||
                        currentState == SystemState.EXHAUSTING || currentState == SystemState.UNJAMMING){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if(currentState == SystemState.STOWED_CLOSED){
                    return SystemState.STOWED_OPEN;
                }
                else if(currentState == SystemState.STOWED_OPEN){
                    return SystemState.STOWED_CLOSED;
                }
            case WANTS_TO_DEPLOY:
                wantsToToggle = false;
                if(currentState == SystemState.STOWED_OPEN || currentState == SystemState.DEPLOYED_OPEN){
                    return SystemState.DEPLOYED_OPEN;
                }
                else if(currentState == SystemState.STOWED_CLOSED || currentState == SystemState.DEPLOYED_CLOSED){
                    return SystemState.DEPLOYED_CLOSED;
                }
                //default: Safest position (Intake is stowed inside the robot)
            default:
                return SystemState.STOWED_CLOSED;
        }
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    public SystemState getCurrentState(){
        return currentState;
    }

    public WantedState getWantedState(){
        return wantedState;
    }


    public void print() {
        
        System.out.println("Current State: " + currentState.toString());
        System.out.println("Wanted State: " + wantedState.toString());
        System.out.println("Previous State: " + previousState.toString());
        System.out.println("State Changed? " + stateChanged);
        
    }
}
