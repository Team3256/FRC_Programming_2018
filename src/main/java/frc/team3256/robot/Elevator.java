package frc.team3256.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;

public class Elevator {

    private static Elevator instance;
    private TalonSRX master, slave;
    private DigitalInput hallEffect;

    public static Elevator getInstance(){
        return instance == null ? instance = new Elevator() : instance;
    }

    private Elevator(){

        hallEffect = new DigitalInput(0);
        hallEffect.setUpSourceEdge(true, false);

        master = new TalonSRX(1);
        slave = new TalonSRX(2);
        slave.set(ControlMode.Follower, master.getDeviceID());

        master.setNeutralMode(NeutralMode.Brake);
        slave.setNeutralMode(NeutralMode.Brake);

        master.configContinuousCurrentLimit(20, 0);
        master.configPeakCurrentLimit(30, 0);
        master.configPeakCurrentDuration(100, 0);
        master.enableCurrentLimit(true);
        slave.configContinuousCurrentLimit(20, 0);
        slave.configPeakCurrentLimit(30, 0);
        slave.configPeakCurrentDuration(100, 0);
        slave.enableCurrentLimit(true);

        if (master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
           0, 0) != ErrorCode.OK){
            DriverStation.reportError("ENCODER NOT DETECTED", false);
        }
    }

    public void setOpenLoop(double value){
        master.set(ControlMode.PercentOutput, value);
    }


    public boolean isTriggered(){
        return !hallEffect.get();
    }

    public double getHeight(){
        return master.getSelectedSensorPosition(0);
    }
}
