package frc.team3256.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;

public class Elevator extends SubsystemBase implements Loop{

    private TalonSRX master, slaveOne, slaveTwo, slaveThree;
    private DigitalInput hallEffect;

    private SystemState currentState = SystemState.HOLD;
    private WantedState wantedState = WantedState.WANTS_TO_HOLD;
    private WantedState prevWantedState = WantedState.WANTS_TO_HOLD;

    private boolean isHomed = false;
    private boolean stateChanged;
    private boolean targetReached = false;
    private boolean wantedStateChanged = false;

    private double m_closedLoopTarget;
    private boolean m_usingClosedLoop;

    private double homingTimeStart;

    private static Elevator instance;

    public static Elevator getInstance() {
         return instance == null ? instance = new Elevator() : instance;
    }

    private InterruptHandlerFunction<Elevator> ihr = new InterruptHandlerFunction<Elevator>() {

        @Override
        public void interruptFired(int interruptAssertedMask, Elevator param) {
            if (master.getSelectedSensorVelocity(0) < 0){
                System.out.println("HOMED ON UPPER SIDE OF CROSSBAR!!!!");
                master.setSelectedSensorPosition((int)heightToSensorUnits(Constants.kCompHomeHeight), 0, 0);
                isHomed = true;
                master.configForwardSoftLimitEnable(true, 0);
                master.configReverseSoftLimitEnable(true, 0);
            }
            else if (master.getSelectedSensorVelocity(0) > 0){
                System.out.println("HOMED ON LOWER SIDE OF CROSSBAR!!!!");
                master.setSelectedSensorPosition((int)heightToSensorUnits(Constants.kCompBottomHomeHeight), 0, 0);
                isHomed = true;
                master.configForwardSoftLimitEnable(true, 0);
                master.configReverseSoftLimitEnable(true, 0);
            }
        }
    };

    private Elevator() {
        hallEffect = new DigitalInput(Constants.kHallEffectPort);
        hallEffect.requestInterrupts(ihr);
        hallEffect.setUpSourceEdge(false, true);
        hallEffect.enableInterrupts();

        master = TalonUtil.generateGenericTalon(Constants.kElevatorMaster);
        slaveOne = TalonUtil.generateSlaveTalon(Constants.kElevatorSlaveOne, Constants.kElevatorMaster);
        slaveTwo = TalonUtil.generateSlaveTalon(Constants.kElevatorSlaveTwo, Constants.kElevatorMaster);
        slaveThree = TalonUtil.generateSlaveTalon(Constants.kElevatorSlaveThree, Constants.kElevatorMaster);

        TalonUtil.configMagEncoder(master);
        master.setSelectedSensorPosition((int)(heightToSensorUnits(7.5)), 0, 0);

        //configure Hold PID values
        TalonUtil.setPIDGains(master, Constants.kElevatorHoldSlot, Constants.kElevatorHoldP,
                Constants.kElevatorHoldI, Constants.kElevatorHoldD, 0);

        //configure FastUp PID values
        TalonUtil.setPIDGains(master, Constants.kElevatorMotionMagicUpSlot, Constants.kElevatorMotionMagicUpP,
                Constants.kElevatorMotionMagicUpI, Constants.kElevatorMotionMagicUpD, Constants.kElevatorMotionMagicUpF);

        //configure FastDown PID values
        TalonUtil.setPIDGains(master, Constants.kElevatorMotionMagicDownSlot, Constants.kElevatorMotionMagicDownP,
                Constants.kElevatorMotionMagicDownI, Constants.kElevatorMotionMagicDownD, Constants.kElevatorMotionMagicDownF);

        //voltage limiting
        TalonUtil.setPeakOutput(Constants.kElevatorMaxUpVoltage/12.0,
                Constants.kElevatorMaxDownVoltage/12.0, master, slaveOne, slaveTwo, slaveThree);
        TalonUtil.setMinOutput(Constants.kElevatorMinHoldVoltage/12.0,
                0, master, slaveOne, slaveTwo, slaveThree);

        //soft limits
        master.configForwardSoftLimitThreshold((int)(heightToSensorUnits(Constants.kElevatorMaxHeight)), 0);
        master.configReverseSoftLimitThreshold((int)(heightToSensorUnits(Constants.kElevatorMinHeight)), 0);
        master.configForwardSoftLimitEnable(false, 0);
        master.configReverseSoftLimitEnable(false,0);

        TalonUtil.setCoastMode(master, slaveOne, slaveTwo, slaveThree);

    }

