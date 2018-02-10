package frc.team3256.lib.trajectory;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Util;
import frc.team3256.lib.control.PIDController;
import frc.team3256.robot.Constants;

public class DriveStraightController {

    private double kP, kI, kD, kV, kA, dt;
    double PID, error, sumError, changeError = 0, prevError = 0;
    private Trajectory trajectory;
    private int curr_segment = 0;
    private double leftOutput, rightOutput, feedForwardValue, feedBackValue, target, adj;
    PIDController pidController = new PIDController();

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

    private double calculateFeedForward(double currVel, double currAccel) {
        return kV * currVel + kA * currAccel;
    }

    private double calculateFeedBack(double currPos, double setpointPos, double setpointVel) {
        error = setpointPos - currPos;
        sumError += error;
        changeError = (prevError - error)/dt - setpointVel;
        PID = (kP * error) + (kI * sumError) + (kD * changeError);
        prevError = error;
        return -PID; //PID is backwards
    }

    public DrivePower updateCalculations(double currPos, double currAngle) {
        if (curr_segment == 0){
            target = currAngle;
        }
        if (!isFinished()){
            Trajectory.Point point = trajectory.getCurrPoint(curr_segment);
            feedForwardValue = calculateFeedForward(point.getVel(), point.getAcc());
            feedBackValue = calculateFeedBack(point.getPos(), currPos, point.getVel());
            System.out.println(PID);
            leftOutput = feedBackValue + feedForwardValue;
            rightOutput = feedBackValue + feedForwardValue;
            pidController.setTargetPosition(target);
            pidController.setMinMaxOutput(-1, 1);
            adj = pidController.update(currAngle);
            //leftOutput += adj;
            //rightOutput -= adj;
            curr_segment++;
            leftOutput = Util.clip(leftOutput, -1, 1);
            rightOutput = Util.clip(rightOutput, -1, 1);
            return new DrivePower(leftOutput, rightOutput);
        }
        return new DrivePower(0,0);
    }

    public void resetTrajectory(){
        curr_segment = 0;
    }

    public boolean isFinished() {
        return curr_segment >= trajectory.getLength();
    }

    public void configureDistanceTrajectory(double startVel, double endVel, double distance){
        TrajectoryGenerator trajectoryGenerator = new TrajectoryGenerator(Constants.kTrajectoryMaxAccel, Constants.kTrajectoryCruiseVelocity, Constants.kControlLoopPeriod);
        this.trajectory = trajectoryGenerator.generateTrajectory(startVel, endVel, distance);
    }
}
