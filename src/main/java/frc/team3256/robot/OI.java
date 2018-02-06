package frc.team3256.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    static Joystick joystick;

    static{
        joystick = new Joystick(0);
    }

    public static boolean getIntake(){
        return false;
    }

    public static boolean getOuttake(){
        return false;
    }

    public static boolean getUnjam(){
        return getIntake() && getOuttake();
    }


    public static boolean toggleIntake(){
        return false;
    }

    public static boolean toggleSqueeze(){
        return false;
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
