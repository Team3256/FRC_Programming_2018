package frc.team3256.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.lib.Looper;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeChooser;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.modes.Final.*;
import frc.team3256.robot.auto.modes.Right.RightRobotLeftScaleRightSwitchThreeCubeAuto;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.operation.TeleopUpdater;
import frc.team3256.robot.subsystems.*;

import java.io.FileWriter;
import java.io.IOException;

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
    FileWriter logFileWriter;

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
                new CenterSwitchAuto(), new RightAuto(), new RightRobotLeftScaleRightSwitchThreeCubeAuto());

        NetworkTableInstance.getDefault().getEntry("AutoOptions").setStringArray(autoModeChooser.getAutoNames());
        NetworkTableInstance.getDefault().getEntry("ChosenAuto").setString("DoNothingAuto");
        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(480, 240);
    }

    @Override
    public void disabledInit() {
        enabledLooper.stop();
        disabledLooper.start();
        driveTrain.setBrake();
        if (logFileWriter != null) {
            try {
                logFileWriter.close();
                logFileWriter = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        try {
            logFileWriter = new FileWriter("/home/lvuser/log" + System.currentTimeMillis() + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        driveTrain.resetNominal();
        //driveTrain.setVelocitySetpoint(0,0);
    }

    @Override
    public void testInit() {
    }

    @Override
    public void disabledPeriodic() {
        if (!Elevator.getInstance().isHomed()) System.out.println("Homed: " + Elevator.getInstance().isHomed());
        subsystemManager.outputToDashboard();
        //System.out.println(elevator.getRawEncoder());
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
        logToFile("" + DriverStation.getInstance().getMatchTime() + ": mode " + driveTrain.getMode() + " - left output " + driveTrain.getLeftOutputVoltage() + " - right output " + driveTrain.getRightOutputVoltage() + " - throttle " + teleopUpdater.getThrottle() + " - turn " + teleopUpdater.getTurn());
    }

    @Override
    public void testPeriodic() {
    }

    @Override
    public void robotPeriodic(){
        subsystemManager.outputToDashboard();
    }

    public void logToFile(String s) {
        if (logFileWriter != null)
            try {
                logFileWriter.write(s + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
