package frc.team3256.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Kinematics;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.ADXRS453_Gyro;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Twist;
import frc.team3256.lib.trajectory.*;
import frc.team3256.robot.Constants;

public class DriveTrain extends SubsystemBase implements Loop {

    private static DriveTrain instance;
    private TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
    private ADXRS453_Gyro gyro;
    private DriveControlMode controlMode;
    private double degrees;
    private double distance;
    private TrajectoryDistanceWrapper trajectoryDistanceWrapper = new TrajectoryDistanceWrapper();
    private TrajectoryArcWrapper trajectoryArcWrapper = new TrajectoryArcWrapper();
    private double leftOffset = 0;
    private double rightOffset = 0;

    public static DriveTrain getInstance() {
        return instance == null ? instance = new DriveTrain() : instance;
    }

    @Override
    public void outputToDashboard() {

    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }

    public enum DriveControlMode {
        OPEN_LOOP,
        VELOCITY,
        CUSTOM_DISTANCE_TRAJECTORY,
        TURN_TO_ANGLE,
        DRIVE_TO_DISTANCE,
        CUSTOM_ARC_TRAJECTORY
    }

    @Override
    public void init(double timestamp) {
        setOpenLoop(0, 0);
        leftMaster.setSelectedSensorPosition(0, 0,0);
        rightMaster.setSelectedSensorPosition(0, 0,0);
        gyro.reset();
    }

    @Override
    public void update(double timestamp) {
        //System.out.println(getMode());
        //System.out.println("Dist Traveled: " + (getLeftDistance() + getRightDistance())/2);
        switch (controlMode) {
            case OPEN_LOOP:
                //Fall through, all driver/manipulator input is handled in TeleopPeriodic
                break;
            case VELOCITY:
                /*
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
                */
                break;
            case CUSTOM_DISTANCE_TRAJECTORY:
                updateDistanceTrajectory();
                break;
            case CUSTOM_ARC_TRAJECTORY:
                updateArcTrajectory();
                break;
            case TURN_TO_ANGLE:
                updateTurnInPlace();
                break;
            case DRIVE_TO_DISTANCE:
                updateDistance();
                break;
        }
    }

    @Override
    public void end(double timestamp) {
        setOpenLoop(0, 0);
    }

