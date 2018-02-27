package frc.team3256.robot.operation;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class DualLogitechConfig implements ControlsInterface{

    private Joystick driver = new Joystick(0);
    private Joystick manipulator = new Joystick(1);

    //Driver: Vertical Axis Left Joystick
    @Override
    public double getThrottle(){
        return -driver.getRawAxis(1);
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

    //Driver: Left Trigger
    @Override
    public boolean getLowGear() {
        return driver.getRawAxis(2) > 0.25;
    }

    //Manipulator: Right Trigger
    @Override
    public boolean getIntake() {
        return manipulator.getRawAxis(3) > 0.25;
    }

    //Manipulator: Left Trigger
    @Override
    public boolean getExhaust() {
        return manipulator.getRawAxis(2) > 0.25;
    }

    //Manipulator: Both Triggers
    @Override
    public boolean getUnjam(){
        return getIntake() && getExhaust();
    }

    //Manipulator: Button Y
    @Override
    public boolean togglePivot() { return manipulator.getRawButton(3); }

    //Manipulator: Button A
    @Override
    public boolean toggleFlop(){
        return manipulator.getRawButton(1);
    }

    //Driver:
    @Override
    public boolean scoreFront(){
        return manipulator.getRawButton(6);
    }

    //Driver:
    @Override
    public boolean scoreRear() {
        return manipulator.getRawButton(4);
    }

    @Override
    public boolean switchPreset() {
        return manipulator.getRawButton(5);
    }

    @Override
    public boolean scalePresetLow() {
        return driver.getRawButton(1);
}

    @Override
    public boolean scalePresetMid() {return driver.getRawButton(42);}

    @Override
    public boolean scalePresetHigh() {
        return manipulator.getRawButton(2);
    }

    @Override
    public double manualElevatorUp() {
        return -manipulator.getRawAxis(1);
    }

    @Override
    public double manualElevatorDown() {
        return manipulator.getRawAxis(1);
    }

    @Override
    public boolean manualSqueezeCarriage() {
        return false;
    }
}