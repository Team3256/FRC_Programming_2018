package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class OI {

    static Joystick xboxOne = new Joystick(Constants.kXboxPort);

    public static double getLeftY(){
        return xboxOne.getRawAxis(1);
    }
}
