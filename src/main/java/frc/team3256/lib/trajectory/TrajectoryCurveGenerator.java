package frc.team3256.lib.trajectory;

import java.util.ArrayList;

public class TrajectoryCurveGenerator {

    public double robotTrack = 2.4*12.0; //inches

    TrajectoryGenerator trajectoryGenerator;
    Trajectory leadPath;
    Trajectory followPath;

    public TrajectoryCurveGenerator(double acc, double maxVel, double dt){
        trajectoryGenerator = new TrajectoryGenerator(acc, maxVel, dt);
    }

    boolean right;

    public void generateTrajectoryCurve(double startVel, double endVel, double degrees, double turnRadius){
        double arcLeadLength = 2 * (turnRadius + (robotTrack * 0.5)) * Math.PI * (degrees/360);
        double followScale = (turnRadius - (robotTrack * 0.5))/((turnRadius + (robotTrack * 0.5)));
        leadPath = trajectoryGenerator.generateTrajectory(startVel, endVel, arcLeadLength);
        followPath = trajectoryGenerator.generateScaledTrajectory(leadPath, followScale);
    }

    public Trajectory getLeadPath() {
        return leadPath;
    }

    public Trajectory getFollowPath() {
        return followPath;
    }

    public static void main (String [] args){
        TrajectoryCurveGenerator trajectoryCurveGenerator = new TrajectoryCurveGenerator(144, 144, 0.005);
        trajectoryCurveGenerator.generateTrajectoryCurve(0, 0, 90, 36);

    }

}
