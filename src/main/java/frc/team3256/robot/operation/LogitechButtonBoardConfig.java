package frc.team3256.robot.operation;

import edu.wpi.first.wpilibj.Joystick;

public class LogitechButtonBoardConfig implements ControlsInterface{

    private Joystick driver = new Joystick(0);
    private Joystick manipulator = new Joystick(1);

    @Override
    public double getThrottle(){
        return -driver.getRawAxis(1);
    }

    @Override
    public double getTurn(){
        return driver.getRawAxis(2);
    }

    @Override
    public boolean getQuickTurn(){
        return driver.getRawButton(6);
    }

    @Override
    public boolean getLowGear() {
        return false;
    }

    @Override
    public boolean getIntake() {
        return false;
    }

    @Override
    public boolean getExhaust() {
        return false;
    }

    @Override
    public boolean getUnjam() {
        return false;
    }

    @Override
    public boolean togglePivot() {
        return false;
    }

    @Override
    public boolean toggleFlop() {
        return manipulator.getRawButton(1);
    }

    @Override
    public boolean scoreFront() {
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
    public boolean turnToCube() {
        return false;
    }

    @Override
    public boolean scalePresetMid() {
        return false;
    }

    @Override
    public boolean scalePresetHigh() {
        return false;
    }

    @Override
    public boolean manualElevatorUp() {
        return false;
    }

    @Override
    public boolean manualElevatorDown() {
        return false;
    }

    @Override
    public boolean scoreFrontSlow(){
        return false;
    }

    @Override
    public boolean scoreRearSlow(){
        return false;
    }

}
