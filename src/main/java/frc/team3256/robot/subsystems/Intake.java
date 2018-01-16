package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.robot.Constants;

public class Intake {
    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid pivotLeft, pivotRight;
    private static Intake intake;

    private Intake(){
        leftIntake = new VictorSP(Constants.kLeftIntakePort); //port to be determined
        rightIntake = new VictorSP(Constants.kRightIntakePort);
        pivotLeft = new DoubleSolenoid(Constants.kPivotLeftForward,Constants.kPivotLeftReverse);
        pivotRight = new DoubleSolenoid(Constants.kPivotRightForward, Constants.kPivotRightReverse);
    }

    public static Intake getInstance(){
        return intake == null ? intake = new Intake(): intake;
    }

    public void setMotorPower(double power){
        leftIntake.set(power);
        rightIntake.set(power);
    }

    public void pivotIn() {
        pivotLeft.set(DoubleSolenoid.Value.kForward);
        pivotRight.set(DoubleSolenoid.Value.kForward);
    }

    public void pivotOut() {
        pivotLeft.set(DoubleSolenoid.Value.kReverse);
        pivotRight.set(DoubleSolenoid.Value.kReverse);
    }

    public void intakeCube() {
        setMotorPower(Constants.intakeMotorPower);
    }
    public void outtakeCube() {
        setMotorPower(Constants.outtakeMotorPower);
    }

}