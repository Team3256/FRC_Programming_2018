package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Looper;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.modes.TestDriveToDistanceAuto;
import frc.team3256.robot.auto.modes.TestTrajectoryAuto;
import frc.team3256.robot.operation.ControlsInterface;
import frc.team3256.robot.operation.DualLogitechConfig;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.SubsystemManager;

public class Robot extends IterativeRobot {

    DriveTrain driveTrain;
    PoseEstimator poseEstimator;
    Looper disabledLooper;
    Looper enabledLooper;
    SubsystemManager subsystemManager;
    ADXRS453_Calibrator gyroCalibrator;
    ControlsInterface controlsInterface;
    AutoModeExecuter autoModeExecuter;

    @Override
    public void robotInit() {
        driveTrain = DriveTrain.getInstance();
        poseEstimator = new PoseEstimator();
        gyroCalibrator = new ADXRS453_Calibrator(driveTrain.getGyro());

        //disabled looper -> recalibrate gyro
        disabledLooper = new Looper(Constants.kSlowLoopPeriod);
        disabledLooper.addLoops(gyroCalibrator);
        //enabled looper -> control loop for subsystems
        enabledLooper = new Looper(Constants.kControlLoopPeriod);
        enabledLooper.addLoops(driveTrain, poseEstimator);

        subsystemManager = new SubsystemManager();
        subsystemManager.addSubsystems(driveTrain);

        //Dual Logitech Config
        controlsInterface = new DualLogitechConfig();
    }

    @Override
    public void disabledInit() {
        driveTrain.resetEncoders();
        enabledLooper.stop();
        disabledLooper.start();
    }

    @Override
    public void autonomousInit() {
        disabledLooper.stop();
        enabledLooper.start();

        autoModeExecuter = new AutoModeExecuter();
        //autoModeExecuter.setAutoMode(new TestDriveToDistanceAuto());
        autoModeExecuter.setAutoMode(new TestTrajectoryAuto());
        autoModeExecuter.start();
    }

    @Override
    public void teleopInit() {
        disabledLooper.stop();
        enabledLooper.start();
        //driveTrain.setVelocitySetpoint(0,0);
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
        double throttle = controlsInterface.getThrottle();
        double turn = -controlsInterface.getTurn();
        boolean quickTurn = controlsInterface.getQuickTurn();
        DrivePower power = TeleopDriveController.curvatureDrive(throttle, turn, quickTurn);
        driveTrain.setOpenLoop(power);
    }

    @Override
    public void testPeriodic() {
    }

    public void allPeriodic(){
        subsystemManager.outputToDashboard();
        enabledLooper.outputToDashboard();
        disabledLooper.outputToDashboard();
    }
}
