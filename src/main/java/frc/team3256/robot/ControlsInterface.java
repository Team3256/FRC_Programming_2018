package frc.team3256.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class ControlsInterface {
    private static Joystick driver = new Joystick(0);
    private static Joystick manipulator = new Joystick(1);

    public static double getThrottle(){
        return driver.getRawAxis(1);
    }

    public static double getTurn(){
        return driver.getRawAxis(2);
        }

    public static boolean getQuickTurn(){
        return driver.getRawButton(6);
        }

    public static boolean intakeCube(){return false;}
    public static boolean exhaustCube(){return false;}
    public static boolean unjamIntake () {return false;}
    public static boolean manualOpen(){return false;}
    public static boolean scoreFront(){return false;}
    public static boolean scoreBack(){return false;}
    public static boolean raiseElevatorToLowest(){return false;}
    public static boolean raiseElevatorToMiddle(){return false;}
    public static boolean raiseElevatorToHighest(){return false;}
    public static boolean elevatorManualUp(){return false;}
    public static boolean elevatorManualDown(){return false;}
    public static boolean manualSqueeze(){return false;} //uncertain atm
}