    private void setOpenLoop(double power){
        master.set(ControlMode.PercentOutput, power);
    }

    public WantedState getWantedState() {
        return wantedState;
    }

    public SystemState getCurrentState() {
        return currentState;
    }

    private void setTargetPosition(ControlMode mode, double targetHeight, int slotID){
        if (!isHomed)return;
        if (stateChanged){
            master.selectProfileSlot(slotID,0);
        }
        master.set(mode, (int)heightToSensorUnits(targetHeight), 0);
    }

    private enum SystemState{
        CLOSED_LOOP_UP,
        CLOSED_LOOP_DOWN,
        HOLD,
        MANUAL_UP,
        MANUAL_DOWN,
        HANGING,
        HOMING,
    }

    public enum WantedState{
        //Preset when scale is in the other alliance's favor
        WANTS_TO_HIGH_SCALE_POS,
        //Preset when scale is balanced
        WANTS_TO_MID_SCALE_POS,
        //Preset when scale is in our alliance's favor
        WANTS_TO_LOW_SCALE_POS,
        //Preset for the switch
        WANTS_TO_SWITCH_POS,
        //Preset to lower the elevator to intake
        WANTS_TO_INTAKE_POS,
        //Manual control going up
        WANTS_TO_MANUAL_UP,
        //Manual control going down
        WANTS_TO_MANUAL_DOWN,
        //Hold position when using manual control
        WANTS_TO_HOLD,
        WANTS_TO_HANG,
        //Go up a bit to home, and then drop down...this should be only called when the robot has not been homed yet
        WANTS_TO_HOME,
    }

    @Override
    public void init(double timestamp) {
        currentState = SystemState.HOLD;
    }

