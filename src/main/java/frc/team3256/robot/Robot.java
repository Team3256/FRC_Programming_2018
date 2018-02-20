package frc.team3256.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Loop;
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

    Compressor compressor;

    double counter = 0;

    boolean prevPivot = false;
    boolean prevFlop = false;

    boolean autoSet = false;

    @Override
    public void robotInit() {

        compressor = new Compressor();
        compressor.setClosedLoopControl(true);

        driveTrain = DriveTrain.getInstance();
        intake = Intake.getInstance();
        elevator = Elevator.getInstance();
        carriage = ElevatorCarriage.getInstance();

        poseEstimator = PoseEstimator.getInstance();
        gyroCalibrator = new ADXRS453_Calibrator(driveTrain.getGyro());

        //disabled looper -> recalibrate gyro
        disabledLooper = new Looper(Constants.kSlowLoopPeriod);
        disabledLooper.addLoops(gyroCalibrator, poseEstimator);
        //enabled looper -> control loop for subsystems
        enabledLooper = new Looper(Constants.kControlLoopPeriod);
        enabledLooper.addLoops(driveTrain, poseEstimator, intake);

        subsystemManager = new SubsystemManager();
        subsystemManager.addSubsystems(driveTrain, intake);

        //Dual Logitech Config
        controlsInterface = new DualLogitechConfig();

        autoModeChooser = new AutoModeChooser();
        autoModeChooser.addAutoModes(new DoNothingAuto(), new TestTurnInPlaceAuto(), new TestTrajectoryAuto());

        NetworkTableInstance.getDefault().getEntry("AutoOptions").setStringArray(autoModeChooser.getAutoNames());
        NetworkTableInstance.getDefault().getEntry("ChosenAuto").setString("DoNothingAuto");
    }

    @Override
    public void disabledInit() {
        enabledLooper.stop();
        disabledLooper.start();
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

        // AutoModeBase autoMode = new TestPurePursuitAuto();
        //AutoModeBase autoMode = autoModeChooser.getChosenAuto(NetworkTableInstance.getDefault().getEntry("ChosenAuto").getString("DoNothingAuto"));
        AutoModeBase autoModeTest = new TestArcTrajectoryAuto();
        autoMode = autoMode == null ? new DoNothingAuto() : autoMode;
        autoModeExecuter.setAutoMode(autoModeTest);
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
        //SmartDashboard.putNumber("CurrTime", Timer.getFPGATimestamp());
        //System.out.println("Measure Period:       " + disabledLooper.getMeasuredPeriod());
        //System.out.println("Pose: " + poseEstimator.getPose());
        //System.out.println("Left Encoder: " + driveTrain.inchesToSensorUnits(driveTrain.getLeftDistance()));
        //System.out.println("Right Encoder:            " + driveTrain.inchesToSensorUnits(driveTrain.getRightDistance()));

    }

    @Override
    public void autonomousPeriodic(){
        if(GameDataAccessor.dataFound() && !autoSet) {
            autoModeExecuter.setAutoMode(GameDataAccessor.getAutoMode());
            autoSet = true;
        } else {
            autoModeExecuter.setAutoMode(new DoNothingAuto());
        }
        autoModeExecuter.start();
    }

    @Override
    public void teleopPeriodic() {

        //System.out.println("MODE: " + elevator.getTalonControlMode());
        driveTrain.setOpenLoop(0,0);
        /*double throttle = controlsInterface.getThrottle();

        System.out.println("HEIGHT:" + elevator.getHeight());

        if (controlsInterface.toggleFlop()){
            elevator.setTargetPosition(30,Constants.kElevatorFastUpSlot);
        }
        else{
            if (Math.abs(throttle) < 0.05) throttle = 0;
            elevator.setOpenLoop(throttle);
        }


        System.out.println(driveTrain.getMode());
        SmartDashboard.putNumber("Velocity",(driveTrain.getLeftVelocityError()+driveTrain.getRightVelocityError())/2);


        double throttle = controlsInterface.getThrottle();
        double turn = controlsInterface.getTurn();
        boolean quickTurn = controlsInterface.getQuickTurn();
        boolean shiftDown = controlsInterface.getLowGear();*/


        boolean flop = controlsInterface.toggleFlop();
        boolean pivot = controlsInterface.togglePivot();

        /*DrivePower power = TeleopDriveController.curvatureDrive(throttle, turn, quickTurn);
            driveTrain.setOpenLoop(power);
            driveTrain.setHighGear(!shiftDown);*/
        if (controlsInterface.unjamIntake()){
                //intake.setIntake(0.5, 0.5);
                intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        }
        else if (controlsInterface.getIntake()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            //intake.setIntake(-Constants.kLeftIntakePower, -Constants.kRightIntakePower);
        }
        else if (controlsInterface.getExhaust()){
            intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
            //intake.setIntake(0.5, 0.5);
        }
        else if(flop && !prevFlop){
            counter++;
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }
        else if(pivot && !prevPivot){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }
        else{
            intake.setWantedState(Intake.WantedState.IDLE);
            //intake.setIntake(0, 0);
        }

        prevFlop = flop;
        prevPivot = pivot;

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

        System.out.println("CURRENT STATE: " + intake.getCurrentState());
        System.out.println("WANTED STATE: " + intake.getWantedState());
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
