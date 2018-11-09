package frc.team3256.robot.operation;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class DualLogitechConfig implements ControlsInterface{

    private Joystick driver = new Joystick(0);
    private Joystick manipulator = new Joystick(1);

    //Driver: Vertical Axis Left Joystick
    @Override
    public double getThrottle(){
        return -driver.getRawAxis(5);
    }

    //Driver: Horizontal Axis Right Joystick
    @Override
    public double getTurn(){
        return driver.getRawAxis(4);
    }

    //Driver: Right Trigger
    @Override
    public boolean getQuickTurn(){
        return driver.getRawAxis(3) > 0.25;
    }

}