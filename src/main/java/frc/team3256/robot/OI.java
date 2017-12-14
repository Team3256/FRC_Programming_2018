package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class OI {
    static boolean leftTriggerPrevState = false;
    static XboxController xboxOne = new XboxController(Constants.kXboxPort);

    public static boolean presetOne() {
        if(Math.abs(xboxOne.getX(GenericHID.Hand.kRight)) > 0.2) return true;
        else return false;
    }

    public static boolean presetTwo() {
        if(Math.abs(xboxOne.getY(GenericHID.Hand.kRight)) > 0.2) return true;
        else return false;
    }

    public static boolean presetThree() {
        return xboxOne.getAButton();
    }

    public static boolean presetFour() {
        return xboxOne.getXButton();
    }

    public static boolean presetFive() {
        return xboxOne.getTrigger(GenericHID.Hand.kLeft);
    }

    public static boolean presetSix() {
        return xboxOne.getTrigger(GenericHID.Hand.kRight);
    }

    public static boolean presetSeven() {
        return xboxOne.getBumper(GenericHID.Hand.kLeft);
    }

    public static boolean presetEight() {
        return xboxOne.getBumper(GenericHID.Hand.kRight);
    }
}
