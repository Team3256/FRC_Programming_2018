package frc.team3256.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    static Joystick joystick;

    static{
        joystick = new Joystick(0);
    }

    public static boolean getIntake(){
        return joystick.getRawAxis(3) > 0.25;
    }

    public static boolean getOuttake(){
        return joystick.getRawAxis(2) > 0.25;
    }

    public static boolean getUnjam(){
        return getIntake() && getOuttake();
    }


    public static boolean toggleIntake(){
        return false;
    }

    public static boolean toggleSqueeze(){
        return joystick.getRawButton(4);
    }

    public static double elevatorPower(){
        return 0.0;
    }

    public static boolean intakeCarriage(){
       return false;
    }

    public static boolean outtakeCarriage(){
        return false;
    }

}
