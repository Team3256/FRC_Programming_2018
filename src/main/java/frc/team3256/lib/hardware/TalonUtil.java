package frc.team3256.lib.hardware;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team3256.robot.ControlsInterface;
import jdk.net.SocketFlow;

/**
 * Utility class to generate talon objects
 */
public class TalonUtil{

    public static TalonSRX generateGenericTalon(int id) {
        TalonSRX talon = new TalonSRX(id);
        talon.set(ControlMode.PercentOutput, 0);
        //setup frame periods
        talon.setControlFramePeriod(ControlFrame.Control_3_General, 5);
        talon.setControlFramePeriod(ControlFrame.Control_4_Advanced, 5);
        talon.setControlFramePeriod(ControlFrame.Control_6_MotProfAddTrajPoint, 1000);
        talon.setStatusFramePeriod(StatusFrame.Status_1_General, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 100, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_6_Misc, 100, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_7_CommStatus, 100, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_9_MotProfBuffer, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 5, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 1000, 10);
        //disable brake mode on default
        talon.setNeutralMode(NeutralMode.Brake);
        //no default reverse
        talon.setSensorPhase(false);
        talon.setInverted(false);
        //no current limit on default
        talon.enableCurrentLimit(false);
        //no soft limits on default
        talon.configForwardSoftLimitEnable(false, 10);
        talon.configReverseSoftLimitEnable(false, 10);
        //no hard limits on default
        talon.overrideLimitSwitchesEnable(false);
        //no ramping on default
        talon.configOpenloopRamp(0,10);
        talon.configClosedloopRamp(0, 10);
        return talon;
    }

    public static TalonSRX generateSlaveTalon(int id, int masterId) {
        TalonSRX  talon = new TalonSRX(id);
        talon.set(ControlMode.Follower, masterId);
        //we don't really need slaves to update fast, so update at 1hz
        talon.setControlFramePeriod(ControlFrame.Control_3_General, 1000);
        talon.setControlFramePeriod(ControlFrame.Control_4_Advanced, 1000);
        talon.setControlFramePeriod(ControlFrame.Control_6_MotProfAddTrajPoint, 1000);
        talon.setStatusFramePeriod(StatusFrame.Status_1_General, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_6_Misc, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_7_CommStatus, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_9_MotProfBuffer, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_10_MotionMagic, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 1000, 10);
        talon.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 1000, 10);
        //enable brake mode on default
        talon.setNeutralMode(NeutralMode.Brake);
        //no default reverse
        talon.setSensorPhase(false);
        talon.setInverted(false);
        //no current limit on default
        talon.enableCurrentLimit(false);
        //no soft limits on default
        talon.configForwardSoftLimitEnable(false, 10);
        talon.configReverseSoftLimitEnable(false, 10);
        //no hard limits on default
        talon.overrideLimitSwitchesEnable(false);
        //no ramping on default
        talon.configOpenloopRamp(0, 10);
        talon.configClosedloopRamp(0, 10);
        return talon;
    }

}
