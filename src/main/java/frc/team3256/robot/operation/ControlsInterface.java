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

}
