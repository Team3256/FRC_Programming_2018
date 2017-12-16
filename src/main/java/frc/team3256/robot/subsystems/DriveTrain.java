package frc.team3256.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Loop;
import frc.team3256.lib.control.PIDController;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.lib.hardware.ADXRS453_Gyro;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;
import frc.team3256.robot.ControlsInterface;

public class DriveTrain implements Loop {

    private static DriveTrain instance;
    private CANTalon leftMaster, rightMaster, leftSlave, rightSlave;
    private ADXRS453_Gyro gyro;
    private DriveControlMode controlMode;

    private double angle, distance;

    public static DriveTrain getInstance() {
        return instance == null ? instance = new DriveTrain() : instance;
    }
    PIDController pidController = new PIDController();

    public enum DriveControlMode {
        OPEN_LOOP,
        VELOCITY,
        TURN_TO_ANGLE,
        DRIVE_TO_DISTANCE
    }

    @Override
    public void init(double timestamp) {
        leftMaster.setPosition(0);
        rightMaster.setPosition(0);
        gyro.reset();
    }

    @Override
    public void update(double timestamp) {
        switch (controlMode) {
            case OPEN_LOOP:
                break;
            case VELOCITY:
                double left = ControlsInterface.getTankLeft();
                double right = ControlsInterface.getTankRight();
                DrivePower power = TeleopDriveController.tankDrive(left, right);
                System.out.println("MOTOR OUTPUT" + leftMaster.getOutputVoltage()/leftMaster.getBusVoltage());
                System.out.println("MOTOR SPEED " + leftMaster.getSpeed());
                System.out.println("Error:" + pidController.getError());
                setVelocitySetpoint(power.getLeft() * 175.0, power.getRight() * 175.0);
                break;
            case TURN_TO_ANGLE:
                updateTurnToAngle();
                break;
            case DRIVE_TO_DISTANCE:
    updateDriveToDistance();
                break;
}
    }

    @Override
    public void end(double timestamp) {

    }

    private DriveTrain() {
        controlMode = DriveControlMode.OPEN_LOOP;
        // create talon objects
        // master talons are set to a 5 period control frame rate
        leftMaster = TalonUtil.generateGenericTalon(Constants.kLeftDriveMaster);
        leftSlave = TalonUtil.generateSlaveTalon(Constants.kLeftDriveSlave, Constants.kLeftDriveMaster);
        rightMaster = TalonUtil.generateGenericTalon(Constants.kRightDriveMaster);
        rightSlave = TalonUtil.generateSlaveTalon(Constants.kRightDriveSlave, Constants.kRightDriveMaster);

        // setup encoders
        leftMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        if (!TalonUtil.magEncConnected(leftMaster)){
            DriverStation.reportError("Mag Encoder on Left Master not detected!!!", false);
        }
        rightMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        if (!TalonUtil.magEncConnected(rightMaster)){
            DriverStation.reportError("Mag Encoder on Right Master not detected!!!", false);
        }

        gyro = new ADXRS453_Gyro();

        leftMaster.setPID(Constants.kDriveMotionMagicP, Constants.kDriveMotionMagicI, Constants.kDriveMotionMagicD,
                Constants.kDriveMotionMagicF, Constants.kDriveMotionMagicIZone, Constants.kDriveMotionMagicCloseLoopRampRate,
                Constants.kDriveMotionMagicProfile);
        rightMaster.setPID(Constants.kDriveMotionMagicP, Constants.kDriveMotionMagicI, Constants.kDriveMotionMagicD,
                Constants.kDriveMotionMagicF, Constants.kDriveMotionMagicIZone, Constants.kDriveMotionMagicCloseLoopRampRate,
                Constants.kDriveMotionMagicProfile);
        leftMaster.setMotionMagicAcceleration(Constants.kDriveMotionMagicAcceleration);
        rightMaster.setMotionMagicAcceleration((Constants.kDriveMotionMagicAcceleration));
        leftMaster.setMotionMagicCruiseVelocity(Constants.kDriveMotionMagicCruiseVelocity);
        rightMaster.setMotionMagicCruiseVelocity(Constants.kDriveMotionMagicCruiseVelocity);

        leftMaster.setPID(Constants.kDriveVelocityP, Constants.kDriveVelocityI, Constants.kDriveVelocityD,
                Constants.kDriveVelocityF, Constants.kDriveVelocityIZone, Constants.kDriveVelocityCloseLoopRampRate,
                Constants.kDriveVelocityProfile);
        rightMaster.setPID(Constants.kDriveVelocityP, Constants.kDriveVelocityI, Constants.kDriveVelocityD,
                Constants.kDriveVelocityF, Constants.kDriveVelocityIZone, Constants.kDriveVelocityCloseLoopRampRate,
                Constants.kDriveVelocityProfile);

        leftMaster.reverseSensor(true);
    }

