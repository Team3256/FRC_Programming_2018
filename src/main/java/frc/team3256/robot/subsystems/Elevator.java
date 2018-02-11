package frc.team3256.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;

public class Elevator extends SubsystemBase{

    private TalonSRX master, slaveOne, slaveTwo, slaveThree;
    private DigitalInput hallEffect;

    private SystemState currentState;
    private WantedState wantedState;

    private boolean isCalibrated = false;
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
            if (!isCalibrated){
                master.setSelectedSensorPosition((int)Constants.kHomeHeight, 0, 0);
                isCalibrated = true;
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

        if (master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0) != ErrorCode.OK){
            DriverStation.reportError("Mag Encoder on elevator not detected!!!", false);
        }
        master.setSelectedSensorPosition(0, 0, 0);

        //configure Hold PID values
        master.config_kP(Constants.kElevatorHoldSlot, Constants.kElevatorHoldP, 0);
        master.config_kI(Constants.kElevatorHoldSlot, Constants.kElevatorHoldI, 0);
        master.config_kD(Constants.kElevatorHoldSlot, Constants.kElevatorHoldD, 0);
        //configure FastUp PID values
        master.config_kP(Constants.kElevatorFastUpSlot, Constants.kElevatorFastUpP, 0);
        master.config_kI(Constants.kElevatorFastUpSlot, Constants.kElevatorFastUpI, 0);
        master.config_kD(Constants.kElevatorFastUpSlot, Constants.kElevatorFastUpD, 0);
        //configure FastDown PID values
        master.config_kP(Constants.kElevatorFastDownSlot, Constants.kElevatorFastDownP, 0);
        master.config_kI(Constants.kElevatorFastDownSlot, Constants.kElevatorFastDownI, 0);
        master.config_kD(Constants.kElevatorFastDownSlot, Constants.kElevatorFastDownD, 0);
    }

    public void setOpenLoop(double power){
        master.set(ControlMode.PercentOutput, power);
    }

    public void setHold(){
        //TODO: Make this the absolute position
        setTargetPosition(getEncoderValue(), Constants.kElevatorHoldSlot);
    }

    public void setTargetPosition(double targetPos, int slotID){
        master.set(ControlMode.Position, targetPos, slotID);
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

    public void update(double currPos){
        if(isTriggered() && !isCalibrated()){
        }
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

    private SystemState handleHold(){
        setTargetPosition(m_closedLoopTarget, Constants.kElevatorHoldSlot);
        return defaultStateTransfer();
    }

    private SystemState handleFastUp(){
        if(isCalibrated){

        }
        setTargetPosition(m_closedLoopTarget, Constants.kElevatorFastUpSlot);
        return SystemState.FAST_UP;
    }

    private SystemState handleFastDown(){
        if(isCalibrated){

        }
        setTargetPosition(m_closedLoopTarget, Constants.kElevatorFastDownSlot);
        return SystemState.FAST_DOWN;
    }

    private SystemState handleManualControlUp(){
        setOpenLoop(Constants.kElevatorUpManualPower);
        return SystemState.MANUAL_UP;
    }

    private SystemState handleManualControlDown(){
        setOpenLoop(Constants.kElevatorDownManualPower);
        return SystemState.MANUAL_DOWN;
    }

    private void setWantedState(WantedState wantedState){
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
                    m_closedLoopTarget = getEncoderValue();
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
        if(m_closedLoopTarget > getEncoderValue() && m_usingClosedLoop) {
            rv = SystemState.FAST_UP;
        }
        else if (m_closedLoopTarget < getEncoderValue() && m_usingClosedLoop){
            rv = SystemState.FAST_DOWN;
        }
        return rv;
    }

    public boolean isCalibrated(){
        return isCalibrated;
    }

    public boolean isTriggered(){
        return !hallEffect.get();
    }

    public int getEncoderValue() {
        return master.getSelectedSensorPosition(0);
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
}
