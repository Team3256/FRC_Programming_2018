package frc.team3256.robot.operation;

public interface ControlsInterface {

//--------------Driver-----------------

    //Y Axis: Throttle for drive
    double getThrottle();

    //X Axis: Turn for drive
    double getTurn();

    //Hold: Enables quick turn
    boolean getQuickTurn();

    //Hold: Shifts down to low gear (default is high gear)
    boolean getLowGear();

    //Hold: Runs carriage to shoot block forward
    boolean scoreFront();

    //Hold: Runs carriage to shoot block reverse
    boolean scoreRear();

    boolean scoreFrontSlow();

    boolean scoreRearSlow();

//--------------Operator-----------------

    //Hold: Runs intake
    boolean getIntake();

    //Hold: Runs intake backwards
    boolean getExhaust();

    //Single presos: Runs intake unjam thingy
    boolean getUnjam();

    //Single press: Toggles intake pivot
    boolean togglePivot();

    //Single press: Toggles intake flop
    boolean toggleFlop();

    //Single press: Elevator moves to switch preset
    boolean switchPreset();

    //Single press: Elevator moves to low scale preset
    boolean turnToCube();

    //Single press: Elevator moves to mid scale preset
    boolean scalePresetMid();

    //Single press: Elevator moves to high scale preset
    boolean scalePresetHigh();

    //Hold: Elevator manually moves up
    boolean manualElevatorUp();

    //Hold: Elevator manually moves down
    boolean manualElevatorDown();

}
