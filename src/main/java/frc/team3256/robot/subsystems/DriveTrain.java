package frc.team3256.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Kinematics;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.ADXRS453_Gyro;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Twist;
import frc.team3256.lib.path.Path;
import frc.team3256.lib.path.PurePursuitTracker;
import frc.team3256.lib.trajectory.*;
import frc.team3256.robot.Constants;
import frc.team3256.robot.PoseEstimator;
import frc.team3256.robot.operation.LogitechButtonBoardConfig;

public class DriveTrain extends SubsystemBase implements Loop {

    private static DriveTrain instance;
    private TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;
    private ADXRS453_Gyro gyro;
    private DoubleSolenoid shifter;

    private DriveControlMode controlMode;
    private double m_turnInPlaceDegrees, m_arcTargetAngle;
    private DriveStraightController driveStraightController = new DriveStraightController();
    private DriveArcController driveArcController = new DriveArcController();
    private PurePursuitTracker purePursuitTracker = new PurePursuitTracker();

    public static DriveTrain getInstance() {
        return instance == null ? instance = new DriveTrain() : instance;
    }

    @Override
    public void outputToDashboard() {
        SmartDashboard.putNumber("leftDistance", getLeftDistance());
        SmartDashboard.putNumber("rightDistance", getRightDistance());
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
        DRIVE_STRAIGHT,
        DRIVE_ARC,
        TURN_TO_ANGLE,
        PURE_PURSUIT
    }