    public DriveTrain() {
        controlMode = DriveControlMode.OPEN_LOOP;
        // create talon objects
        // master talons are set to a 5 period control frame rate
        leftMaster = TalonUtil.generateGenericTalon(Constants.kLeftDriveMaster);
        leftSlave = TalonUtil.generateSlaveTalon(Constants.kLeftDriveSlave, Constants.kLeftDriveMaster);
        rightMaster = TalonUtil.generateGenericTalon(Constants.kRightDriveMaster);
        rightSlave = TalonUtil.generateSlaveTalon(Constants.kRightDriveSlave, Constants.kRightDriveMaster);

        // setup encoders
        if (leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0) != ErrorCode.OK){
            DriverStation.reportError("Mag Encoder on Left Master not detected!!!", false);
        }
        if (rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0) != ErrorCode.OK){
            DriverStation.reportError("Mag Encoder on Right Master not detected!!!", false);
        }
        gyro = new ADXRS453_Gyro();

        leftMaster.config_kP(Constants.kDriveVelocityProfile, Constants.kLeftDriveVelocityP, 0);
        leftMaster.config_kI(Constants.kDriveVelocityProfile, Constants.kLeftDriveVelocityI, 0);
        leftMaster.config_kD(Constants.kDriveVelocityProfile, Constants.kLeftDriveVelocityD, 0);
        leftMaster.config_kF(Constants.kDriveVelocityProfile, Constants.kLeftDriveVelocityF, 0);
        leftMaster.config_IntegralZone(Constants.kDriveVelocityProfile, Constants.kLeftDriveVelocityIZone, 0);
        rightMaster.config_kP(Constants.kDriveVelocityProfile, Constants.kRightDriveVelocityP, 0);
        rightMaster.config_kI(Constants.kDriveVelocityProfile, Constants.kRightDriveVelocityI, 0);
        rightMaster.config_kD(Constants.kDriveVelocityProfile, Constants.kRightDriveVelocityD, 0);
        rightMaster.config_kF(Constants.kDriveVelocityProfile, Constants.kRightDriveVelocityF, 0);
        rightMaster.config_IntegralZone(Constants.kDriveVelocityProfile, Constants.kRightDriveVelocityIZone, 0);

        leftMaster.config_kP(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicP, 0);
        leftMaster.config_kI(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicI, 0);
        leftMaster.config_kD(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicD, 0);
        leftMaster.config_kF(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicF, 0);
        rightMaster.config_kP(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicP, 0);
        rightMaster.config_kI(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicI, 0);
        rightMaster.config_kD(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicD, 0);
        rightMaster.config_kF(Constants.kDriveMotionMagicProfile, Constants.kDriveMotionMagicF, 0);

        leftMaster.configMotionCruiseVelocity((int)inchesPerSecToSensorUnits(Constants.kDriveMotionMagicCruiseVelocity),
            0);
        leftMaster.configMotionAcceleration((int)inchesPerSec2ToSensorUnits(Constants.kDriveMotionMagicAcceleration)
            ,0);
        rightMaster.configMotionCruiseVelocity((int)inchesPerSecToSensorUnits(Constants.kDriveMotionMagicCruiseVelocity),
            0);
        rightMaster.configMotionAcceleration((int)inchesPerSec2ToSensorUnits(Constants.kDriveMotionMagicAcceleration),
            0);

        leftMaster.setSensorPhase(true);
        rightMaster.setSensorPhase(true);

        //leftMaster.setInverted(true);
        //leftSlave.setInverted(true);

        rightMaster.setInverted(true);
        rightSlave.setInverted(true);

        trajectoryDistanceWrapper.setGains(Constants.kPercentOutputP, Constants.kPercentOutputI, Constants.kPercentOutputD, Constants.kPercentOutputV, Constants.kPercentOutputA);
    }

    public double getLeftDistance() {
        return sensorUnitsToInches(leftMaster.getSelectedSensorPosition(0));
    }

    public double getRightDistance() {
        return sensorUnitsToInches(rightMaster.getSelectedSensorPosition(0));
    }

    /**
     * @return left velocity in in/sec
     */
    public double getLeftVelocity(){
        return sensorUnitsToInchesPerSec(leftMaster.getSelectedSensorVelocity(0));
    }

    /**
     * @return right velocity in in/sec
     */
    public double getRightVelocity(){
        return sensorUnitsToInches(rightMaster.getSelectedSensorVelocity(0));
    }

    //TODO: test these methods
    public double getLeftVelocityError(){
        if (controlMode!=DriveControlMode.VELOCITY){
            System.out.println("ERROR: WE ARE NOT IN VELOCITY CONTROL MODE");
            return 0;
        }
        //Returns in in/sec
        return sensorUnitsToInchesPerSec(leftMaster.getClosedLoopError(0));
    }

    public double getRightVelocityError(){
        if (controlMode != DriveControlMode.VELOCITY){
            System.out.println("ERROR: WE ARE NOT IN VELOCITY CONTROL MODE");
            return 0;
        }
        //Returns in in/sec
        return sensorUnitsToInchesPerSec(rightMaster.getClosedLoopError(0));
    }

    /**
     * Changes the mode into Velocity mode, and sets the setpoints for the talons
     * @param setpoint_left left velocity setpoint in inches/sec
     * @param setpoint_right right velocity setpoint in inches/sec
     */
    public void setVelocitySetpoint(double setpoint_left, double setpoint_right){
        if (controlMode != DriveControlMode.VELOCITY){
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
            leftMaster.set(ControlMode.PercentOutput, 0);
            rightMaster.set(ControlMode.PercentOutput, 0);
            return;
        }
        //otherwise, update the talons with the new velocity setpoint
        leftMaster.set(ControlMode.Velocity, inchesPerSecToSensorUnits(left_velocity));
        rightMaster.set(ControlMode.Velocity, inchesPerSecToSensorUnits(right_velocity));
    }


    /**
     * Set the mode into open-loop mode (PercentVBus on the talons, and set the corresponding power
     * @param leftPower
     * @param rightPower
     */
    public void setOpenLoop(double leftPower, double rightPower) {
        if (controlMode != DriveControlMode.OPEN_LOOP){
            controlMode = DriveControlMode.OPEN_LOOP;
        }
        //TALON reverseOutput doesn't work in PercentVBus (open loop)
        leftMaster.set(ControlMode.PercentOutput, leftPower);
        rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void setOpenLoop(DrivePower power){
        setOpenLoop(power.getLeft(), power.getRight());
    }

    //updates turning in place
    private void updateTurnInPlace() {
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){
            return;
        }
        Rotation error = Rotation.fromDegrees(degrees - gyro.getAngle());
        if (isTurnInPlaceFinished()){
            setOpenLoop(0,0);
            return;
        }
        //periodically re-converts the gyro angle into a new motion magic goal for the talons
        Kinematics.DriveVelocity delta = Kinematics.inverseKinematics(new Twist(0,0,error.radians()), Constants.kRobotTrack);
        leftMaster.set(ControlMode.MotionMagic, inchesToSensorUnits(getLeftDistance() + delta.left), 0);
        rightMaster.set(ControlMode.MotionMagic, inchesToSensorUnits(getRightDistance() + delta.right), 0);
    }

    public void updateDistance() {
        if (controlMode != DriveControlMode.DRIVE_TO_DISTANCE){
            return;
        }
        if (isDriveToDistanceFinished()){
            setOpenLoop(0,0);
            return;
        }
        leftMaster.set(ControlMode.MotionMagic, inchesToSensorUnits(distance + leftOffset), 0);
        rightMaster.set(ControlMode.MotionMagic, inchesToSensorUnits(distance + rightOffset), 0);
    }

    public void updateDistanceTrajectory() {
        if (controlMode != DriveControlMode.CUSTOM_DISTANCE_TRAJECTORY) {
            return;
        }
        if (trajectoryDistanceWrapper.isFinished()){
            setOpenLoop(0,0);
            return;
        }
        System.out.println("Updating....");
        leftMaster.set(ControlMode.PercentOutput, trajectoryDistanceWrapper.updateCalculations(getLeftDistance()));
        rightMaster.set(ControlMode.PercentOutput, trajectoryDistanceWrapper.updateCalculations(getRightDistance()));
    }

    public void updateArcTrajectory() {
        if (controlMode != DriveControlMode.CUSTOM_ARC_TRAJECTORY) {
            return;
        }
        if (trajectoryArcWrapper.isFinished()){
            setOpenLoop(0,0);
            return;
        }
        leftMaster.set(ControlMode.PercentOutput, trajectoryArcWrapper.updateCalculations(getLeftDistance()));
        rightMaster.set(ControlMode.PercentOutput, trajectoryArcWrapper.updateCalculations(getRightDistance()));
    }

    public void configureArcTrajectory(double startVel, double endVel, double degrees, double turnRadius) {
        trajectoryArcWrapper.configureArcTrajectory(startVel, endVel, degrees, turnRadius);
        if (controlMode != DriveControlMode.CUSTOM_ARC_TRAJECTORY){
            controlMode = DriveControlMode.CUSTOM_ARC_TRAJECTORY;
        }
    }

    public void configureDistanceTrajectory(double startVel, double endVel, double distance){
        trajectoryDistanceWrapper.configureDistanceTrajectory(startVel, endVel, distance);
        if (controlMode != DriveControlMode.CUSTOM_DISTANCE_TRAJECTORY){
            controlMode = DriveControlMode.CUSTOM_DISTANCE_TRAJECTORY;
        }
    }

    public void resetEncoders() {
        leftMaster.setSelectedSensorPosition(0, 0, 0);
        rightMaster.setSelectedSensorPosition(0, 0,0);
        System.out.println("AFTER RESET: " + getLeftDistance() + " " + getRightDistance());
    }

    public void setOffsets(double leftOffset, double rightOffset) {
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
    }

    public boolean isDriveToDistanceFinished() {
        double distanceTraveled = (getLeftDistance() - leftOffset + getRightDistance() - rightOffset)/2;
        if (controlMode != DriveControlMode.DRIVE_TO_DISTANCE) {
            return true;
        }
        if (Math.abs(distance - distanceTraveled) <= 0.75) {
            return true;
        }
        return false;
    }

    public boolean isTurnInPlaceFinished() {
        double error = Math.abs(degrees - gyro.getAngle());
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){
            return true;
        }
        if (error <= 1.0){
            return true;
        }
        return false;
    }

    public boolean isTrajectoryFinished() {
        return trajectoryDistanceWrapper.isFinished();
    }
    public void setTurnInPlaceSetpoint(double setpoint) {
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){
            controlMode = DriveControlMode.TURN_TO_ANGLE;
        }
        this.degrees = setpoint;
        updateTurnInPlace();
    }

    public void setDriveToDistanceSetpoint(double setpoint) {
        if (controlMode != DriveControlMode.DRIVE_TO_DISTANCE) {
            controlMode = DriveControlMode.DRIVE_TO_DISTANCE;
        }
        this.distance = setpoint;
        updateDistance();
    }


    public double inchesToSensorUnits(double inches) {
        return (inches * 4096)/(Constants.kWheelDiameter * Math.PI);
    }

    //Sensor units for velocity are encoder units per 100 ms
    public double inchesPerSecToSensorUnits(double inchesPerSec){
        return inchesToSensorUnits(inchesPerSec)/10.0;
    }

    public double inchesPerSec2ToSensorUnits(double inchesPerSec2){
      return inchesPerSecToSensorUnits(inchesPerSec2)/10.0;
    }

    public double sensorUnitsToInches(double sensorUnits){
        return sensorUnits/4096*Math.PI*Constants.kWheelDiameter;
    }

    public double sensorUnitsToInchesPerSec(double sensorUnits){
        return sensorUnitsToInches(sensorUnits*10.0);
    }

    public ADXRS453_Gyro getGyro(){
        return gyro;
    }

    public Rotation getAngle(){
        return Rotation.fromDegrees(gyro.getAngle());
    }

    public DriveControlMode getMode(){
        return controlMode;
    }
}
