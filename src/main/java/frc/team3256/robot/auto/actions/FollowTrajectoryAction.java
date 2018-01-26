package frc.team3256.robot.auto.actions;

import frc.team3256.lib.Kinematics;
import frc.team3256.lib.trajectory.Trajectory;
import frc.team3256.lib.trajectory.TrajectoryFollower;
import frc.team3256.lib.trajectory.TrajectoryGenerator;
import frc.team3256.robot.subsystems.DriveTrain;

public class FollowTrajectoryAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double startVel, endVel, distance;
    private TrajectoryFollower trajectoryFollower = new TrajectoryFollower();
    private Trajectory trajectory;

    public FollowTrajectoryAction(double startVel, double endVel, double distance) {
        this.trajectory = new TrajectoryGenerator().generateTrajectory(startVel, endVel, distance);
        this.startVel = startVel;
        this.endVel = endVel;
        this.distance = distance;
    }

    @Override
    public boolean isFinished() {
        return trajectoryFollower.isFinished();
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
    }

    @Override
    public void start() {
        drive.configureDistanceTrajectory(startVel, endVel, distance);
        drive.updateTrajectory();
    }
}