    @Override
    public void init(double timestamp) {
        setOpenLoop(0, 0);
        leftMaster.setSelectedSensorPosition(0, 0,0);
        rightMaster.setSelectedSensorPosition(0, 0,0);
        gyro.reset();
        setHighGear(true);
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
                double power = new LogitechButtonBoardConfig().getThrottle();
                if (Math.abs(power) <= 0.2){power = 0;}
                power*=12.0*12.0;
                SmartDashboard.putNumber("REQUESTED VEL", power);
                SmartDashboard.putNumber("ACTUAL VEL", getAverageVelocity());
                SmartDashboard.putNumber("Vel ERROR", (getLeftVelocityError()+getRightVelocityError())/2);
                updateVelocitySetpoint(power, power);
                break;
            case DRIVE_STRAIGHT:
                updateDriveStraight();
                break;
            case DRIVE_ARC:
                updateDriveArc();
                break;
            case TURN_TO_ANGLE:
                updateTurnInPlace();
                break;
            case PURE_PURSUIT:
                updatePurePursuit();
                break;
        }
    }

    @Override
    public void end(double timestamp) {
        setOpenLoop(0, 0);
    }

    private DriveTrain() {
        controlMode = DriveControlMode.OPEN_LOOP;
        // create talon objects
        leftMaster = TalonUtil.generateGenericTalon(Constants.kLeftDriveMaster);
        leftSlave = TalonUtil.generateSlaveTalon(Constants.kLeftDriveSlave, Constants.kLeftDriveMaster);
        rightMaster = TalonUtil.generateGenericTalon(Constants.kRightDriveMaster);
        rightSlave = TalonUtil.generateSlaveTalon(Constants.kRightDriveSlave, Constants.kRightDriveMaster);

        leftMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, (int)(1000*Constants.kControlLoopPeriod), 0);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, (int)(1000*Constants.kControlLoopPeriod), 0);

        leftMaster.setStatusFramePeriod(StatusFrame.Status_1_General, (int)(1000*Constants.kControlLoopPeriod), 0);
        rightMaster.setStatusFramePeriod(StatusFrame.Status_1_General, (int)(1000*Constants.kControlLoopPeriod), 0);

        // setup encoders
        TalonUtil.configMagEncoder(leftMaster);
        TalonUtil.configMagEncoder(rightMaster);
        //gyroscope
        gyro = new ADXRS453_Gyro();

        //shifter
        shifter = new DoubleSolenoid(Constants.kShifterForward, Constants.kShifterReverse);

        //load gains
        TalonUtil.setPIDGains(leftMaster, Constants.kDriveVelocityProfile, Constants.kDriveVelocityP,
                Constants.kDriveVelocityI, Constants.kDriveVelocityD, Constants.kDriveVelocityF);
        TalonUtil.setPIDGains(rightMaster, Constants.kDriveVelocityProfile, Constants.kDriveVelocityP,
                Constants.kDriveVelocityI, Constants.kDriveVelocityD, Constants.kDriveVelocityF);



        TalonUtil.setPIDGains(leftMaster, Constants.kTurnMotionMagicProfile, Constants.kTurnLowGearMotionMagicP,
                Constants.kTurnLowGearMotionMagicI, Constants.kTurnLowGearMotionMagicD, Constants.kTurnLowGearMotionMagicF);
        TalonUtil.setPIDGains(rightMaster, Constants.kTurnMotionMagicProfile, Constants.kTurnLowGearMotionMagicP,
                Constants.kTurnLowGearMotionMagicI, Constants.kTurnLowGearMotionMagicD, Constants.kTurnLowGearMotionMagicF);

        leftMaster.configMotionCruiseVelocity((int)inchesPerSecToSensorUnits(Constants.kTurnLowGearMotionMagicCruiseVelocity),
            0);
        leftMaster.configMotionAcceleration((int)inchesPerSec2ToSensorUnits(Constants.kTurnLowGearMotionMagicAcceleration)
            ,0);
        rightMaster.configMotionCruiseVelocity((int)inchesPerSecToSensorUnits(Constants.kTurnLowGearMotionMagicCruiseVelocity),
            0);
        rightMaster.configMotionAcceleration((int)inchesPerSec2ToSensorUnits(Constants.kTurnLowGearMotionMagicAcceleration),
            0);


        leftMaster.setSensorPhase(false);
        rightMaster.setSensorPhase(false);

        leftMaster.setInverted(true);
        leftSlave.setInverted(true);
        rightMaster.setInverted(false);
        rightSlave.setInverted(false);

        //drive straight and drive arc trajectory following controllers
        driveStraightController.setGains(Constants.kDistanceTrajectoryP, Constants.kDistanceTrajectoryI, Constants.kDistanceTrajectoryD, Constants.kDistanceTrajectoryV, Constants.kDistanceTrajectoryA, Constants.kStraightP, Constants.kStraightI, Constants.kStraightD);
        driveStraightController.setLoopTime(Constants.kControlLoopPeriod);

        driveArcController.setGains(Constants.kCurveTrajectoryP, Constants.kCurveTrajectoryI, Constants.kCurveTrajectoryD, Constants.kCurveTrajectoryV, Constants.kCurveTrajectoryA, Constants.kCurveP, Constants.kCurveI, Constants.kCurveD);
        driveArcController.setLoopTime(Constants.kControlLoopPeriod);

        purePursuitTracker.setLoopTime(Constants.kControlLoopPeriod);
        purePursuitTracker.setPathCompletionTolerance(Constants.kPathCompletionTolerance);
        purePursuitTracker.setLookaheadDistance(Constants.kLookaheadDistance);
    }

    public double getLeftOutputVoltage() {
        return leftMaster.getMotorOutputVoltage();
    }

    public double getRightOutputVoltage() {
        return rightMaster.getMotorOutputVoltage();
    }

    public double getLeftDistance() {
        return sensorUnitsToInches(leftMaster.getSelectedSensorPosition(0));
    }

    public double getRightDistance() {
        return sensorUnitsToInches(rightMaster.getSelectedSensorPosition(0));
    }

    public double getAverageDistance(){
        return (getLeftDistance() + getRightDistance())/2.0;
    }

    /**
     * @return left velocity in in/sec
     */
    public double getLeftVelocity(){
        //return leftMaster.getSelectedSensorVelocity(0);
        return sensorUnitsToInchesPerSec(leftMaster.getSelectedSensorVelocity(0));
    }

    /**
     * @return right velocity in in/sec
     */
    public double getRightVelocity(){
        //return rightMaster.getSelectedSensorVelocity(0);
        return sensorUnitsToInchesPerSec(rightMaster.getSelectedSensorVelocity(0));
    }

    public double getAverageVelocity(){
        return (getLeftVelocity() + getRightVelocity())/2.0;
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
            setHighGear(true);
            leftMaster.enableVoltageCompensation(true);
            rightMaster.enableVoltageCompensation(true);
            leftSlave.enableVoltageCompensation(true);
            rightSlave.enableVoltageCompensation(true);
            leftMaster.selectProfileSlot(Constants.kDriveVelocityProfile,0);
            rightMaster.selectProfileSlot(Constants.kDriveVelocityProfile, 0);
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
        leftMaster.set(ControlMode.Velocity, inchesPerSecToSensorUnits(left_velocity), Constants.kDriveVelocityProfile);
        rightMaster.set(ControlMode.Velocity, inchesPerSecToSensorUnits(right_velocity), Constants.kDriveVelocityProfile);
    }


    /**
     * Set the mode into open-loop mode (PercentVBus on the talons, and set the corresponding power
     * @param leftPower
     * @param rightPower
     */
    public void setOpenLoop(double leftPower, double rightPower) {

        if (controlMode != DriveControlMode.OPEN_LOOP){
            leftMaster.enableVoltageCompensation(false);
            rightMaster.enableVoltageCompensation(false);
            leftSlave.enableVoltageCompensation(false);
            rightSlave.enableVoltageCompensation(false);
            TalonUtil.setBrakeMode(leftMaster, leftSlave, rightMaster, rightSlave);
            controlMode = DriveControlMode.OPEN_LOOP;
            resetNominal();
            enableRamp();
        }
        leftMaster.set(ControlMode.PercentOutput, leftPower);
        rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void enableRamp(){
        leftMaster.configOpenloopRamp(Constants.kRampRate, 0);
        rightMaster.configOpenloopRamp(Constants.kRampRate, 0);
    }
    
    public void disableRamp(){
        leftMaster.configOpenloopRamp(0,0);
        rightMaster.configOpenloopRamp(0,0);
    }

    public void setBrake(){
        TalonUtil.setBrakeMode(leftMaster, leftSlave, rightMaster, rightSlave);
        leftMaster.set(ControlMode.PercentOutput, 0);
        rightMaster.set(ControlMode.PercentOutput, 0);
    }

    public void setOpenLoop(DrivePower power){
        setOpenLoop(power.getLeft(), power.getRight());
    }

    //updates turning in place
    private void updateTurnInPlace() {
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){
            return;
        }
        Rotation robotToTarget = getAngle().inverse().rotate(Rotation.fromDegrees(m_turnInPlaceDegrees));
        System.out.println("ROBOT TO TARGET: " + robotToTarget);
        System.out.println("ANGLE: " + getCubeOffsetAngle());

        if (isTurnInPlaceFinished()){
            return;
        }
        //periodically re-converts the gyro angle into a new motion magic goal for the talons
        Kinematics.DriveVelocity delta = Kinematics.inverseKinematics(new Twist(0,0,robotToTarget.radians()), Constants.kRobotTrack, Constants.kScrubFactor);


        //System.out.println("DL: " + delta.left + "; " + "DR: " + delta.right);
        leftMaster.set(ControlMode.MotionMagic, inchesToSensorUnits(getLeftDistance() + delta.left), 0);
        rightMaster.set(ControlMode.MotionMagic, inchesToSensorUnits(getRightDistance() + delta.right), 0);

        System.out.println(delta.left + "\t" + delta.right);
        System.out.println("Left CLE: " + sensorUnitsToInches(leftMaster.getClosedLoopError(0)));
        System.out.println("Right CLE: " + sensorUnitsToInches(rightMaster.getClosedLoopError(0)));
        System.out.println("LEFT TARGET: " + leftMaster.getClosedLoopTarget(0));
        System.out.println("RIGHT TARGET: " + rightMaster.getClosedLoopTarget(0));
        System.out.println("LEFT VOLTAGE: " + leftMaster.getMotorOutputVoltage());
        System.out.println("RIGHT VOLTAGE: " + rightMaster.getMotorOutputVoltage());
    }

    public void updateDriveStraight() {
        if (controlMode != DriveControlMode.DRIVE_STRAIGHT) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }
        if (driveStraightController.isFinished()) {
            System.out.println("----------------------");
            setOpenLoop(0, 0);
            return;
        }
        //System.out.println("Updating....");
        DrivePower output = driveStraightController.update(getAverageDistance(), getAngle().degrees());
        leftMaster.set(ControlMode.PercentOutput, output.getLeft());
        rightMaster.set(ControlMode.PercentOutput, output.getRight());
    }

    private void updatePurePursuit() {
        if (controlMode != DriveControlMode.PURE_PURSUIT) {
            System.out.println("ERROR, WE ARE NOT IN PURE PURSUIT MODE");
            return;
        }
        if (purePursuitTracker.isFinished()) {
            setOpenLoop(0, 0);
            return;
        }Kinematics.DriveVelocity output = Kinematics.inverseKinematics(purePursuitTracker.update(PoseEstimator.getInstance().getPose()), Constants.kRobotTrack, Constants.kScrubFactor);
        System.out.println(output.left + "\t" + output.right);
        leftMaster.set(ControlMode.Velocity, inchesPerSecToSensorUnits(output.left));
        rightMaster.set(ControlMode.Velocity, inchesPerSecToSensorUnits(output.right));
    }

    public void setHighGear(boolean highGear) {
        shifter.set(highGear ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
    }

    public void updateDriveArc() {
        DrivePower output;
        if (controlMode != DriveControlMode.DRIVE_ARC) {
            return;
        }
        if (driveArcController.isFinished()){
            setOpenLoop(0,0);
            return;
        }

        if (m_arcTargetAngle >= 0) {
            output = driveArcController.updateCalculations(getRightDistance(), getLeftDistance(), getAngle().degrees(), getAverageVelocity());
            leftMaster.set(ControlMode.PercentOutput, output.getRight());
            rightMaster.set(ControlMode.PercentOutput, output.getLeft());
        }
        else {
            output = driveArcController.updateCalculations(getLeftDistance(), getRightDistance(), getAngle().degrees(), getAverageVelocity());
            SmartDashboard.putNumber("Left Output", output.getLeft());
            SmartDashboard.putNumber("Right Output", output.getRight());
            leftMaster.set(ControlMode.PercentOutput, output.getLeft());
            rightMaster.set(ControlMode.PercentOutput, output.getRight());
        }
    }

    public void configureDriveArc(double startVel, double endVel, double degrees, double turnRadius, boolean backwardsTurn, double currAngle) {
        m_arcTargetAngle = degrees;
        driveArcController.configureArcTrajectory(startVel, endVel, degrees, turnRadius, backwardsTurn, currAngle);
        if (controlMode != DriveControlMode.DRIVE_ARC){
            setHighGear(true);
            leftMaster.configNominalOutputForward(2.0/12.0, 0);
            rightMaster.configNominalOutputForward(2.0/12.0, 0);
            leftSlave.configNominalOutputForward(2.0/12.0, 0);
            rightSlave.configNominalOutputForward(2.0/12.0, 0);
            TalonUtil.setBrakeMode(leftMaster, leftSlave, rightMaster, rightSlave);
            controlMode = DriveControlMode.DRIVE_ARC;
        }
        disableRamp();
        updateDriveArc();
    }

    public void configureDriveStraight(double startVel, double endVel, double distance, double angle){
        driveStraightController.setSetpoint(startVel, endVel, distance, angle);
        if (controlMode != DriveControlMode.DRIVE_STRAIGHT){
            controlMode = DriveControlMode.DRIVE_STRAIGHT;
            TalonUtil.setBrakeMode(leftMaster, leftSlave, rightMaster, rightSlave);
            resetNominal();
            setHighGear(true);
        }
        TalonUtil.setBrakeMode(leftMaster, leftSlave, rightMaster, rightSlave);
        disableRamp();
        updateDriveStraight();
    }

    public void configurePurePursuit(Path path) {
        if (controlMode != DriveControlMode.PURE_PURSUIT){
            leftMaster.selectProfileSlot(Constants.kDriveVelocityProfile, 0);
            rightMaster.selectProfileSlot(Constants.kDriveVelocityProfile, 0);
            controlMode = DriveControlMode.PURE_PURSUIT;
        }
        setHighGear(true);
        purePursuitTracker.setPath(path);
        disableRamp();
        updatePurePursuit();
    }

    public void resetDriveStraightController() {
        driveStraightController.resetController();
    }

    public void resetDriveArcController() {
        driveArcController.resetTrajectory();
    }

    public void resetPurePursuit() {
        purePursuitTracker.reset();
    }

    public void resetEncoders() {
        leftMaster.setSelectedSensorPosition(0, 0, 0);
        rightMaster.setSelectedSensorPosition(0, 0,0);
    }

    public boolean isTurnInPlaceFinished() {
        double error = Math.abs(m_turnInPlaceDegrees - getAngle().degrees());
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){
            return true;
        }
        if (error <= 1.0){
            return true;
        }
        return false;
        //return sensorUnitsToInches(leftMaster.getClosedLoopError(0)) < 0.5 && sensorUnitsToInches(rightMaster.getClosedLoopError(0)) < 0.5;
        //return Math.abs(degrees + startAngle) < Math.abs(gyro.getAngle());
    }

    public boolean isDriveStraightFinished() {
        return driveStraightController.isFinished();
    }

    public boolean isArcControllerFinished() {
        return driveArcController.isFinished() || Math.abs(getAngle().degrees() - m_arcTargetAngle) < 1.0;
    }

    public boolean isPurePursuitFinished() {
        return purePursuitTracker.isFinished();
    }

    public void setTurnInPlaceSetpoint(double setpoint) {
        resetGyro();
        this.m_turnInPlaceDegrees = setpoint;
        setHighGear(false);
        rightMaster.selectProfileSlot(Constants.kTurnMotionMagicProfile, 0);
        leftMaster.selectProfileSlot(Constants.kTurnMotionMagicProfile, 0);
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){

            leftMaster.configNominalOutputForward(2.0/12.0, 0);
            rightMaster.configNominalOutputForward(2.0/12.0, 0);
            leftSlave.configNominalOutputForward(2.0/12.0, 0);
            rightSlave.configNominalOutputForward(2.0/12.0, 0);
            controlMode = DriveControlMode.TURN_TO_ANGLE;
        }

        updateTurnInPlace();
    }

    public void setAbsoluteTurnInPlaceSetpoint(double setpoint) {
        this.m_turnInPlaceDegrees = getAngle().degrees() + setpoint;
        setHighGear(false);
        rightMaster.selectProfileSlot(Constants.kTurnMotionMagicProfile, 0);
        leftMaster.selectProfileSlot(Constants.kTurnMotionMagicProfile, 0);
        if (controlMode != DriveControlMode.TURN_TO_ANGLE){

            leftMaster.configNominalOutputForward(2.0/12.0, 0);
            rightMaster.configNominalOutputForward(2.0/12.0, 0);
            leftSlave.configNominalOutputForward(2.0/12.0, 0);
            rightSlave.configNominalOutputForward(2.0/12.0, 0);
            controlMode = DriveControlMode.TURN_TO_ANGLE;
        }

        updateTurnInPlace();
    }

    public double inchesToSensorUnits(double inches) {
        return (inches * 4096.0)/(Constants.kWheelDiameter * Math.PI) * Constants.kDriveEncoderScalingFactor;
    }

    //Sensor units for velocity are encoder units per 100 ms
    public double inchesPerSecToSensorUnits(double inchesPerSec){
        return inchesToSensorUnits(inchesPerSec)/10.0;
    }

    public double inchesPerSec2ToSensorUnits(double inchesPerSec2){
      return inchesPerSecToSensorUnits(inchesPerSec2)/10.0;
    }

    public double sensorUnitsToInches(double sensorUnits){
        return (sensorUnits*Math.PI*Constants.kWheelDiameter)/4096/Constants.kDriveEncoderScalingFactor;
    }

    public double sensorUnitsToInchesPerSec(double sensorUnits){
        return sensorUnitsToInches(sensorUnits*10.0);
    }

    public ADXRS453_Gyro getGyro(){
        return gyro;
    }

    public Rotation getAngle(){
        //Return negative value of the gyro, because the gyro returns
        return Rotation.fromDegrees(-gyro.getAngle());
    }

    public DriveControlMode getMode(){
        return controlMode;
    }

    public void setCoastMode(){
        leftMaster.setNeutralMode(NeutralMode.Coast);
        rightMaster.setNeutralMode(NeutralMode.Coast);
        leftSlave.setNeutralMode(NeutralMode.Coast);
        rightSlave.setNeutralMode(NeutralMode.Coast);
    }

    public void resetGyro(){
        gyro.reset();
    }

    public void resetNominal(){
        leftMaster.configNominalOutputForward(0, 0);
        rightMaster.configNominalOutputForward(0, 0);
        leftSlave.configNominalOutputForward(0, 0);
        rightSlave.configNominalOutputForward(0, 0);
    }

    public double getCubeOffsetAngle() {
        return SmartDashboard.getNumber("cube_angle", 0.0);
    }
}
