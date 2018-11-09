package frc.team3256.robot.operation;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.robot.Constants;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

public class TeleopUpdater {

    private ControlsInterface controls = new DualLogitechConfig();

    private DriveTrain m_drive = DriveTrain.getInstance();

    public void update(){

        double throttle = controls.getThrottle();
        double turn = controls.getTurn();
        boolean quickTurn = controls.getQuickTurn();
        boolean shiftDown = controls.getLowGear();

        //Drivetrain subsystem

        DrivePower power = TeleopDriveController.curvatureDrive(throttle, turn, quickTurn, !shiftDown);

    }

    public double getThrottle() {
        return controls.getThrottle();
    }

    public double getTurn() {
        return controls.getTurn();
    }
}
