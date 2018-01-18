package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.robot.Constants;

public class Intake extends SubsystemBase{
    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid pivotLeft, pivotRight;
    public CubeHandlerState cubeHandlerState;
    private CubeHandlerState previousState;
    private static Intake intake;

    public enum CubeHandlerState{
        INTAKING,
        EXHAUSTING,
        UNJAMMING,
        OPEN_IDLE,
        CLOSED_IDLE
    }

    public void update() {
        switch(cubeHandlerState) {
            case INTAKING:
                if (previousState != (cubeHandlerState.INTAKING)){
                    previousState = cubeHandlerState.INTAKING;
                    pivotLeft.set(DoubleSolenoid.Value.kReverse);
                    pivotRight.set(DoubleSolenoid.Value.kReverse);
                }
                leftIntake.set(Constants.intakeMotorPower);
                rightIntake.set(Constants.intakeMotorPower);
                break;
            case EXHAUSTING:
                if (previousState != cubeHandlerState.EXHAUSTING){
                    previousState = cubeHandlerState.EXHAUSTING;
                    pivotLeft.set(DoubleSolenoid.Value.kReverse);
                    pivotRight.set(DoubleSolenoid.Value.kReverse);
                }
                leftIntake.set(Constants.outtakeMotorPower);
                rightIntake.set(Constants.outtakeMotorPower);
                break;
            case UNJAMMING:
                leftIntake.set(0);
                rightIntake.set(0);
                break;
            case OPEN_IDLE:
                pivotLeft.set(DoubleSolenoid.Value.kForward);
                pivotRight.set(DoubleSolenoid.Value.kForward);
                break;
            case CLOSED_IDLE:
                pivotLeft.set(DoubleSolenoid.Value.kReverse);
                pivotRight.set(DoubleSolenoid.Value.kReverse);
                break;
        }
        previousState = cubeHandlerState;
    }

    private Intake(){
        leftIntake = new VictorSP(Constants.kLeftIntakePort); //port to be determined
        rightIntake = new VictorSP(Constants.kRightIntakePort);
        pivotLeft = new DoubleSolenoid(Constants.kPivotLeftForward,Constants.kPivotLeftReverse);
        pivotRight = new DoubleSolenoid(Constants.kPivotRightForward, Constants.kPivotRightReverse);
    }

    public static Intake getInstance(){
        return intake == null ? intake = new Intake(): intake;
    }

    public void setState(CubeHandlerState wantedState){
        cubeHandlerState = wantedState;
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
}