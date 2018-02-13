package frc.team3256.robot.operation;

public interface ControlsInterface {

    //Driver
    double getThrottle();

    double getTurn();

    boolean getQuickTurn();

    boolean getLowGear();

    //Operator
    boolean getIntake();

    boolean getExhaust();

    boolean unjamIntake();

    boolean togglePivot();

    boolean toggleFlop();

    boolean scoreFront();

    boolean scoreRear();

    boolean switchPreset();

    boolean scalePresetLow();

    boolean scalePresetHigh();

    double manualElevatorUp();

    double manualElevatorDown();

    boolean manualSqueezeCarriage();
}
