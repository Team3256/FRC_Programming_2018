package frc.team3256.robot.subsystems;


import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import frc.team3256.lib.Loop;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;

public class Elevator extends SubsystemBase implements Loop{

    private TalonSRX master, slaveOne, slaveTwo, slaveThree;
    private DigitalInput hallEffect;

    private SystemState currentState;
    private WantedState wantedState;

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
            if (!isHomed){
                master.setSelectedSensorPosition((int)heightToSensorUnits(Constants.kHomeHeight), 0, 0);
                isHomed = true;
                master.configForwardSoftLimitEnable(true, 0);
                master.configReverseSoftLimitEnable(true, 0);
                hallEffect.disableInterrupts();
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

        master.configPeakOutputForward(3.0/12.0, 0);
        master.configPeakOutputReverse(-3.0/12.0,0);
        slaveOne.configPeakOutputForward(3.0/12.0, 0);
        slaveOne.configPeakOutputReverse(-3.0/12.0,0);
        slaveTwo.configPeakOutputForward(3.0/12.0, 0);
        slaveTwo.configPeakOutputReverse(-3.0/12.0,0);
        slaveThree.configPeakOutputForward(3.0/12.0, 0);
        slaveThree.configPeakOutputReverse(-3.0/12.0,0);


        master.configNominalOutputForward(0.5/12.0, 0);
        master.configNominalOutputReverse(-0.5/12.0,0);
        slaveOne.configNominalOutputForward(0.5/12.0, 0);
        slaveOne.configNominalOutputReverse(-0.5/12.0,0);
        slaveTwo.configNominalOutputForward(0.5/12.0, 0);
        slaveTwo.configNominalOutputReverse(-0.5/12.0,0);
        slaveThree.configNominalOutputForward(0.5/12.0, 0);
        slaveThree.configNominalOutputReverse(-0.5/12.0,0);


        //soft limits

        master.configForwardSoftLimitThreshold((int)(heightToSensorUnits(Constants.kElevatorMaxHeight)), 0);
        master.configReverseSoftLimitThreshold((int)(heightToSensorUnits(Constants.kElevatorMinHeight)), 0);
        master.configForwardSoftLimitEnable(false, 0);
        master.configReverseSoftLimitEnable(false,0);

        master.setNeutralMode(NeutralMode.Brake);
        slaveOne.setNeutralMode(NeutralMode.Brake);
        slaveTwo.setNeutralMode(NeutralMode.Brake);
        slaveThree.setNeutralMode(NeutralMode.Brake);

    }

    public void setOpenLoop(double power){
        tempflag = false;
        master.set(ControlMode.PercentOutput, power);
    }

    public void holdPosition(){
        //TODO: Make this the absolute position
        setTargetPosition(getHeight(), Constants.kElevatorHoldSlot);
    }

    boolean tempflag = false;

    public void setTargetPosition(double targetHeight, int slotID){
        if (!isHomed)return;
        System.out.println("OUTPUT VOLTAGE: " + master.getMotorOutputVoltage());
        if (!tempflag) {
            master.selectProfileSlot(slotID, 0);
            tempflag = true;
        }
        master.set(ControlMode.Position, (int)heightToSensorUnits(targetHeight), 0);
    }

    public enum SystemState{
        FAST_UP,
        FAST_DOWN,
        HOLD,
        MANUAL_UP,
        MANUAL_DOWN,
    }

    public enum WantedState{
        HIGH_SCALE,
        MID_SCALE,
        LOW_SCALE,
        HOLD,
        SWITCH,
        MANUAL_UP,
        MANUAL_DOWN,
        INTAKE_POS,
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
            case FAST_UP:
                newState = handleFastUp();
                break;
            case FAST_DOWN:
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
            System.out.println("\tCURR_STATE:" + currentState + "\tNEW_STATE:" + newState);
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

    private SystemState handleHold(){
        setTargetPosition(getHeight(), Constants.kElevatorHoldSlot);
        return defaultStateTransfer();
    }

    private SystemState handleFastUp(){
        if(isHomed){
            if (atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            setTargetPosition(m_closedLoopTarget, Constants.kElevatorFastUpSlot);
            return SystemState.FAST_UP;
        }
        return defaultStateTransfer();
    }

    private SystemState handleFastDown(){
        if(isHomed){
            if(atClosedLoopTarget()){
                return SystemState.HOLD;
            }
            setTargetPosition(m_closedLoopTarget, Constants.kElevatorFastDownSlot);
            return SystemState.FAST_DOWN;
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
        SystemState rv = SystemState.HOLD;
        switch (wantedState){
            case HIGH_SCALE:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kHighScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case MID_SCALE:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kMidScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case LOW_SCALE:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kLowScalePreset;
                }
                m_usingClosedLoop = true;
                break;
            case HOLD:
                if(stateChanged) {
                    m_closedLoopTarget = getHeight();
                }
                m_usingClosedLoop = true;
                break;
            case SWITCH:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kSwitchPreset;
                }
                m_usingClosedLoop = true;
                break;
            case MANUAL_UP:
                m_usingClosedLoop = false;
                break;
            case MANUAL_DOWN:
                m_usingClosedLoop = false;
                break;
            case INTAKE_POS:
                if(stateChanged) {
                    m_closedLoopTarget = Constants.kIntakePreset;
                }
                m_usingClosedLoop = true;
                break;
            default:
                rv = SystemState.HOLD;
                break;
        }
        if(m_closedLoopTarget > getHeight() && m_usingClosedLoop) {
            rv = SystemState.FAST_UP;
        }
        else if (m_closedLoopTarget < getHeight() && m_usingClosedLoop){
            rv = SystemState.FAST_DOWN;
        }
        return rv;
    }

    public boolean isHomed(){
        return isHomed;
    }

    public boolean isTriggered(){
        return !hallEffect.get();
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
