package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.DriveTrain;

public class FollowTrajectoryAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double startVel, endVel, distance, initialAngle;

    public FollowTrajectoryAction(double startVel, double endVel, double distance, double initialAngle) {
        this.startVel = startVel;
        this.endVel = endVel;
        this.distance = distance;
        this.initialAngle = initialAngle;
    }

    @Override
    public boolean isFinished() {
        return drive.isDriveStraightFinished();
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
        drive.setOpenLoop(0,0);
        drive.resetDriveStraightController();
    }

    @Override
    public void start() {
        drive.setHighGear(true);
        drive.resetDriveStraightController();
        drive.configureDriveStraight(startVel, endVel, distance + drive.getAverageDistance(), initialAngle);
    }
}
