package frc.team3256.robot.subsystems;

public class ElevatorCarriage {


    private SystemState currentState;
    private WantedState wantedState;
    private boolean stateChanged;
    private double startTime;


    private static ElevatorCarriage elevatorCarriage;

    public static ElevatorCarriage getInstance(){
        return elevatorCarriage == null ? elevatorCarriage = new ElevatorCarriage(): elevatorCarriage;
    }

    public enum SystemState {
        RECEIVING_FROM_INTAKE, //Self-explanatory
        //SECURING_CUBE, //Runs motors at a smaller power after receiving the cube to secure the cube
        SCORING_FORWARD, //Run rollers forward
        SCORING_BACKWARD, //Run rollers backward
        SQUEEZING_IDLE, //Actuators squeeze cube in place
        OPEN_IDLE //Actuators stay open
    }

    public enum WantedState {
        //Operator -> Whenever no buttons are pressed
        WANTS_TO_RECEIVE,
        //Condition -> After receiving cube
        //WANTS_TO_SECURE_CUBE,
        //Operator -> Score forward button
        WANTS_TO_SCORE_FORWARD,
        //Operator -> Score backward button
        WANTS_TO_SCORE_BACKWARD,
        //Operator -> Whenever robot has cube
        WANTS_TO_SQUEEZE_IDLE,
        //Operator -> Manual open
        WANTS_TO_OPEN
    }

    public void init(double timestamp){
        currentState = SystemState.OPEN_IDLE;
        stateChanged = true;
    }

    public void update(double timestamp) {
        SystemState newState;
        switch(currentState){
            case RECEIVING_FROM_INTAKE:
                newState = handleReceiveFromIntake();
                break;
            /*case SECURING_CUBE:
                newState = handleSecureCube();
                break;*/
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
        //If we have a cube, then we squeeze
        if (hasCube()){
            return SystemState.SQUEEZING_IDLE; //Previously SecureCube
        }
        return defaultStateTransfer();
    }

    private SystemState handleScoreForward(){
        return defaultStateTransfer();
    }

    private SystemState handleScoreBackward(){
        return defaultStateTransfer();
    }

    private SystemState handleSqueezeIdle(){
        return defaultStateTransfer();
    }

    private SystemState handleOpenIdle(){
        return defaultStateTransfer();
    }

    /*private SystemState handleSecureCube(){
        if(stateChanged){
            startTime = Timer.getFPGATimestamp();
            squeeze();
        }
        if((Timer.getFPGATimestamp() - startTime) < 3){
            runMotors(Constants.kCarriageSecurePower);
        }
        return defaultStateTransfer();
    }*/

    //default WantedState -> SystemState
    private SystemState defaultStateTransfer(){
        switch(wantedState){
            case WANTS_TO_RECEIVE:
                return SystemState.RECEIVING_FROM_INTAKE;

            /*case WANTS_TO_SECURE_CUBE:
                return SystemState.SECURING_CUBE;*/

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

    public SystemState getCurrentState() {
        return currentState;
    }

    public boolean hasCube(){
        return Intake.getInstance().hasCube();
    }

    public void end(double timestamp) {

    }


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

    public void selfTest() {

    }

    public void zeroSensors() {

    }
}
