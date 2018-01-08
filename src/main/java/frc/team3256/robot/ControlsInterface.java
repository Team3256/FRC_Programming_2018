package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class ControlsInterface {
    static XboxController driver = new XboxController(0);

    public static double getTankLeft(){
        return driver.getY(GenericHID.Hand.kLeft);
    }

    public static double getTankRight(){
        return driver.getY(GenericHID.Hand.kRight);
    }

    public static double getThrottle(){
        return driver.getY(GenericHID.Hand.kLeft);
    }

    public static double getTurn(){
        return driver.getX(GenericHID.Hand.kRight);
    }

    public static boolean getQuickTurn(){
        return driver.getBumper(GenericHID.Hand.kRight);
    }
}
