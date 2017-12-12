package frc.team3256.lib.hardware;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Talon;

/**
 * Utility class to generate talon objects
 */
public class TalonGenerator {
    public static CANTalon generateMasterTalon(int id) {
        CANTalon talon = new CANTalon(id, 5);
        talon.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        talon.set(0);
        return talon;
    }

    public static CANTalon generateSlaveTalon(int id, int masterId) {
        CANTalon talon = new CANTalon(id, 1000);
        talon.changeControlMode(CANTalon.TalonControlMode.Follower);
        talon.set(masterId);
        return talon;
    }
}
