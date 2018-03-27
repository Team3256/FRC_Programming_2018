package frc.team3256.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.lib.Looper;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeChooser;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.modes.Center.CenterLeftSwitchAuto;
import frc.team3256.robot.auto.modes.Center.CenterRightSwitchAuto;
import frc.team3256.robot.auto.modes.Final.*;
import frc.team3256.robot.auto.modes.Right.RightRobotLeftScaleRightSwitchThreeCubeAuto;
import frc.team3256.robot.auto.modes.Test.IntakeTestAuto;
import frc.team3256.robot.auto.modes.Test.TestArcTrajectoryAuto;
import frc.team3256.robot.auto.modes.Test.TestPurePursuitAuto;
import frc.team3256.robot.auto.modes.Test.TestTurnInPlaceAuto;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.operation.TeleopUpdater;
import frc.team3256.robot.subsystems.*;

public class Robot extends IterativeRobot {

    DriveTrain driveTrain;
    Intake intake;
    Elevator elevator;
    Carriage carriage;
    PoseEstimator poseEstimator;
    Looper disabledLooper;
    Looper enabledLooper;
    SubsystemManager subsystemManager;
    ADXRS453_Calibrator gyroCalibrator;
    AutoModeExecuter autoModeExecuter;
    AutoModeChooser autoModeChooser;
    TeleopUpdater teleopUpdater;

    Compressor compressor;

    @Override
    public void robotInit() {
        compressor = new Compressor();
        compressor.setClosedLoopControl(true);

        driveTrain = DriveTrain.getInstance();
        intake = Intake.getInstance();
        elevator = Elevator.getInstance();
        carriage = Carriage.getInstance();

        teleopUpdater = new TeleopUpdater();

        poseEstimator = PoseEstimator.getInstance();
        gyroCalibrator = new ADXRS453_Calibrator(driveTrain.getGyro());

        //disabled looper -> recalibrate gyro
        disabledLooper = new Looper(Constants.kSlowLoopPeriod);
        disabledLooper.addLoops(gyroCalibrator, poseEstimator);
        //enabled looper -> control loop for subsystems
        enabledLooper = new Looper(Constants.kControlLoopPeriod);
        enabledLooper.addLoops(driveTrain, poseEstimator, intake, carriage, elevator);

        subsystemManager = new SubsystemManager();
        subsystemManager.addSubsystems(driveTrain, intake, elevator, carriage);

        autoModeChooser = new AutoModeChooser();
        autoModeChooser.addAutoModes(new DoNothingAuto(), new CrossBaselineForwardAuto(), new CrossBaselineBackwardAuto(),
                new CenterSwitchAuto(), new RightSwitchAuto(), new RightScaleAuto(), new RightScaleTwoCubeAuto());

        NetworkTableInstance.getDefault().getEntry("AutoOptions").setStringArray(autoModeChooser.getAutoNames());
        NetworkTableInstance.getDefault().getEntry("ChosenAuto").setString("DoNothingAuto");
        UsbCamera cam = CameraServer.getInstance().startAutomaticCapture(0);
        cam.setVideoMode(VideoMode.PixelFormat.kYUYV, 377, 236, 30);
        CameraServer.getInstance().startAutomaticCapture(cam);
        //System.out.println(camera.getVideoMode().pixelFormat);
    }

    @Override
    public void disabledInit() {
        enabledLooper.stop();
        disabledLooper.start();
        driveTrain.setBrake();
        driveTrain.setCoastMode();
    }

    @Override
    public void autonomousInit() {
        disabledLooper.stop();
        enabledLooper.start();

        GameDataAccessor.getGameData();

        autoModeExecuter = new AutoModeExecuter();

        // AutoModeBase autoMode = new TestPurePursuitAuto();
        AutoModeBase autoMode = autoModeChooser.getChosenAuto(NetworkTableInstance.getDefault().getEntry("ChosenAuto").getString("DoNothingAuto"));
        //AutoModeBase autoModeTest = new RightRobotLeftScaleRightSwitchThreeCubeAuto(); //to be commented out
        autoMode = autoMode == null ? new DoNothingAuto() : autoMode;
        System.out.println(autoMode);
        autoModeExecuter.setAutoMode(autoMode); //to be changed to automode
        autoModeExecuter.start();
    }

    @Override
    public void teleopInit() {
        disabledLooper.stop();
        enabledLooper.start();
        driveTrain.setBrake();
        driveTrain.enableRamp();
        driveTrain.resetNominal();
        elevator.setWantedState(Elevator.WantedState.WANTS_TO_HOME);
        //driveTrain.setVelocitySetpoint(0,0);
    }

    @Override
    public void testInit() {
    }

    @Override
    public void disabledPeriodic() {
        //subsystemManager.outputToDashboard();
        //System.out.println("CUBE ANGLE: " + driveTrain.getCubeOffsetAngle());
        //System.out.println(elevator.getRawEncoder());
}

    @Override
    public void autonomousPeriodic(){
        GameDataAccessor.getGameData();
    }

    @Override
    public void teleopPeriodic() {
        teleopUpdater.update();
        //subsystemManager.outputToDashboard();
        //System.out.println("VOLTAGE: " + elevator.getOutputVoltage());
        /*
        System.out.println("TARGET: " + elevator.getTargetHeight());
        System.out.println("CURR: " + elevator.getHeight());
        System.out.println("VOLTAGE : " + elevator.getOutputVoltage());
        System.out.println("MODE: " + elevator.getCurrentState());
        */
        //System.out.println("DISTANCE TRAVELED: " + (Math.abs(DriveTrain.getInstance().getRightDistance()) + DriveTrain.getInstance().getLeftDistance())/2);
        //System.out.println("ROTATIONS: " + driveTrain.getAngle().degrees());
        //System.out.println("VELOCITY: " + (driveTrain.getRightVelocity() + driveTrain.getLeftVelocity())/2);
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void robotPeriodic(){
        //subsystemManager.outputToDashboard();
        SmartDashboard.putBoolean("CompressorOn", compressor.enabled());
    }
}
