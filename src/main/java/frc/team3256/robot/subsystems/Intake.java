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
    private boolean wantsToToggle = false;

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
        WANTS_TO_TOGGLE_FLOP,
        WANTS_TO_SCORE,
        WANTS_TO_SCORE_SLOW
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

    }

    public void update(){
        SystemState newState;
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
        return;
    }

    private SystemState handleExhaust(){
        setIntake(Constants.kIntakeExhaustPower,Constants.kIntakeExhaustPower);
    }

    private SystemState handleClosedIdle(){

    }

    private SystemState handleOpenIdle(){

    }

    private SystemState handleScoring(){

    }

    private SystemState handleScoringSlow(){

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
            case WANTS_TO_TOGGLE_FLOP:
                if (wantsToToggle) {

                }
            return SystemState.;
            case WANTS_TO_SCORE:
            return SystemState.SCORING;
            case WANTS_TO_SCORE_SLOW:
            return SystemState.SCORING_SLOW;
        }
    }

    public SystemState getCurrentState() { return currentState; }

    public WantedState getWantedState() { return wantedState; }

}
