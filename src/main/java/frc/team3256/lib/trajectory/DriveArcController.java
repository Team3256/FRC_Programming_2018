package frc.team3256.lib.trajectory;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Util;
import frc.team3256.lib.control.PIDController;
import frc.team3256.robot.Constants;

public class DriveArcController {

    private double kP, kI, kD, kV, kA, dt;
    double PID, error, sumError, changeError = 0, prevError = 0;
    private int curr_segment;
    private Trajectory trajectoryCurveLead;
    private Trajectory trajectoryCurveFollow;
    private double feedForwardValueLead, feedBackValueLead, feedForwardValueFollow, feedBackValueFollow, followOutput, leadOutput, adjustment, targetAngle, angle;
    private PIDController pidController = new PIDController();
    private double kGyroP, kGyroI, kGyroD;

    public void setGains(double kP, double kI, double kD, double kV, double kA, double kGyroP, double kGyroI, double kGyroD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kV = kV;
        this.kA = kA;
        this.kGyroP = kGyroP;
        this.kGyroI = kGyroI;
        this.kGyroD = kGyroD;
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

    public double calculateFeedBack(double setpointPos, double currPos, double setpointVel) {
        error = setpointPos - currPos;
        sumError += error;
        changeError = (prevError - error)/dt - setpointVel;
        PID = (kP * error) + (kI * sumError) + (kD * changeError);
        prevError = error;
        return PID;
    }

    public DrivePower updateCalculations(double currPosLead, double currPosFollow, double currAngle) {
        if (curr_segment == 0) {
            pidController.setGains(kGyroP,kGyroI,kGyroD);
        }
        if (!isFinished()){
            Trajectory.Point leadPoint = trajectoryCurveLead.getCurrPoint(curr_segment);
            Trajectory.Point followPoint = trajectoryCurveFollow.getCurrPoint(curr_segment);
            feedForwardValueLead = calculateFeedForward(leadPoint.getVel(), leadPoint.getAcc());
            feedBackValueLead = calculateFeedBack(leadPoint.getPos(), currPosLead, leadPoint.getVel());
            feedForwardValueFollow = calculateFeedForward(followPoint.getVel(), leadPoint.getAcc());
            feedBackValueFollow = calculateFeedBack(followPoint.getPos(), currPosFollow, followPoint.getVel());
            System.out.println(PID);
            followOutput = feedBackValueFollow + feedForwardValueFollow;
            leadOutput = feedBackValueLead + feedForwardValueLead;
            targetAngle = (curr_segment * angle)/trajectoryCurveLead.getLength();
            pidController.setTargetPosition(targetAngle);
            pidController.setMinMaxOutput(-1, 1);
            adjustment = pidController.update(currAngle);
            leadOutput -= adjustment;
            followOutput += adjustment;
            curr_segment++;
            followOutput = Util.clip(followOutput, -1, 1);
            leadOutput = Util.clip(leadOutput, -1, 1);
            return new DrivePower(leadOutput, followOutput);
        }
        return new DrivePower(0,0);
    }

    public boolean isFinished() {
        return curr_segment >= trajectoryCurveLead.getLength();
    }

    public void configureArcTrajectory(double startVel, double endVel, double degrees, double turnRadius) {
        angle = degrees;
        TrajectoryCurveGenerator trajectoryCurveGenerator = new TrajectoryCurveGenerator(Constants.kCurveTrajectoryMaxAccel, Constants.kCurveTrajectoryCruiseVelocity, Constants.kControlLoopPeriod);
        trajectoryCurveGenerator.generateTrajectoryCurve(startVel, endVel, degrees, turnRadius);
        this.trajectoryCurveLead = trajectoryCurveGenerator.getLeadPath();
        this.trajectoryCurveFollow = trajectoryCurveGenerator.getFollowPath();
    }

    public void resetTrajectory(){
        curr_segment = 0;
    }
}
