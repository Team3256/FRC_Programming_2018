package frc.team3256.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Looper;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.lib.hardware.ADXRS453_Calibrator;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeChooser;
import frc.team3256.robot.auto.AutoModeExecuter;
import frc.team3256.robot.auto.actions.WaitAction;
import frc.team3256.robot.auto.modes.*;
import frc.team3256.robot.operation.ControlsInterface;
import frc.team3256.robot.operation.DualLogitechConfig;
import frc.team3256.robot.subsystems.*;

public class Robot extends IterativeRobot {

    DriveTrain driveTrain;
    Intake intake;
    Elevator elevator;
    ElevatorCarriage carriage;
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
        intake = Intake.getInstance();
        elevator = Elevator.getInstance();
        carriage = ElevatorCarriage.getInstance();

        poseEstimator = PoseEstimator.getInstance();
        gyroCalibrator = new ADXRS453_Calibrator(driveTrain.getGyro());

        //disabled looper -> recalibrate gyro
        disabledLooper = new Looper(Constants.kSlowLoopPeriod);
        disabledLooper.addLoops(gyroCalibrator);
        //enabled looper -> control loop for subsystems
        enabledLooper = new Looper(Constants.kControlLoopPeriod);
        enabledLooper.addLoops(driveTrain, poseEstimator, intake);

        subsystemManager = new SubsystemManager();
        subsystemManager.addSubsystems(driveTrain);

        //Dual Logitech Config
        controlsInterface = new DualLogitechConfig();

        autoModeChooser = new AutoModeChooser();
        autoModeChooser.addAutoModes(new TestTurnInPlaceAuto(), new TestTrajectoryAuto());
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
        AutoModeBase autoMode = new TestArcTrajectoryAuto();
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
        //System.out.println("Left: " + driveTrain.getLeftDistance());
        //System.out.println("Right: " + driveTrain.getRightDistance());
        SmartDashboard.putNumber("CurrTime", Timer.getFPGATimestamp());
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
        if (controlsInterface.unjamIntake()){
            //intake.setIntake(0.5, 0.5);
            intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        }
        else if (controlsInterface.getIntake()){
            System.out.println("INTAKE");
            intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            //intake.setIntake(-Constants.kLeftIntakePower, -Constants.kRightIntakePower);
        }
        else if (controlsInterface.getExhaust()){
            System.out.println("OUTTAKE");
            intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
            //intake.setIntake(0.5, 0.5);
        }
        else{
            System.out.println("IDLE");
            //intake.setIntake(0, 0);
            intake.setWantedState(Intake.WantedState.IDLE);
        }
        /*
        if (controlsInterface.getIntake()){
            intake.setIntake(0.5,0.5);
        }
        else if (controlsInterface.getExhaust()){
            intake.setIntake(-0.5, -0.5);
        }
        else
            intake.setIntake(0,0);
        /*
        if (controlsInterface.manualSqueezeCarriage()){
            carriage.squeeze();
        }
        else
            carriage.open();

        if (controlsInterface.manualElevatorUp() > 0.25){
            elevator.setOpenLoop(0.5);
        }
        else if (controlsInterface.manualElevatorDown() > 0.25){
            elevator.setOpenLoop(-0.5);
        }
        else
            elevator.setOpenLoop(0);

        if (controlsInterface.scoreFront()){
            carriage.runMotors(0.5);
        }
        else if (controlsInterface.scoreRear()){
            carriage.runMotors(-0.5);
        }
        else
            carriage.runMotors(0);
        */

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
