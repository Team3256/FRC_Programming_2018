package frc.team3256.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.Looper;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeChooser;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.modes.Final.*;
import frc.team3256.robot.auto.modes.Right.RightRobotRightScaleRightSwitchThreeCubeAuto;
import frc.team3256.robot.auto.modes.Right.RightRobotRightSwitchAuto;
import frc.team3256.robot.auto.modes.Test.*;
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
        subsystemManager.addSubsystems(driveTrain, intake);

        autoModeChooser = new AutoModeChooser();
        autoModeChooser.addAutoModes(new DoNothingAuto(), new CrossBaselineForwardAuto(), new CrossBaselineBackwardAuto(),
                new CenterSwitchAuto(), new RightRobotRightScaleRightSwitchThreeCubeAuto(), new RightRobotRightSwitchAuto(), new RightScaleAuto());

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

        GameDataAccessor.getGameData();

        autoModeExecuter = new AutoModeExecuter();

        // AutoModeBase autoMode = new TestPurePursuitAuto();
        AutoModeBase autoMode = autoModeChooser.getChosenAuto(NetworkTableInstance.getDefault().getEntry("ChosenAuto").getString("DoNothingAuto"));
        //AutoModeBase autoModeTest = new RightRobotRightSwitchAuto();
        autoMode = autoMode == null ? new DoNothingAuto() : autoMode;
        System.out.println(autoMode);
        autoModeExecuter.setAutoMode(autoMode);
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
        if (!Elevator.getInstance().isHomed()) System.out.println("Homed: " + Elevator.getInstance().isHomed());

    }

    @Override
    public void autonomousPeriodic(){
        GameDataAccessor.getGameData();
    }

    @Override
    public void teleopPeriodic() {
        teleopUpdater.update();
        System.out.println("ELEVATOR STATE:" + elevator.getCurrentState());
        System.out.println("TARGET HEIGHT: " + elevator.getTargetHeight());
        System.out.println("CURRENT HEIGHT: " + elevator.getHeight());
        System.out.println("WANTED STATE: " + elevator.getWantedState());
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void robotPeriodic(){
    }
}
