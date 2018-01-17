package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.robot.Constants;

public class ElevatorCarriage extends SubsystemBase{

    private VictorSP rollerLeft, rollerRight;
    private DoubleSolenoid squeezeSolenoid;

    private ElevatorCarriage() {
        rollerLeft = new VictorSP(Constants.kCarriageRollerLeft);
        rollerRight = new VictorSP(Constants.kCarriageRollerRight);
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
