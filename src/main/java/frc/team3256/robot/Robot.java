package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.robot.subsystems.DriveTrain;

public class Robot extends IterativeRobot {

    @Override
    public void robotInit() {
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void testInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        System.out.println(
                "L-ENC: "
                        + DriveTrain.getInstance().getLeftDistance()
                        + "\tR_ENC: "
                        + DriveTrain.getInstance().getRightDistance());
        TeleopDriveController.tankDrive(-1.0*OI.getLeft(), OI.getRight());
    }

    @Override
    public void testPeriodic() {
    }
}
