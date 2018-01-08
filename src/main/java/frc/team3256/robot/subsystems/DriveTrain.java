package frc.team3256.robot.subsystems;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    public enum DriveControlMode {
        OPEN_LOOP,
        VELOCITY,
        TURN_TO_ANGLE,
        DRIVE_TO_DISTANCE
    }

    @Override
    public void init(double timestamp) {
        setOpenLoop(0, 0);
        leftMaster.setPosition(0);
        rightMaster.setPosition(0);
        gyro.reset();
    }

    @Override
    public void update(double timestamp) {
        System.out.println(getMode());
        switch (controlMode) {
            case OPEN_LOOP:
                //Fall through, all driver/manipulator input is handled in TeleopPeriodic
                break;
            case VELOCITY:
                double left = ControlsInterface.getTankLeft();
                double right = ControlsInterface.getTankRight();
                DrivePower power = TeleopDriveController.tankDrive(left, right);
                //System.out.println("MOTOR OUTPUT" + leftMaster.getOutputVoltage()/leftMaster.getBusVoltage());
                //System.out.println("MOTOR SPEED " + leftMaster.getSpeed());
                System.out.println("LEFT ERROR: " + getLeftVelocityError());
                System.out.println("RIGHT ERROR" + getRightVelocityError());
                SmartDashboard.putNumber("LEFT ERROR", getLeftVelocityError());
                SmartDashboard.putNumber("RIGHT ERROR", getRightVelocityError());
                updateVelocitySetpoint(power.getLeft()*Constants.kMaxVelocityHighGearInPerSec
                                , power.getRight()*Constants.kMaxVelocityHighGearInPerSec);
                break;
            /*
            case TURN_TO_ANGLE:
                updateTurnToAngle();
                break;
            case DRIVE_TO_DISTANCE:
                updateDriveToDistance();
                break;
            */
}
    }

    @Override
    public void end(double timestamp) {
        setOpenLoop(0, 0);
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

        /*
        We haven't tested this yet
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
        */

        leftMaster.setPID(Constants.kLeftDriveVelocityP, Constants.kLeftDriveVelocityI, Constants.kLeftDriveVelocityD,
                Constants.kLeftDriveVelocityF, Constants.kLeftDriveVelocityIZone, Constants.kLeftDriveVelocityCloseLoopRampRate,
                Constants.kDriveVelocityProfile);
        rightMaster.setPID(Constants.kRightDriveVelocityP, Constants.kRightDriveVelocityI, Constants.kRightDriveVelocityD,
                Constants.kRightDriveVelocityF, Constants.kRightDriveVelocityIZone, Constants.kRightDriveVelocityCloseLoopRampRate,
                Constants.kDriveVelocityProfile);

        leftMaster.reverseSensor(true);
        rightMaster.reverseSensor(true);
    }

    public double getLeftDistance() {
        return rotToInches(leftMaster.getPosition());
    }

    public double getRightDistance() {
        return rotToInches(rightMaster.getPosition());
    }

    /**
     * @return left velocity in in/sec
     */
    public double getLeftVelocity(){
        return rpmToInchesPerSec(leftMaster.getSpeed());
    }

    /**
     * @return right velocity in in/sec
     */
    public double getRightVelocity(){
        return rpmToInchesPerSec(rightMaster.getSpeed());
    }

    //TODO: test these methods
    public double getLeftVelocityError(){
        if (controlMode!=DriveControlMode.VELOCITY){
            System.out.println("ERROR: WE ARE NOT IN VELOCITY CONTROL MODE");
            return 0;
        }
        //Returns in in/sec
        return rpmToInchesPerSec(leftMaster.getSetpoint())-getLeftVelocity();
    }

    public double getRightVelocityError(){
        if (controlMode != DriveControlMode.VELOCITY){
            System.out.println("ERROR: WE ARE NOT IN VELOCITY CONTROL MODE");
            return 0;
        }
        //Returns in in/sec
        return rpmToInchesPerSec(rightMaster.getSetpoint()-getRightVelocity());
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
        leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    }

    public void configureTalonsForVelocity() {
        leftMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
        leftMaster.setProfile(Constants.kDriveVelocityProfile);
        rightMaster.changeControlMode(CANTalon.TalonControlMode.Speed);
        rightMaster.setProfile(Constants.kDriveVelocityProfile);
    }

    /*
    We haven't started testing these yet
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
    */

    /**
     * Changes the mode into Velocity mode, and sets the setpoints for the talons
     * @param setpoint_left left velocity setpoint in inches/sec
     * @param setpoint_right right velocity setpoint in inches/sec
     */
    public void setVelocitySetpoint(double setpoint_left, double setpoint_right){
        if (controlMode != DriveControlMode.VELOCITY){
            configureTalonsForVelocity();
            controlMode = DriveControlMode.VELOCITY;
        }
        updateVelocitySetpoint(setpoint_left, setpoint_right);
    }

    /**
     * Updates the Talons with a new velocity setpoint
     * @param left_velocity left velocity setpoint in inches/sec
     * @param right_velocity right velocity setpoint in inches/sec
     */
    public void updateVelocitySetpoint(double left_velocity, double right_velocity){
        //if we aren't in the velocity control mode, then something is messed up, so set motors to 0 for safety
        if (controlMode != DriveControlMode.VELOCITY){
            System.out.println("ERROR: We should be in the velocity mode");
            leftMaster.set(0);
            rightMaster.set(0);
            return;
        }
        if (Math.abs(left_velocity) <= 24){
            left_velocity = 0;
        }
        else if (Math.abs(right_velocity) <= 24){
            right_velocity = 0;
        }
        //otherwise, update the talons with the new velocity setpoint
        System.out.println(inchesPerSecToRpm(right_velocity)+" : "+rightMaster.getSpeed());
        leftMaster.set(inchesPerSecToRpm(left_velocity));
        rightMaster.set(inchesPerSecToRpm(right_velocity));
    }

    /*
    We haven't started testing these yet
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
    */

    /**
     * Set the mode into open-loop mode (PercentVBus on the talons, and set the corresponding power
     * @param leftPower
     * @param rightPower
     */
    public void setOpenLoop(double leftPower, double rightPower) {
        if (controlMode != DriveControlMode.OPEN_LOOP){
            configureTalonsForOpenLoop();
            controlMode = DriveControlMode.OPEN_LOOP;
        }
        //TALON reverseOutput doesn't work in PercentVBus (open loop)
        leftMaster.set(-1.0*leftPower);
        rightMaster.set(rightPower);
    }

    public void setOpenLoop(DrivePower power){
        setOpenLoop(power.getLeft(), power.getRight());
    }

    /*
    We haven't started testing these yet
    public void updateTurnToAngle() {
        //TODO: Take in gyro heading and set a new setpoint based on the gyro heading. This accounts for wheels slipping.
        leftMaster.set(degreesToInches(angle));
        rightMaster.set(degreesToInches(angle));
    }

    public void updateDriveToDistance() {
        //TODO: Take in gyro heading and adjust the left and right talon setpoints to make sure we are driving straight
        leftMaster.set(distance);
        rightMaster.set(distance);
    }
    */

    public ADXRS453_Gyro getGyro(){
        return gyro;
    }

    public double degreesToInches(double degrees) {
        return Constants.ROBOT_TRACK * Math.PI * degrees / 360;
    }

    public DriveControlMode getMode(){
        return controlMode;
    }
}
