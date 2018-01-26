package frc.team3256.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;

public class Elevator {

    private static Elevator instance;
    private TalonSRX master, slave;
    private DigitalInput hallEffect;
    private boolean homed = false;
    private double offset = 0.0;

    public static Elevator getInstance(){
        return instance == null ? instance = new Elevator() : instance;
    }

    private InterruptHandlerFunction<Elevator> ihr = new InterruptHandlerFunction<Elevator>() {

        @Override
        public void interruptFired(int interruptAssertedMask, Elevator param) {
            if (!homed){
                offset = getRelativeHeight();
                homed = true;
                hallEffect.disableInterrupts();
            }
        }
    };


    private Elevator(){
        hallEffect = new DigitalInput(9);
        hallEffect.requestInterrupts(ihr);
        hallEffect.setUpSourceEdge(true, false);
        hallEffect.enableInterrupts();

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

    public double getRelativeHeight(){
        return master.getSelectedSensorPosition(0);
    }

    public double getAbsoluteHeight(){
        if (homed){
            return getRelativeHeight() + offset;
        }
        else return 0.0;
    }
}