    public double getLeftDistance() {
        return rotToInches(leftMaster.getPosition());
    }

    public double getRightDistance() {
        return rotToInches(rightMaster.getPosition());
    }

    public double getLeftVelocity(){
        return rpmToInchesPerSec(leftMaster.getSpeed());
    }

    public double getRightVelocity(){
        return rpmToInchesPerSec(rightMaster.getSpeed());
    }

    public double rotToInches(double rot){
        return rot*4*Math.PI;
    }

    public double inchesToRot(double inches){
        return inches / (4*Math.PI);
    }

    public double rpmToInchesPerSec(double rpm){
        return rotToInches(rpm)/60;
    }

    public double inchesPerSecToRpm(double inchesPerSec){
        return inchesToRot(inchesPerSec)*60;
    }

    public double rpmToNativeUnitsPerHundredMs(double rpm){
        return (rpm*4096)/600;
    }



    public void configureTalonsForOpenLoop(){
        if (controlMode != DriveControlMode.OPEN_LOOP){
            controlMode = DriveControlMode.OPEN_LOOP;
            leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
            rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        }
    }

    public void configureTalonsForVelocity() {
        if (controlMode != DriveControlMode.VELOCITY){
            controlMode = DriveControlMode.VELOCITY;
            leftMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
            leftMaster.setProfile(Constants.kDriveVelocityProfile);
            rightMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
            rightMaster.setProfile(Constants.kDriveVelocityProfile);
        }
    }

    public void configureTalonsForDistance() {
        if (controlMode != DriveControlMode.DRIVE_TO_DISTANCE)
            controlMode = DriveControlMode.DRIVE_TO_DISTANCE;
        leftMaster.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
        rightMaster.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
    }

    public void configureTalonsForAngle() {
        if (controlMode != DriveControlMode.TURN_TO_ANGLE)
            controlMode = DriveControlMode.TURN_TO_ANGLE;
        leftMaster.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
        rightMaster.changeControlMode(CANTalon.TalonControlMode.MotionMagic);
    }

    public void setVelocitySetpoint(double setpoint_left, double setpoint_right){
        configureTalonsForVelocity();
        leftMaster.set(inchesPerSecToRpm(setpoint_left));
        rightMaster.set(inchesPerSecToRpm(setpoint_right));
    }

    public double getLeftSetpoint(){
        return leftMaster.getSetpoint();
    }

    public double getLeftClosedLoopError(){
        return leftMaster.getClosedLoopError();
    }

    public void setAngleSetpoint(double angle) {
        configureTalonsForAngle();
        leftMaster.set(degreesToInches(angle));
        rightMaster.set(degreesToInches(angle));
        this.angle = angle;
    }

    public void setDistanceSetpoint(double distance) {
        configureTalonsForDistance();
        leftMaster.set(distance);
        rightMaster.set(distance);
        this.distance = distance;
    }

    public void setOpenLoop(double leftPower, double rightPower) {
        configureTalonsForOpenLoop();
        //TALON reverseOutput doesn't work in PercentVBus (open loop)
        leftMaster.set(-1.0*leftPower);
        rightMaster.set(rightPower);
    }

    public void setOpenLoop(DrivePower power){
        setOpenLoop(power.getLeft(), power.getRight());
    }

    public void updateTurnToAngle() {
        leftMaster.set(degreesToInches(angle));
        rightMaster.set(degreesToInches(angle));
    }

    public void updateDriveToDistance() {
        leftMaster.set(distance);
        rightMaster.set(distance);
    }

    public ADXRS453_Gyro getGyro(){
        return gyro;
    }

    public static double degreesToInches(double degrees) {
        return Constants.ROBOT_TRACK * Math.PI * degrees / 360;
    }

    public DriveControlMode getMode(){
        return controlMode;
    }
}
