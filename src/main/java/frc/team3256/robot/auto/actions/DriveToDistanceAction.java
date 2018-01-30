package frc.team3256.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.subsystems.DriveTrain;

import java.awt.*;

public class DriveToDistanceAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double distance;
    private double duration;
    private double startTime;
    private boolean usingTimeout;

    public DriveToDistanceAction(double distance) {
        this.distance = distance;
        usingTimeout = false;
    }

    public DriveToDistanceAction (double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
        usingTimeout = true;
    }

    @Override
    public boolean isFinished() {
        if(usingTimeout){
            return Timer.getFPGATimestamp() - startTime > duration || drive.isDriveToDistanceFinished();
        }
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
        drive.setStartAngle(drive.getAngle().degrees());
        startTime = Timer.getFPGATimestamp();
    }
}
