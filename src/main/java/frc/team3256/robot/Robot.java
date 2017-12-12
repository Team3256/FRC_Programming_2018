package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.Looper;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.subsystems.DriveTrain;

public class Robot extends IterativeRobot {

    DriveTrain driveTrain;
    Looper disabledLooper;
    Looper enabledLooper;
    ADXRS453_Calibrator gyroCalibrator;

    @Override
    public void robotInit() {
        driveTrain = DriveTrain.getInstance();
        gyroCalibrator = new ADXRS453_Calibrator(driveTrain.getGyro());
        //disabled looper -> recalibrate gyro
        disabledLooper = new Looper();
        disabledLooper.addLoop(gyroCalibrator);
        //enabled looper -> control loop for subsystems
        enabledLooper = new Looper();
        enabledLooper.addLoop(driveTrain);
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
    }

    @Override
    public void testPeriodic() {
    }
}
