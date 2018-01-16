package frc.team3256.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import frc.team3256.robot.Constants;

public class Elevator {

    int hallEffectChannel = 0;

    TalonSRX master, slave;
    DigitalInput hallEffect, topBumper, bottomBumper;


    static Elevator instance;

    public static Elevator getInstance() {
         return instance == null ? instance = new Elevator() : instance;
    }

    public void init() {
        hallEffect = new DigitalInput(Constants.kHallEffectPort);
        topBumper = new DigitalInput(Constants.kTopBumperPort);
        bottomBumper = new DigitalInput(Constants.kBottomBumperPort);
        master = new TalonSRX(Constants.kElevatorMaster);
        slave = new TalonSRX(Constants.kElevatorSlave);
    }

    public boolean isCalibrated(){
        return hallEffect.get();
    }

    public int getEncoderValue() {
        return master.getSelectedSensorPosition(0);
    }
}
