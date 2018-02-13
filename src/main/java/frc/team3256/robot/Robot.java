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
        enabledLooper.addLoops(driveTrain, poseEstimator);

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

        //Intake unjam, intake, and exhaust
        if(controlsInterface.unjamIntake()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_RECEIVE);
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
        }
        else if (controlsInterface.getIntake()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_RECEIVE);
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
        }
        else if (controlsInterface.getExhaust()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_RECEIVE);
            elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
        }
        else{
            intake.setWantedState(Intake.WantedState.IDLE);
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }

        //Intake toggle pivot & flop
        if(controlsInterface.togglePivot()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }
        else if(controlsInterface.toggleFlop()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }
        else{
            intake.setWantedState(Intake.WantedState.IDLE);
        }

        //Elevator manuals & presets
        if (controlsInterface.manualElevatorUp() > 0.25){
            elevator.setWantedState(Elevator.WantedState.MANUAL_UP);
        }
        else if(controlsInterface.manualElevatorDown() > 0.25){
            elevator.setWantedState(Elevator.WantedState.MANUAL_DOWN);
        }
        else if(controlsInterface.switchPreset()){
            elevator.setWantedState(Elevator.WantedState.SWITCH);
        }
        else if(controlsInterface.scalePresetLow()){
            elevator.setWantedState(Elevator.WantedState.LOW_SCALE);
        }
        else if(controlsInterface.scalePresetHigh()){
            elevator.setWantedState(Elevator.WantedState.HIGH_SCALE);
        }
        else{
            elevator.setWantedState(Elevator.WantedState.HOLD);
        }

        //ElevatorCarriage squeeze & scoring
        if(controlsInterface.manualSqueezeCarriage()){
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }

        if(controlsInterface.scoreFront()){
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_FORWARD);
        }
        else if (controlsInterface.scoreRear()){
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_BACKWARD);
        }
        else{
            carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }


        //System.out.println("LEFT ENCODER: " + driveTrain.getLeftVelocity() + "RIGHT ENCODER: " + driveTrain.getRightVelocity());
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
