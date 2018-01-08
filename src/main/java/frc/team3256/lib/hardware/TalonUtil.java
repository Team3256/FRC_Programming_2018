package frc.team3256.lib.hardware;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Talon;

/**
 * Utility class to generate talon objects
 */
public class TalonUtil{

    public static CANTalon generateGenericTalon(int id) {
        CANTalon talon = new CANTalon(id, 5);
        talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        talon.set(0);
        //these provide all the basic info we need, so get these at 200hz
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 5);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.General, 5);
        //we don't really care about these, so 10hz
        talon.changeMotionControlFramePeriod(100);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 100);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.AnalogTempVbat, 100);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.PulseWidth, 100);
        //enable brake mode on default
        talon.enableBrakeMode(true);
        //no default reverse
        talon.reverseSensor(false);
        talon.reverseOutput(false);
        //no current limit on default
        talon.EnableCurrentLimit(false);
        talon.setCurrentLimit(0);
        //no soft limits on default
        talon.enableForwardSoftLimit(false);
        talon.setForwardSoftLimit(0);
        talon.enableReverseSoftLimit(false);
        talon.setReverseSoftLimit(0);
        //no hard limits on default
        talon.enableLimitSwitch(false, false);
        talon.ConfigFwdLimitSwitchNormallyOpen(true);
        //no ramping on default
        talon.setVoltageRampRate(0);
        talon.setVoltageCompensationRampRate(0);
        //reset all sensor data
        talon.setPosition(0);
        talon.setEncPosition(0);
        talon.setPulseWidthPosition(0);
        talon.setAnalogPosition(0);
        return talon;
    }

    public static CANTalon generateSlaveTalon(int id, int masterId) {
        CANTalon talon = new CANTalon(id, 1000);
        talon.changeControlMode(CANTalon.TalonControlMode.Follower);
        talon.set(masterId);
        //we don't really need slaves to update fast, so update at 1hz
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 1000);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.General, 1000);
        talon.changeMotionControlFramePeriod(1000);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 1000);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.AnalogTempVbat, 1000);
        talon.setStatusFrameRateMs(CANTalon.StatusFrameRate.PulseWidth, 1000);
        //enable brake mode on default
        talon.enableBrakeMode(true);
        //no default reverse
        talon.reverseSensor(false);
        talon.reverseOutput(false);
        //no current limit on default
        talon.EnableCurrentLimit(false);
        talon.setCurrentLimit(0);
        //no soft limits on default
        talon.enableForwardSoftLimit(false);
        talon.setForwardSoftLimit(0);
        talon.enableReverseSoftLimit(false);
        talon.setReverseSoftLimit(0);
        //no hard limits on default
        talon.enableLimitSwitch(false, false);
        talon.ConfigFwdLimitSwitchNormallyOpen(true);
        //no ramping on default
        talon.setVoltageRampRate(0);
        talon.setVoltageCompensationRampRate(0);
        //reset all sensor data
        talon.setPosition(0);
        talon.setEncPosition(0);
        talon.setPulseWidthPosition(0);
        talon.setAnalogPosition(0);
        return talon;
    }

    //Checks if a mag encoder is hooked up to the talon in relative mode
    public static boolean magEncConnected(CANTalon talon){
        return talon.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative) == CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent;
    }
}
