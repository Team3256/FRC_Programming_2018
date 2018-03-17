package frc.team3256.lib.trajectory;


import frc.team3256.lib.DrivePower;
import frc.team3256.robot.Constants;


public class DriveTrajectoryController {
    DriveArcController driveArcController = new DriveArcController();
    DriveStraightController driveStraightController = new DriveStraightController();
    public int curr_segment = 0;
    Trajectory leftTrajectory = new Trajectory(0);
    Trajectory rightTrajectory = new Trajectory(0);

    public void addToTrajectory(Trajectory leftTrajectory, Trajectory rightTrajectory){
        this.leftTrajectory.addPoints(leftTrajectory.points);
        this.rightTrajectory.addPoints(rightTrajectory.points);
    }

    public DrivePower updateTrajectory(double currPosLeft, double currPosRight, double currAngle, Trajectory leftTrajectory, Trajectory rightTrajectory){
        if(leftTrajectory.getType().equals(Trajectory.Point.Type.STRAIGHT)){
            Trajectory.Point point = leftTrajectory.getCurrPoint(curr_segment);
            curr_segment++;
            return (driveStraightController.update((currPosLeft + currPosRight)/2.0, currAngle, point, point.inverse));
        }
        else {
            Trajectory.Point leftPoint = leftTrajectory.getCurrPoint(curr_segment);
            Trajectory.Point rightPoint = rightTrajectory.getCurrPoint(curr_segment);
            curr_segment++;
            return (driveArcController.updateCalculations(currPosLeft, currPosRight, currAngle, leftPoint, rightPoint, rightPoint.inverse, rightPoint.radius, rightPoint.backTurn));
        }
    }

    public boolean isFinished() {
        return curr_segment >= leftTrajectory.getLength();
    }

    public void addStraightTrajectory(double startVel, double endVel, double distance){
        boolean inverse;
        if (distance < 0){
            inverse = true;
        }
        else inverse = false;
        TrajectoryGenerator trajectoryGenerator = new TrajectoryGenerator(Constants.kDistanceTrajectoryAccel, Constants.kDistanceTrajectoryCruiseVelocity, Constants.kControlLoopPeriod);
        Trajectory trajectory = trajectoryGenerator.generateTrajectory(startVel, endVel, distance, Trajectory.Point.Type.STRAIGHT, inverse);
        addToTrajectory(trajectory, trajectory);
    }

    public void addArcTrajectory(double startVel, double endVel, double degrees, double turnRadius, boolean backwardsTurn, double currAngle) {
        boolean inverseTurn;
        if (currAngle >= degrees){inverseTurn = true;}
        else inverseTurn = false;
        degrees = Math.abs(degrees);
        TrajectoryCurveGenerator trajectoryCurveGenerator = new TrajectoryCurveGenerator(Constants.kCurveTrajectoryMaxAccel, Constants.kCurveTrajectoryCruiseVelocity, Constants.kControlLoopPeriod, Constants.kRobotTrack);
        trajectoryCurveGenerator.generateTrajectoryCurve(startVel, endVel, degrees, turnRadius, inverseTurn, backwardsTurn);
        if (inverseTurn) {
            leftTrajectory = trajectoryCurveGenerator.followPath;
            rightTrajectory = trajectoryCurveGenerator.leadPath;
        } else {
            leftTrajectory = trajectoryCurveGenerator.leadPath;
            rightTrajectory = trajectoryCurveGenerator.followPath;
        }
        addToTrajectory(leftTrajectory, rightTrajectory);
    }
}
