package frc.team3256.robot.operation;

import edu.wpi.first.wpilibj.Joystick;

public class LogitechButtonBoardConfig implements ControlsInterface{

    private Joystick driver = new Joystick(0);
    private Joystick manipulator = new Joystick(1);

    @Override
    public double getThrottle(){
        return driver.getRawAxis(1);
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
    public boolean unjamIntake() {
        return false;
    }

    @Override
    public boolean openIntake() {
        return false;
    }

    @Override
    public boolean deployIntake() {
        return false;
    }

    @Override
    public boolean stowIntake() {
        return false;
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
    public boolean switchPresetLow() {
        return false;
    }

    @Override
    public boolean switchPresetHigh() {
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
