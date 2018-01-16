package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class ControlsInterface {
    private static Joystick driver = new Joystick(0);

    public static double getThrottle(){
        return driver.getRawAxis(1);
    }

    public static double getTurn(){
        return driver.getRawAxis(2);
        }

    public static boolean getQuickTurn(){
        return driver.getRawButton(6);
        }
}
