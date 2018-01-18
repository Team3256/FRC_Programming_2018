package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class OI {

    static Joystick xboxOne = new Joystick(Constants.kXboxPort);

    public static double getLeftY(){
        return xboxOne.getRawAxis(1);
    }

    public static boolean getA(){
        return xboxOne.getRawButton(1);
    }

    public static boolean rightTrigger(){
        return xboxOne.getRawAxis(4) > 0.25;
    }

    public static boolean leftTrigger(){
        return xboxOne.getRawAxis(3) > 0.25;
    }
}
