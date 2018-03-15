package frc.team3256.robot.subsystems;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.hardware.SharpIR;
import frc.team3256.robot.Constants;

public class Intake {
    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid flopperActuator;
    private SharpIR cubeDetector;

    private SystemState currentState = SystemState.CLOSED_IDLE;
    private SystemState previousState;
    private WantedState wantedState;
    private WantedState prevWantedState;

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

        flopperActuator = new DoubleSolenoid(Constants.kIntakeFlopForward, Constants.kIntakeFlopReverse);

        cubeDetector = new SharpIR(Constants.kIntakeSharpIR, Constants.kIntakeSharpIRMinVoltage, Constants.kIntakeSharpIRMaxVoltage);

        leftIntake.setInverted(false);
        rightIntake.setInverted(false);
    }

    public static Intake getInstance(){
        return instance == null ? instance = new Intake(): instance;
    }

    public void init(){
        prevWantedState = WantedState.IDLE;
        wantedState = WantedState.IDLE;
        stateChanged = true;
    }

    public void update(){
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

    private SystemState handleIntake(){
        if(stateChanged){
            closeFlopper();
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
            closeFlopper();
        }
        setIntake(Constants.kIntakeExhaustPower,Constants.kIntakeExhaustPower);
        return defaultStateTransfer();
    }

    private SystemState handleClosedIdle(){
        closeFlopper();
        return defaultStateTransfer();
    }

    private SystemState handleOpenIdle(){
        openFlopper();
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

    public void closeFlopper(){
        flopperActuator.set(DoubleSolenoid.Value.kForward); //Direction TBD
    }

    public void openFlopper(){
        flopperActuator.set(DoubleSolenoid.Value.kReverse); //Direction TBD
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
                if(currentState == SystemState.INTAKING || currentState == SystemState.EXHAUSTING ||
                        currentState == SystemState.SCORING || currentState == SystemState.SCORING_SLOW){
                    return SystemState.CLOSED_IDLE;
                }
                else if(currentState == SystemState.OPEN_IDLE){
                    return SystemState.OPEN_IDLE;
                }
            default:
                return SystemState.CLOSED_IDLE;
        }
    }

    public SystemState getCurrentState() { return currentState; }

    public WantedState getWantedState() { return wantedState; }

}
