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

    private TalonSRX master, slave;
    private DigitalInput hallEffect, topBumper, bottomBumper;


    private static Elevator instance;
    public static Elevator getInstance() {
         return instance == null ? instance = new Elevator() : instance;
    }

    private Elevator() {
        hallEffect = new DigitalInput(Constants.kHallEffectPort);
        topBumper = new DigitalInput(Constants.kTopBumperPort);
        bottomBumper = new DigitalInput(Constants.kBottomBumperPort);

        master = TalonUtil.generateGenericTalon(Constants.kElevatorMaster);
        slave = TalonUtil.generateSlaveTalon(Constants.kElevatorSlave, Constants.kElevatorMaster);

        if (master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0) != ErrorCode.OK){
            DriverStation.reportError("Mag Encoder on elevator not detected!!!", false);
        }
        master.setSelectedSensorPosition(0, 0, 0);
    }


    public boolean isCalibrated(){
        return hallEffect.get();
    }

    public int getEncoderValue() {
        return master.getSelectedSensorPosition(0);
    }

    public void setTargetPosition(double targetPositionInches) {

    }

    public void setOpenLoopPower(double power) { master.set(ControlMode.PercentOutput, power); }

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