    @Override
    public void update(double timestamp){
        if (prevWantedState != wantedState){
            wantedStateChanged = true;
            prevWantedState = wantedState;
        }
        else wantedStateChanged = false;
        if (wantedStateChanged){
            targetReached = false;
        }
        SystemState newState = SystemState.HOLD;
        switch(currentState){
            case HOLD:
                newState = handleHold();
                break;
            case CLOSED_LOOP_UP:
                newState = handleFastUp();
                break;
            case CLOSED_LOOP_DOWN:
                newState = handleFastDown();
                break;
            case MANUAL_UP:
                newState = handleManualControlUp();
                break;
            case MANUAL_DOWN:
                newState = handleManualControlDown();
                break;
            case HANGING:
                newState = handleHang();
            case HOMING:
                newState = handleHome(timestamp);
                break;
        }
        //State Transfer
        if(newState != currentState){
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;
    }

    @Override
    public void end(double timestamp) {

    }

    public boolean atClosedLoopTarget(){
        if (!m_usingClosedLoop || wantedStateChanged || stateChanged) return false;
        return (Math.abs(getHeight() - m_closedLoopTarget) < Constants.kElevatorTolerance);
    }

    public double getTargetHeight() {
        return m_closedLoopTarget;
    }

    private SystemState handleZeroPower() {
        if (stateChanged){
        }
        setOpenLoop(0);
        return defaultStateTransfer();
    }

    private SystemState handleHome(double timestamp) {
        if (isHomed()){
            return SystemState.HOLD;
        }
        if (stateChanged) {
            homingTimeStart = timestamp;
        }
        if (timestamp - homingTimeStart < Constants.kElevatorHomingUpTime && getHeight() < 9) {
            if (isHomed) {
                return SystemState.HOLD;
            }
            setOpenLoop(Constants.kElevatorUpSlowPower);
            return SystemState.HOMING;
        }
        else if (getHeight() > 9){
            isHomed = true;
            wantedState = WantedState.WANTS_TO_INTAKE_POS;
        }
        return SystemState.HOLD;
    }

    private SystemState handleHold(){
        if (stateChanged){
            master.selectProfileSlot(Constants.kElevatorHoldSlot,0);
        }
        if (getHeight() < Constants.kDropPreset){
            setOpenLoop(0);
            return defaultStateTransfer();
        }
        setTargetPosition(ControlMode.Position, getHeight(), Constants.kElevatorHoldSlot);
        return defaultStateTransfer();
    }

    private SystemState handleFastUp(){
        if(isHomed){
            if (atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            if (stateChanged){
                master.configMotionCruiseVelocity((int)velocityToSensorUnits(Constants.kElevatorMaxVelocityUp), 0);
                master.configMotionAcceleration((int)accelerationToSensorUnits(Constants.kElevatorMaxAccelerationUp), 0);
                master.selectProfileSlot(Constants.kElevatorMotionMagicUpSlot, 0);
            }
            setTargetPosition(ControlMode.MotionMagic, m_closedLoopTarget, Constants.kElevatorMotionMagicUpSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }

    private SystemState handleFastDown(){
        if(isHomed){
            if (atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            if (stateChanged){
                master.configMotionCruiseVelocity((int)velocityToSensorUnits(Constants.kElevatorMaxVelocityDown), 0);
                master.configMotionAcceleration((int)accelerationToSensorUnits(Constants.kElevatorMaxAccelerationDown), 0);
                master.selectProfileSlot(Constants.kElevatorMotionMagicDownSlot, 0);
            }
            setTargetPosition(ControlMode.MotionMagic, m_closedLoopTarget, Constants.kElevatorMotionMagicDownSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }

    private SystemState handleManualControlUp(){
        if (stateChanged){

        }
        setOpenLoop(Constants.kElevatorUpManualPower);
        return defaultStateTransfer();
    }

    private SystemState handleManualControlDown() {
        if (stateChanged){

        }
        setOpenLoop(Constants.kElevatorDownManualPower);
        return defaultStateTransfer();
    }

    private SystemState handleHang(){
        setOpenLoop(Constants.kElevatorHangPower);
        return defaultStateTransfer();
    }

    private double heightToSensorUnits(double inches) {
        return (inches/Constants.kElevatorPulleyDiameter)*4096.0*Constants.kElevatorGearRatio;
    }

    private double velocityToSensorUnits(double inchesPerSec){
        return heightToSensorUnits(inchesPerSec)/10.0;
    }

    private double accelerationToSensorUnits(double inchesPerSec2){
        return velocityToSensorUnits(inchesPerSec2)/10.0;
    }

    private double sensorUnitsToHeight(double ticks) {
        return (ticks/4096.0)*Constants.kElevatorPulleyDiameter/Constants.kElevatorGearRatio;
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    private SystemState defaultStateTransfer(){
        SystemState rv;
        switch (wantedState){
            case WANTS_TO_HIGH_SCALE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kHighScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case WANTS_TO_MID_SCALE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kMidScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case WANTS_TO_LOW_SCALE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kLowScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case WANTS_TO_HOLD:
                m_usingClosedLoop = false;
                return SystemState.HOLD;
            case WANTS_TO_SWITCH_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kSwitchPreset;
                }
                m_usingClosedLoop = true;
                break;
            case WANTS_TO_MANUAL_UP:
                m_usingClosedLoop = false;
                return SystemState.MANUAL_UP;
            case WANTS_TO_MANUAL_DOWN:
                m_usingClosedLoop = false;
                return SystemState.MANUAL_DOWN;
            case WANTS_TO_INTAKE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kIntakePreset;
                }
                m_usingClosedLoop = true;
                break;
            case WANTS_TO_HANG:
                m_usingClosedLoop = false;
                return SystemState.HANGING;
            case WANTS_TO_HOME:
                return SystemState.HOMING;
        }
        if(m_closedLoopTarget > getHeight() && m_usingClosedLoop) {
            rv = SystemState.CLOSED_LOOP_UP;
        }
        else if (m_closedLoopTarget < getHeight() && m_usingClosedLoop){
            rv = SystemState.CLOSED_LOOP_DOWN;
        }
        else rv = SystemState.HOLD;
        return rv;
    }

    public boolean isTriggered(){
        return !hallEffect.get();
    }

    public double getVelocity() {
        return master.getSelectedSensorVelocity(0);
    }

    public double getHeight() {
        return sensorUnitsToHeight(master.getSelectedSensorPosition(0));
    }

    public double getRawEncoder(){
        return master.getSelectedSensorPosition(0);
    }

    @Override
    public void outputToDashboard() {
        SmartDashboard.putBoolean("isHomed", isHomed());
        SmartDashboard.putNumber("height", getHeight());
        SmartDashboard.putBoolean("hallEffect", isTriggered());
    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }

    public boolean isHomed(){
        return isHomed;
    }

    public ControlMode getTalonControlMode(){
        return master.getControlMode();
    }

    public double getClosedLoopError(){
        return master.getClosedLoopError(0);
    }

    public double getOutputVoltage(){
        return master.getMotorOutputVoltage();
    }
}
