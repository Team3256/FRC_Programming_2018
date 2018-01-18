package frc.team3256.robot.operation;

public interface ControlsInterface {

    //Driver
    public double getThrottle();

    public double getTurn();

    public boolean getQuickTurn();

    public boolean getLowGear();

    //Operator
    public boolean getIntake();

    public boolean getExhaust();

    public boolean unjamIntake();

    public boolean openIntake();

    public boolean deployIntake();

    public boolean stowIntake();

    public boolean scoreFront();

    public boolean scoreRear();

    public boolean switchPresetLow();

    public boolean switchPresetHigh();

    public boolean scalePresetLow();

    public boolean scalePresetHigh();

    public double manualElevatorUp();

    public double manualElevatorDown();

    public boolean manualSqueezeCarriage();
}
