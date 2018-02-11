package frc.team3256.robot.operation;

public interface ControlsInterface {

    //Driver
    public double getThrottle();

    public double getTurn();

    public boolean getQuickTurn();

    public boolean getLowGear();

    //Operator
    boolean getIntake();

    boolean getExhaust();

    boolean unjamIntake();

    boolean openIntake();

    boolean deployIntake();

    boolean stowIntake();

    boolean scoreFront();

    boolean scoreRear();

    boolean switchPreset();

    boolean scalePresetLow();

    boolean scalePresetMid();

    boolean scalePresetHigh();

    double manualElevatorUp();

    double manualElevatorDown();

    boolean manualSqueezeCarriage();
}
