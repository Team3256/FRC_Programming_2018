package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class OI {
    static XboxController driver = new XboxController(0);

    public static double getLeft(){
        return driver.getY(GenericHID.Hand.kLeft);
    }
    public static double getRight(){
        return driver.getY(GenericHID.Hand.kRight);
    }
    public static double getTurn(){
        return driver.getX(GenericHID.Hand.kRight);
    }
}
