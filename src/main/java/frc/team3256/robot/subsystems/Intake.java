package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.robot.Constants;

public class Intake {
    private VictorSP leftIntake, rightIntake;
    private DoubleSolenoid pivotLeft, pivotRight;
    private static Intake intake;

    private Intake(){
        leftIntake = new VictorSP(Constants.leftIntakePort); //port to be determined
        rightIntake = new VictorSP(Constants.rightIntakePort);
        pivotLeft = new DoubleSolenoid(Constants.pivotLeftForward,Constants.pivotLeftReverse);
        pivotRight = new DoubleSolenoid(Constants.pivotRightForward, Constants.pivotRightReverse);
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