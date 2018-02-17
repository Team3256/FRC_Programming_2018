package frc.team3256.robot.operation;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;

public class DualLogitechConfig implements ControlsInterface{

    private Joystick driver = new Joystick(0);
    private Joystick manipulator = new Joystick(1);

    @Override
    public double getThrottle(){
        return -driver.getRawAxis(1);
    }

    @Override
    public double getTurn(){
        return driver.getRawAxis(4);
    }

    @Override
    public boolean getQuickTurn(){
        return driver.getRawAxis(3) > 0.25;
    }

    @Override
    public boolean getLowGear() {
        return driver.getRawAxis(2) > 0.25;
    }

    @Override
    public boolean getIntake() {
        return manipulator.getRawAxis(3) > 0.25;
    }

    @Override
    public boolean getExhaust() {
        return manipulator.getRawAxis(2) > 0.25;
    }

    @Override
    public boolean unjamIntake(){
        return getIntake() && getExhaust();
    }

    @Override
    public boolean togglePivot() {
        return false;
    }

    @Override
    public boolean toggleFlop(){
        return false;
    }

    @Override
    public boolean scoreFront(){
        return false;
    }

    @Override
    public boolean scoreRear() {
        return false;
    }

    @Override
    public boolean switchPreset() {
        return false;
    }

    @Override
    public boolean scalePresetLow() {
        return false;
    }

    @Override
    public boolean scalePresetHigh() {
        return false;
    }

    @Override
    public double manualElevatorUp() {
        return 0;
    }

    @Override
    public double manualElevatorDown() {
        return 0;
    }

    @Override
    public boolean manualSqueezeCarriage() {
        return false;
    }
}