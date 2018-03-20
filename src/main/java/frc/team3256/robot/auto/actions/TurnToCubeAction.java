package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.DriveTrain;

public class TurnToCubeAction implements Action {

    DriveTrain drive = DriveTrain.getInstance();

    @Override
    public boolean isFinished() {
        return drive.isTurnInPlaceFinished();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        drive.setOpenLoop(0,0);
    }

    @Override
    public void start() {
        drive.setTurnInPlaceSetpoint(drive.getCubeOffsetAngle() + drive.getAngle().degrees());
    }
}
