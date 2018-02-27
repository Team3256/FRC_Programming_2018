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

    boolean getUnjam();

    boolean togglePivot();

    boolean toggleFlop();

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
