package frc.team3256.robot.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;

public class Elevator extends SubsystemBase{

    private TalonSRX master, slaveOne, slaveTwo, slaveThree;
    private DigitalInput hallEffect, topBumper, bottomBumper;

    private SystemState currentState;
    private WantedState wantedState;
    private boolean isCalibrated;
    private boolean manualUp;
    private boolean manualDown;
    private boolean stateChanged;

    private double manualUpPower = 0;
    private double manualDownPower = 0;
    private double targetPos;
    private double HIGH_SCALE_POS = 0;
    private double MID_SCALE_POS = 0;
    private double LOW_SCALE_POS = 0;
    private double SWITCH_POS = 0;
    private double INTAKE_POS = 0;
    private double STOW_POS = 0;

    private static Elevator instance;
    public static Elevator getInstance() {
         return instance == null ? instance = new Elevator() : instance;
    }

    private Elevator() {
        hallEffect = new DigitalInput(Constants.kHallEffectPort);
        topBumper = new DigitalInput(Constants.kTopBumperPort);
        bottomBumper = new DigitalInput(Constants.kBottomBumperPort);

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
        master.set(ControlMode.Position, getEncoderValue());
    }

    public void setPosition(double targetPos, int slotID){
        master.set(ControlMode.Position, targetPos, slotID);
    }

    public enum SystemState{
        FAST_UP,
        FAST_DOWN,
        HOLD,
        MANUAL_CONTROL
    }

    public enum WantedState{
        HIGH_SCALE,
        MID_SCALE,
        LOW_SCALE,
        HOLD,
        SWITCH,
        MANUAL_CONTROL,
        INTAKE_POS,
        STOW
    }

    public void update(double currPos){
        SystemState newState;
        switch(currentState){
            case HOLD:
                newState = handleHold();
            case FAST_UP:
                newState = handleFastUp();
            case FAST_DOWN:
                newState = handleFastDown();
            case MANUAL_CONTROL: default:
                newState = handleManualControl();
        }
        //State Transfer
        if(newState != currentState){
            currentState = newState;
            stateChanged = true;
        }
        else stateChanged = false;
    }

    private SystemState handleHold(){
        if(isCalibrated) {

        }
        setPosition(targetPos, Constants.kElevatorHoldSlot);
        return SystemState.HOLD;
    }

    private SystemState handleFastUp(){
        if(isCalibrated){

        }
        setPosition(targetPos, Constants.kElevatorFastUpSlot);
        return SystemState.FAST_UP;
    }

    private SystemState handleFastDown(){
        if(isCalibrated){

        }
        setPosition(targetPos, Constants.kElevatorFastDownSlot);
        return SystemState.FAST_DOWN;
    }

    private SystemState handleManualControl(){
        if(manualUp){
            setOpenLoop(manualUpPower);
            return SystemState.MANUAL_CONTROL;
        }
        else if(manualDown){
            setOpenLoop(manualDownPower);
            return SystemState.MANUAL_CONTROL;
        }
        else{
            setHold();
            return SystemState.HOLD;
        }
    }

    private void setWantedState(WantedState wantedState, boolean manualUp, boolean manualDown){
        this.wantedState = wantedState;
        this.manualUp = manualUp;
        this.manualDown = manualDown;
    }

    private SystemState defaultStateTransfer(){
        switch (wantedState){
            case HIGH_SCALE:
                targetPos = HIGH_SCALE_POS;
                if(targetPos > getEncoderValue()) {
                    return SystemState.FAST_UP;
                }
                else{
                    return SystemState.FAST_DOWN;
                }
            case MID_SCALE:
                targetPos = MID_SCALE_POS;
                if(targetPos > getEncoderValue()){
                    return SystemState.FAST_UP;
                }
                else{
                    return SystemState.FAST_DOWN;
                }
            case LOW_SCALE:
                targetPos = LOW_SCALE_POS;
                if(targetPos > getEncoderValue()){
                    return SystemState.FAST_UP;
                }
                else{
                    return SystemState.FAST_DOWN;
                }
            case HOLD:
                targetPos = getEncoderValue();
                return SystemState.HOLD;
            case SWITCH:
                targetPos = SWITCH_POS;
                if(targetPos > getEncoderValue()){
                    return SystemState.FAST_UP;
                }
                else{
                    return SystemState.FAST_DOWN;
                }
            case MANUAL_CONTROL:
                return SystemState.MANUAL_CONTROL;
            case INTAKE_POS:
                targetPos = INTAKE_POS;
                if(targetPos > getEncoderValue()){
                    return SystemState.FAST_UP;
                }
                else{
                    return SystemState.FAST_DOWN;
                }
            case STOW:
                targetPos = STOW_POS;
                if(targetPos > getEncoderValue()){
                    return SystemState.FAST_UP;
                }
                else{
                    return SystemState.FAST_DOWN;
                }
        }
    }

    public boolean isCalibrated(){
        return hallEffect.get();
    }

    public int getEncoderValue() {
        return master.getSelectedSensorPosition(0);
    }

    public void setTargetPosition(double targetPositionInches) {

    }

    public boolean topBumperTriggered() { return topBumper.get(); }

    public boolean bottomBumperTriggered() { return bottomBumper.get(); }

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
