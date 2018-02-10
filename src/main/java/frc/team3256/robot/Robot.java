package frc.team3256.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Looper;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeChooser;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.modes.DoNothingAuto;
import frc.team3256.robot.auto.modes.TestDriveToDistanceAuto;
import frc.team3256.robot.auto.modes.TestTrajectoryAuto;
import frc.team3256.robot.auto.modes.TestTurnInPlaceAuto;
import frc.team3256.robot.operation.ControlsInterface;
import frc.team3256.robot.operation.DualLogitechConfig;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.SubsystemManager;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class Robot extends IterativeRobot {

    DriveTrain driveTrain;
    PoseEstimator poseEstimator;
    Looper disabledLooper;
    Looper enabledLooper;
    SubsystemManager subsystemManager;
    ADXRS453_Calibrator gyroCalibrator;
    ControlsInterface controlsInterface;
    AutoModeExecuter autoModeExecuter;
    AutoModeChooser autoModeChooser;

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

        autoModeChooser = new AutoModeChooser();
        autoModeChooser.addAutoModes(new TestDriveToDistanceAuto(), new TestTurnInPlaceAuto(), new TestTrajectoryAuto());
        autoModeChooser.addAutoModes(new DoNothingAuto());

        NetworkTableInstance.getDefault().getEntry("AutoOptions").setStringArray(autoModeChooser.getAutoNames());
        NetworkTableInstance.getDefault().getEntry("ChosenAuto").setString("DoNothingAuto");
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
        //AutoModeBase autoMode = autoModeChooser.getChosenAuto(NetworkTableInstance.getDefault().getEntry("ChosenAuto").getString("DoNothingAuto"));
        AutoModeBase autoMode = new TestTrajectoryAuto();
        autoMode = autoMode == null ? new DoNothingAuto() : autoMode;
        autoModeExecuter.setAutoMode(autoMode);
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
        System.out.println("Left: " + driveTrain.getLeftDistance());
        System.out.println("Right: " + driveTrain.getRightDistance());
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        double throttle = controlsInterface.getThrottle();
        double turn = controlsInterface.getTurn();
        boolean quickTurn = controlsInterface.getQuickTurn();
        boolean shiftDown = controlsInterface.getLowGear();
        DrivePower power = TeleopDriveController.curvatureDrive(throttle, turn, quickTurn);
        driveTrain.setOpenLoop(power);
        driveTrain.setHighGear(!shiftDown);
        System.out.println("LEFT ENCODER: " + driveTrain.getLeftVelocity() + "RIGHT ENCODER: " + driveTrain.getRightVelocity());
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
