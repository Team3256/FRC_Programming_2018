package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.DriveTrain;

import java.awt.*;

public class DriveToDistanceAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double distance;

    public DriveToDistanceAction(double distance) {
        this.distance = distance;
    }

    @Override
    public boolean isFinished() {
        return drive.isDriveToDistanceFinished();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        drive.setOpenLoop(0,0);
        drive.setOffsets(drive.getLeftDistance(), drive.getRightDistance());
    }

    @Override
    public void start() {
        drive.setDriveToDistanceSetpoint(distance);
        drive.setOffsets(drive.getLeftDistance(), drive.getRightDistance());
    }
}
