package frc.team3256.lib.trajectory;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Util;
import frc.team3256.robot.Constants;

public class DriveArcController {

    private double kP, kI, kD, kV, kA, dt;
    double PID, error, sumError, changeError = 0, prevError = 0;
    private int curr_segment;
    private Trajectory trajectoryCurveLead;
    private Trajectory trajectoryCurveFollow;
    private double feedForwardValueLead, feedBackValueLead, feedForwardValueFollow, feedBackValueFollow, leftOutput, rightOutput;

    public void setGains(double kP, double kI, double kD, double kV, double kA) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kV = kV;
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
        return -PID;
    }

    public DrivePower updateCalculations(double currPosLead, double currPosFollow) {
        if (!isFinished()){
            Trajectory.Point leadPoints = trajectoryCurveLead.getCurrPoint(curr_segment);
            Trajectory.Point followPoints = trajectoryCurveFollow.getCurrPoint(curr_segment);
            feedForwardValueLead = calculateFeedForward(leadPoints.getVel(), leadPoints.getAcc());
            feedBackValueLead = calculateFeedBack(leadPoints.getPos(), currPosLead, leadPoints.getVel());
            feedForwardValueFollow = calculateFeedForward(followPoints.getVel(), leadPoints.getAcc());
            feedBackValueFollow = calculateFeedBack(followPoints.getPos(), currPosFollow, followPoints.getVel());
            System.out.println(PID);
            leftOutput = feedBackValueFollow + feedForwardValueFollow;
            rightOutput = feedBackValueLead + feedForwardValueLead;
            //leftOutput += adj;
            //rightOutput -= adj;
            curr_segment++;
            leftOutput = Util.clip(leftOutput, -1, 1);
            rightOutput = Util.clip(rightOutput, -1, 1);
            return new DrivePower(leftOutput, rightOutput);
        }
        return new DrivePower(0,0);
    }

    public boolean isFinished() {
        return curr_segment >= trajectoryCurveLead.getLength();
    }

    public void configureArcTrajectory(double startVel, double endVel, double degrees, double turnRadius) {
        TrajectoryCurveGenerator trajectoryCurveGenerator = new TrajectoryCurveGenerator(Constants.kCurveTrajectoryMaxAccel, Constants.kCurveTrajectoryCruiseVelocity, Constants.kControlLoopPeriod);
        trajectoryCurveGenerator.generateTrajectoryCurve(startVel, endVel, degrees, turnRadius);
        this.trajectoryCurveLead = trajectoryCurveGenerator.getLeadPath();
        this.trajectoryCurveFollow = trajectoryCurveGenerator.getFollowPath();
    }

    public void resetTrajectory(){
        curr_segment = 0;
    }
}

