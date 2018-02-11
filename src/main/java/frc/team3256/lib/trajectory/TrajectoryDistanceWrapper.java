package frc.team3256.lib.trajectory;

import frc.team3256.robot.Constants;
import frc.team3256.robot.subsystems.DriveTrain;

public class TrajectoryDistanceWrapper {

    private double kP, kI, kD, kV, kA, dt;
    double PID, error, sumError, changeError = 0, prevError = 0;
    private Trajectory trajectory;
    private int curr_segment;
    private double output, feedForwardValue, feedBackValue;

    public void setGains(double kP, double kI, double kD, double kV, double kA) {
        this.kP = kP;
        this.kI = kI;
        this.kV = kV;
        this.kD = kD;
        this.kA = kA;
    }

    public void setLoopTime(double dt) {
        this.dt = dt;
    }

    public void resetController() {
        kP = 0;
        kI = 0;
        kD = 0;
        kV = 0;
        kA = 0;
        curr_segment = 0;
    }

    public double calculateFeedForward(double currVel, double currAccel) {
        return kV * currVel + kA * currAccel;
    }

    public double calculateFeedBack(double currPos, double setpointPos, double setpointVel) {
        error = setpointPos - currPos;
        sumError += error;
        changeError = (prevError - error)/dt - setpointVel;
        PID = (kP * error) + (kI * sumError) + (kD * changeError);
        prevError = error;
        return -PID; //PID is backwards
    }

    public double updateCalculations(double currPos) {
        if (!isFinished()){
            Trajectory.Point point = trajectory.getCurrPoint(curr_segment);
            feedForwardValue = calculateFeedForward(point.getVel(), point.getAcc());
            feedBackValue = calculateFeedBack(point.getPos(), currPos, point.getVel());
            System.out.println(PID);
            output = feedBackValue + feedForwardValue;
            curr_segment++;
            if (output > 1) {
                output = 1;
            }
            else if (output < -1) {
                output = -1;
            }

            return output;
        }
        return 0;
    }

    public void resetTrajectory(){
        curr_segment = 0;
    }

    public boolean isFinished() {
        return curr_segment >= trajectory.getLength();
    }

    public void configureDistanceTrajectory(double startVel, double endVel, double distance){
        TrajectoryGenerator trajectoryGenerator = new TrajectoryGenerator(Constants.kPercentOutputMaxA, Constants.kPercentOutputCruiseVelocity, Constants.kControlLoopPeriod);
        this.trajectory = trajectoryGenerator.generateTrajectory(startVel, endVel, distance);
    }
}