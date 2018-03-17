package frc.team3256.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;

public class Carriage extends SubsystemBase implements Loop {

    private TalonSRX pivotArm;
    private DigitalInput hallEffect;

    private SystemState currentState = SystemState.HOLD;
    private WantedState wantedState;

    private boolean isHomed = false;
    private boolean stateChanged = true;
    private boolean targetReached;
    private boolean failedToHome = false;

    private double m_closedLoopTarget;
    private boolean m_usingClosedLoop;

    private double homingTimeStart;

    private static Carriage instance;

    public static Carriage getInstance() {
        return instance == null ? instance = new Carriage() : instance;
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

    private InterruptHandlerFunction<Carriage> ihr = new InterruptHandlerFunction<Carriage>() {
        @Override
        public void interruptFired(int interruptAssertedMask, Carriage param) {
            if (pivotArm.getSelectedSensorVelocity(0) < 0){
                System.out.println("PIVOT ARM HOMED ON UPPER SIDE!!!!!!!!!!!!");
                pivotArm.setSelectedSensorPosition((int)angleToSensorUnits(Constants.kTopArmHomeAngle), 0, 0);
                isHomed = true;
                pivotArm.configForwardSoftLimitEnable(true, 0);
                pivotArm.configReverseSoftLimitEnable(true, 0);
            }
            else if (pivotArm.getSelectedSensorVelocity(0) > 0){
                System.out.println("PIVOT ARM HOMED ON LOWER SIDE!!!!!!!!!!!!");
                pivotArm.setSelectedSensorPosition((int)angleToSensorUnits(Constants.kBottomArmHomeAngle), 0, 0);
                isHomed = true;
                pivotArm.configForwardSoftLimitEnable(true, 0);
                pivotArm.configReverseSoftLimitEnable(true, 0);
            }
        }
    };

    @Override
    public void init(double timestamp) {
        setWantedState(WantedState.WANTS_TO_HOLD);
    }

    @Override
    public void update(double timestamp) {
        SystemState newState = SystemState.HOLD;
        switch(currentState){
            case HOLD:
                newState = handleHold();
                break;
            /*case CLOSED_LOOP:
                newState = handleClosedLoop();
                break;*/
            case PIVOT_UP:
                newState = handlePivotUp();
                break;
            case PIVOT_DOWN:
                newState = handlePivotDown();
                break;
            case MANUAL_FORWARD:
                newState = handleManualForward();
                break;
            case MANUAL_REVERSE:
                newState = handleManualReverse();
                break;
            case ZERO_POWER:
                newState = handleZeroPower();
                break;
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

    public enum SystemState{
        //CLOSED_LOOP,
        PIVOT_UP,
        PIVOT_DOWN,
        MANUAL_FORWARD,
        MANUAL_REVERSE,
        HOLD,
        ZERO_POWER,
        HOMING,
    }

    public enum WantedState{
        FRONT_SCORE_PRESET,
        BACK_SCORE_PRESET,
        EXCHANGE_PRESET,
        INTAKE_PRESET,
        STOW_PRESET,
        WANTS_TO_MANUAL_FORWARD,
        WANTS_TO_MANUAL_REVERSE,
        WANTS_TO_HOLD,
        WANTS_TO_HOME,
    }

    private Carriage() {
        hallEffect = new DigitalInput(Constants.kHallEffectPivotHomePort);
        hallEffect.requestInterrupts(ihr);
        hallEffect.setUpSourceEdge(false, true);
        hallEffect.enableInterrupts();

        pivotArm = TalonUtil.generateGenericTalon(Constants.kPivotArm);
        TalonUtil.configMagEncoder(pivotArm);
        pivotArm.setSelectedSensorPosition(0, 0, 0);
        
        //configure hold PID values
        TalonUtil.setPIDGains(pivotArm, Constants.kArmHoldSlot, Constants.kArmHoldP,
                Constants.kArmHoldI, Constants.kArmHoldD, 0);
        //configure up PID values
        TalonUtil.setPIDGains(pivotArm, Constants.kArmUpSlot, Constants.kArmUpP,
                Constants.kArmUpI, Constants.kArmUpD, 0);
        //configure down PID values
        TalonUtil.setPIDGains(pivotArm, Constants.kArmDownSlot, Constants.kArmDownP,
                Constants.kArmDownI, Constants.kArmDownD, 0);

        //voltage limiting
        TalonUtil.setPeakOutput(Constants.kArmMaxForwardVoltage/12.0,
                Constants.kArmMaxReverseVoltage/12.0, pivotArm);
        TalonUtil.setMinOutput(Constants.kArmMinHoldVoltage/12.0,
                0, pivotArm);

        //soft limits
        pivotArm.configForwardSoftLimitThreshold((int)(angleToSensorUnits(Constants.kArmMaxAngle)), 0);
        pivotArm.configReverseSoftLimitThreshold((int)(angleToSensorUnits(Constants.kArmMinAngle)), 0);
        pivotArm.configForwardSoftLimitEnable(false, 0);
        pivotArm.configReverseSoftLimitEnable(false,0);

        TalonUtil.setCoastMode(pivotArm);
    }

    public boolean atClosedLoopTarget(){
        if (!m_usingClosedLoop || stateChanged) return false;
        targetReached = true;
        return (Math.abs(getAngle() - m_closedLoopTarget) < Constants.kCarriageTolerance);
    }

    private SystemState handleZeroPower() {
        setOpenLoop(0);
        return defaultStateTransfer();
    }

    private SystemState handleHome(double timestamp) {
        if (failedToHome) return SystemState.ZERO_POWER;
        if (stateChanged) {
            homingTimeStart = timestamp;
        }
        if (timestamp - homingTimeStart < Constants.kArmHomingForwardTime) {
            if (isHomed) {
                return SystemState.ZERO_POWER;
            }
            setOpenLoop(Constants.kArmForwardSlowPower);
            return SystemState.HOMING;
        }
        else{
            failedToHome = true;
        }
        return SystemState.ZERO_POWER;
    }

    private SystemState handleHold(){
        if (stateChanged){
            pivotArm.selectProfileSlot(Constants.kArmHoldSlot,0);
        }
        if (getAngle() < Constants.kArmDropAnglePreset || 180.0-getAngle() < Constants.kArmDropAnglePreset) {
            return SystemState.ZERO_POWER;
        }
        setTargetPosition(getAngle(), Constants.kArmHoldSlot);
        return defaultStateTransfer();
    }

    private SystemState handlePivotUp(){
        if(isHomed){
            if(atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            if(stateChanged){
                pivotArm.selectProfileSlot(Constants.kArmUpSlot, 0);
            }
            setTargetPosition(m_closedLoopTarget,Constants.kArmUpSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }

    private SystemState handlePivotDown(){
        if(isHomed){
            if(atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            if(stateChanged){
                pivotArm.selectProfileSlot(Constants.kArmDownSlot, 0);
            }
            setTargetPosition(m_closedLoopTarget,Constants.kArmDownSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }

    /*private SystemState handleClosedLoop(){
        if(isHomed){
            if (atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            if (stateChanged){
                pivotArm.selectProfileSlot(Constants.kArmMovingSlot, 0);
            }

            setTargetPosition(m_closedLoopTarget, Constants.kArmMovingSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }*/

    private SystemState handleManualForward(){
        if (stateChanged){
        }
        setOpenLoop(Constants.kArmManualPowerConstant);
        return defaultStateTransfer();
    }

    private SystemState handleManualReverse() {
        if (stateChanged){
        }
        setOpenLoop(-Constants.kArmManualPowerConstant);
        return defaultStateTransfer();
    }

    private SystemState defaultStateTransfer(){
        SystemState rv;
        switch (wantedState){
            case FRONT_SCORE_PRESET:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kArmFrontScorePreset;
                }
                m_usingClosedLoop = true;
                break;
            case BACK_SCORE_PRESET:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kArmBackScorePreset;
                }
                m_usingClosedLoop = true;
                break;
            case INTAKE_PRESET:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kArmIntakePreset;
                }
                m_usingClosedLoop = true;
                break;
            case STOW_PRESET:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kArmStowPreset;
                }
                m_usingClosedLoop = true;
                break;
            case EXCHANGE_PRESET:
                if (stateChanged){
                    m_closedLoopTarget = Constants.kArmExchangePreset;
                }
            case WANTS_TO_HOLD:
                m_usingClosedLoop = false;
                return SystemState.HOLD;
            case WANTS_TO_MANUAL_FORWARD:
                m_usingClosedLoop = false;
                return SystemState.MANUAL_FORWARD;
            case WANTS_TO_MANUAL_REVERSE:
                m_usingClosedLoop = false;
                return SystemState.MANUAL_REVERSE;
            case WANTS_TO_HOME:
                return SystemState.HOMING;
        }
        /*if(!targetReached && m_usingClosedLoop) {
            rv = SystemState.CLOSED_LOOP;
        }*/
        if(m_closedLoopTarget > getAngle() && m_usingClosedLoop){
            rv = SystemState.PIVOT_DOWN;
        }
        if(m_closedLoopTarget < getAngle() && m_usingClosedLoop){
            rv = SystemState.PIVOT_UP;
        }
        else rv = SystemState.HOLD;
        return rv;
    }

    public WantedState getWantedState() { return wantedState; }

    public SystemState getCurrentState() { return currentState; }

    private double angleToSensorUnits(double degrees) {
        return degrees / 360.0 * 4096.0 * Constants.kArmGearRatio;
    }
    private double sensorUnitsToAngle(double ticks) {
        return ticks * 360.0 / 4096.0 / Constants.kArmGearRatio;
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

    private void setOpenLoop(double power){
        pivotArm.set(ControlMode.PercentOutput, power);
    }

    public double getAngle() {
        return sensorUnitsToAngle(pivotArm.getSelectedSensorPosition(0));
    }

    private void setTargetPosition(double targetAngle, int slotID){
        if (!isHomed)return;
        if (stateChanged){
            pivotArm.selectProfileSlot(slotID,0);
        }
        pivotArm.set(ControlMode.Position, (int)angleToSensorUnits(targetAngle), 0);
    }

}
