package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.SharpIR;
import frc.team3256.robot.Constants;

public class Intake implements Loop{
    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid squeezeActuator;
    private SharpIR cubeDetector;

    private SystemState currentState = SystemState.CLOSED_IDLE;
    private SystemState previousState;
    private WantedState wantedState;

    private boolean stateChanged;

    private static Intake instance;

    public enum SystemState{
        INTAKING,
        EXHAUSTING,
        CLOSED_IDLE,
        OPEN_IDLE,
        SCORING,
        SCORING_SLOW
    }

    public enum WantedState{
        WANTS_TO_INTAKE,
        WANTS_TO_EXHAUST,
        WANTS_TO_OPEN,
        WANTS_TO_SCORE,
        WANTS_TO_SCORE_SLOW,
        IDLE
    }

    private Intake(){
        leftIntake = new VictorSP(Constants.kLeftIntakePort);
        rightIntake = new VictorSP(Constants.kRightIntakePort);

        squeezeActuator = new DoubleSolenoid(Constants.kIntakeFlopForward, Constants.kIntakeFlopReverse);

        cubeDetector = new SharpIR(Constants.kIntakeSharpIR, Constants.kIntakeSharpIRMinVoltage, Constants.kIntakeSharpIRMaxVoltage);

        leftIntake.setInverted(false);
        rightIntake.setInverted(false);
    }

    public static Intake getInstance(){
        return instance == null ? instance = new Intake(): instance;
    }

    @Override
    public void init(double timestamp){
        wantedState = WantedState.IDLE;
        stateChanged = false;
    }

    @Override
    public void update(double timestamp){
        SystemState newState = SystemState.CLOSED_IDLE;
        switch (currentState){
            case INTAKING:
                newState = handleIntake();
                break;
            case EXHAUSTING:
                newState = handleExhaust();
                break;
            case CLOSED_IDLE:
                newState = handleClosedIdle();
                break;
            case OPEN_IDLE:
                newState = handleOpenIdle();
                break;
            case SCORING:
                newState = handleScoring();
            break;
            case SCORING_SLOW:
                newState = handleScoringSlow();
                break;
        }
        if(newState != currentState){
            System.out.println("\tPREV_STATE:" + previousState + "\tCURR_STATE:" + currentState +
                    "\tNEW_STATE:" + newState);
            previousState = currentState;
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;
    }

    @Override
    public void end(double timestamp){

    }

    private SystemState handleIntake(){
        if(stateChanged){
            closesqueeze();
        }
        if(hasCube()){
            setIntake(0,0);
            return SystemState.CLOSED_IDLE;
        }
        else{
            setIntake(Constants.kLeftIntakePower, Constants.kRightIntakePower);
        }
        return defaultStateTransfer();
    }

    private SystemState handleExhaust(){
        if(stateChanged){
            closesqueeze();
        }
        setIntake(Constants.kIntakeExhaustPower,Constants.kIntakeExhaustPower);
        return defaultStateTransfer();
    }

    private SystemState handleClosedIdle(){
        closesqueeze();
        return defaultStateTransfer();
    }

    private SystemState handleOpenIdle(){
        opensqueeze();
        return defaultStateTransfer();
    }

    private SystemState handleScoring(){
        setIntake(Constants.kIntakeScoreForwardPower, Constants.kIntakeScoreForwardPower);
        return defaultStateTransfer();
    }

    private SystemState handleScoringSlow(){
        setIntake(Constants.kIntakeScoreForwardSlowPower, Constants.kIntakeScoreForwardSlowPower);
        return defaultStateTransfer();
    }

    public void setIntake(double left, double right){
        leftIntake.set(left);
        rightIntake.set(right);
    }

    public boolean hasCube(){
        return false; //cubeDetector.isTriggered();
    }

    public void closesqueeze(){
        squeezeActuator.set(DoubleSolenoid.Value.kForward); //Direction TBD
    }

    public void opensqueeze(){
        squeezeActuator.set(DoubleSolenoid.Value.kReverse); //Direction TBD
    }



    private SystemState defaultStateTransfer(){
        switch (wantedState){
            case WANTS_TO_INTAKE:
                return SystemState.INTAKING;
            case WANTS_TO_EXHAUST:
                return SystemState.EXHAUSTING;
            case WANTS_TO_OPEN:
                return SystemState.OPEN_IDLE;
            case WANTS_TO_SCORE:
                return SystemState.SCORING;
            case WANTS_TO_SCORE_SLOW:
                return SystemState.SCORING_SLOW;
            case IDLE:
                if(currentState == SystemState.OPEN_IDLE){
                    return SystemState.OPEN_IDLE;
                }
                else{
                    return SystemState.CLOSED_IDLE;
                }
            default:
                return SystemState.CLOSED_IDLE;
        }
    }

    public SystemState getCurrentState() { return currentState; }

    public WantedState getWantedState() { return wantedState; }

}
