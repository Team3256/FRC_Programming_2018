package frc.team3256.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Looper;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeChooser;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.modes.*;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.operation.ControlsInterface;
import frc.team3256.robot.operation.DualLogitechConfig;
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
    ControlsInterface controlsInterface;
    AutoModeExecuter autoModeExecuter;
    AutoModeChooser autoModeChooser;
    TeleopUpdater teleopUpdater;

    Compressor compressor;

    boolean prevPivot = false;
    boolean prevFlop = false;

    boolean manualelev = false;

    boolean autoSet = false;

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
        subsystemManager.addSubsystems(driveTrain, intake);

        //Dual Logitech Config
        controlsInterface = new DualLogitechConfig();

        autoModeChooser = new AutoModeChooser();
        autoModeChooser.addAutoModes(new DoNothingAuto(), new TestTurnInPlaceAuto(), new TestTrajectoryAuto(), new TestWebappAuto(), new TestPurePursuitAuto());

        NetworkTableInstance.getDefault().getEntry("AutoOptions").setStringArray(autoModeChooser.getAutoNames());
        NetworkTableInstance.getDefault().getEntry("ChosenAuto").setString("DoNothingAuto");

    }

    @Override
    public void disabledInit() {
        enabledLooper.stop();
        disabledLooper.start();
        driveTrain.setBrake();
    }

    @Override
    public void autonomousInit() {
        disabledLooper.stop();
        enabledLooper.start();

        autoModeExecuter = new AutoModeExecuter();
        AutoModeBase autoMode = new TestArcTrajectoryAuto();

        if(GameDataAccessor.dataFound() && !autoSet){
            autoMode = GameDataAccessor.getAutoMode();
            autoSet = true;
        }

        intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);

        // AutoModeBase autoMode = new TestPurePursuitAuto();
        //AutoModeBase autoMode = autoModeChooser.getChosenAuto(NetworkTableInstance.getDefault().getEntry("ChosenAuto").getString("DoNothingAuto"));
        AutoModeBase autoModeTest = new RightRobotRightScaleRightSwitchThreeCubeAuto();
        autoMode = autoMode == null ? new DoNothingAuto() : autoMode;
        autoModeExecuter.setAutoMode(autoModeTest);
        autoModeExecuter.start();
    }

    @Override
    public void teleopInit() {
        disabledLooper.stop();
        enabledLooper.start();
        driveTrain.setBrake();
        driveTrain.enableRamp();
        //driveTrain.setVelocitySetpoint(0,0);
    }

    @Override
    public void testInit() {
    }

    @Override
    public void disabledPeriodic() {
        //System.out.println("Left: " + driveTrain.getLeftDistance());
        //System.out.println("Right: " + driveTrain.getRightDistance());
        //SmartDashboard.putNumber("CurrTime", Timer.getFPGATimestamp());
        //System.out.println("Measure Period:       " + disabledLooper.getMeasuredPeriod());
        //System.out.println("Pose: " + poseEstimator.getPose());
        //System.out.println("Left Encoder: " + driveTrain.inchesToSensorUnits(driveTrain.getLeftDistance()));
        //System.out.println("Right Encoder:            " + driveTrain.inchesToSensorUnits(driveTrain.getRightDistance()));
        //System.out.println("Voltage: " + intake.getVoltage());
        //System.out.println("Is Triggered: " + intake.hasCube());
        //System.out.println("Hall Effect Triggered: " + elevator.isTriggered() + "\n" + "Current Velocity: " + elevator.getVelocity());

    }

    @Override
    public void autonomousPeriodic(){
        /*
        if(GameDataAccessor.dataFound() && !autoSet) {
            autoModeExecuter.setAutoMode(GameDataAccessor.getAutoMode());
            autoSet = true;
        } else {
            autoModeExecuter.setAutoMode(new DoNothingAuto());
        }
        autoModeExecuter.start();
        */
        //System.out.println("DRIVE MODE: " + driveTrain.getMode());
    }

    @Override
    public void teleopPeriodic() {
        teleopUpdater.update();
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void robotPeriodic(){
        //subsystemManager.outputToDashboard();
        //enabledLooper.outputToDashboard();
        //disabledLooper.outputToDashboard();
    }
}
