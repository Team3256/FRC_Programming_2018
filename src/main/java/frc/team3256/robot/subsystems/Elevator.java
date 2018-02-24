package frc.team3256.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;

public class Elevator extends SubsystemBase implements Loop{

    private TalonSRX master, slaveOne, slaveTwo, slaveThree;
    private DigitalInput hallEffect;

    private SystemState currentState = SystemState.HOLD;
    private WantedState wantedState = WantedState.HOLD;

    private boolean isHomed = false;
    private boolean stateChanged;

    private double m_closedLoopTarget;
    private boolean m_usingClosedLoop;

    private static Elevator instance;

    public static Elevator getInstance() {
         return instance == null ? instance = new Elevator() : instance;
    }

    private InterruptHandlerFunction<Elevator> ihr = new InterruptHandlerFunction<Elevator>() {

        @Override
        public void interruptFired(int interruptAssertedMask, Elevator param) {
            if (master.getSelectedSensorVelocity(0) < 0){
                System.out.println("HOMED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                master.setSelectedSensorPosition((int)heightToSensorUnits(Constants.kHomeHeight), 0, 0);
                isHomed = true;
                master.configForwardSoftLimitEnable(true, 0);
                master.configReverseSoftLimitEnable(true, 0);
                //hallEffect.disableInterrupts();
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
        master.setSelectedSensorPosition(0, 0, 0);

        //configure Hold PID values
        TalonUtil.setPIDGains(master, Constants.kElevatorHoldSlot, Constants.kElevatorHoldP,
                Constants.kElevatorHoldI, Constants.kElevatorHoldD, 0);
        //configure FastUp PID values
        TalonUtil.setPIDGains(master, Constants.kElevatorFastUpSlot, Constants.kElevatorFastUpP,
                Constants.kElevatorFastUpI, Constants.kElevatorFastUpD, 0);
        //configure FastDown PID values
        TalonUtil.setPIDGains(master, Constants.kElevatorFastDownSlot, Constants.kElevatorFastDownP,
                Constants.kElevatorFastDownI, Constants.kElevatorFastDownD, 0);


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

    public void setOpenLoop(double power){
        master.set(ControlMode.PercentOutput, power);
    }

    public WantedState getWantedState() {
        return wantedState;
    }

    public SystemState getCurrentState() {
        return currentState;
    }

    public void holdPosition(){
        setTargetPosition(getHeight(), Constants.kElevatorHoldSlot);
    }

    public TalonSRX getMaster() {
        return master;
    }

    public void setTargetPosition(double targetHeight, int slotID){
        if (!isHomed)return;
        if (stateChanged){
            master.selectProfileSlot(slotID,0);
        }
        master.set(ControlMode.Position, (int)heightToSensorUnits(targetHeight), 0);
    }

    public enum SystemState{
        CLOSED_LOOP_UP,
        CLOSED_LOOP_HOLD,
        HOLD,
        MANUAL_UP,
        MANUAL_DOWN,
    }

    public enum WantedState{
        //Preset when scale is in the other alliance's favor
        HIGH_SCALE_POS,
        //Preset when scale is balanced
        MID_SCALE_POS,
        //Preset when scale is in our alliance's favor
        LOW_SCALE_POS,
        //Preset for the switch
        SWITCH_POS,
        //Preset to lower the elevator to intake
        INTAKE_POS,
        //Manual control going up
        MANUAL_UP,
        //Manual control going down
        MANUAL_DOWN,
        //Hold position when using manual control
        HOLD,
    }

    @Override
    public void init(double timestamp) {

    }

    @Override
    public void update(double timestamp){
        SystemState newState = SystemState.HOLD;
        switch(currentState){
            case HOLD:
                newState = handleHold();
                break;
            case CLOSED_LOOP_UP:
                newState = handleFastUp();
                break;
            case CLOSED_LOOP_HOLD:
                newState = handleFastDown();
                break;
            case MANUAL_UP:
                newState = handleManualControlUp();
                break;
            case MANUAL_DOWN:
                newState = handleManualControlDown();
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
        if (!m_usingClosedLoop) return false;
        return (Math.abs(getHeight() - m_closedLoopTarget) < Constants.kElevatorTolerance);
    }

    public double getTargetHeight() {
        return m_closedLoopTarget;
    }

    private SystemState handleHold(){
        if (stateChanged){
            master.selectProfileSlot(Constants.kElevatorHoldSlot,0);
        }
        setTargetPosition(getHeight(), Constants.kElevatorHoldSlot);
        return defaultStateTransfer();
    }

    private SystemState handleFastUp(){
        if(isHomed){
            if (stateChanged){master.selectProfileSlot(Constants.kElevatorFastUpSlot, 0);}
            if (atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            setTargetPosition(m_closedLoopTarget, Constants.kElevatorFastUpSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }

    private SystemState handleFastDown(){
        if(isHomed){
            if (stateChanged){master.selectProfileSlot(Constants.kElevatorFastDownSlot, 0);}
            if(atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            setTargetPosition(m_closedLoopTarget, Constants.kElevatorFastDownSlot);
            return defaultStateTransfer();
        }
        return defaultStateTransfer();
    }

    private SystemState handleManualControlUp(){
        setOpenLoop(Constants.kElevatorUpManualPower);
        return defaultStateTransfer();
    }

    private SystemState handleManualControlDown() {
        setOpenLoop(Constants.kElevatorDownManualPower);
        return defaultStateTransfer();
    }

    private double heightToSensorUnits(double inches) {
        return (inches/Constants.kElevatorPulleyDiameter)*4096.0*Constants.kElevatorGearRatio;
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
            case HIGH_SCALE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kHighScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case MID_SCALE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kMidScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case LOW_SCALE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kLowScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case HOLD:
                m_usingClosedLoop = false;
                return SystemState.HOLD;
            case SWITCH_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kSwitchPreset;
                }
                m_usingClosedLoop = true;
                break;
            case MANUAL_UP:
                m_usingClosedLoop = false;
                return SystemState.MANUAL_UP;
            case MANUAL_DOWN:
                m_usingClosedLoop = false;
                return SystemState.MANUAL_DOWN;
            case INTAKE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kIntakePreset;
                }
                m_usingClosedLoop = true;
                break;
        }
        if(m_closedLoopTarget > getHeight() && m_usingClosedLoop) {
            rv = SystemState.CLOSED_LOOP_UP;
        }
        else if (m_closedLoopTarget < getHeight() && m_usingClosedLoop){
            rv = SystemState.CLOSED_LOOP_HOLD;
        }
        else rv = SystemState.HOLD;
        return rv;
    }

    public boolean isHomed(){
        return isHomed;
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

    @Override
    public void outputToDashboard() {
        /*
        SmartDashboard.putString("Current State: ", currentState.toString());
        SmartDashboard.putString("Wanted State: ", wantedState.toString());
        SmartDashboard.putBoolean("State Changed? ", stateChanged);
        SmartDashboard.putNumber("Motor Power: ", master.getMotorOutputVoltage());
        SmartDashboard.putString("Control Modes: ", master.getControlMode().toString() + ", " + slaveOne.getControlMode().toString() +
        slaveTwo.getControlMode().toString() + ", " + slaveThree.getControlMode().toString());
        SmartDashboard.putBoolean("Is Calibrated? ", isHomed);
        SmartDashboard.putNumber("Closed Loop Target: ", m_closedLoopTarget);
        SmartDashboard.putBoolean("Using Closed Loop? ", m_usingClosedLoop);
        SmartDashboard.putBoolean("Hall effect? ", hallEffect.get());
        */
    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }

    public ControlMode getTalonControlMode(){
        return master.getControlMode();
    }

    public double getClosedLoopError(){
        return master.getClosedLoopError(0);
    }
}
