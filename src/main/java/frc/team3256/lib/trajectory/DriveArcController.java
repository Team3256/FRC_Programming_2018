package frc.team3256.lib.trajectory;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Util;
import frc.team3256.lib.control.PIDController;
import frc.team3256.robot.Constants;

public class DriveArcController {

    private double kP, kI, kD, kV, kA, dt;
    double PID, error, sumError, changeError, prevError = 0;
    private double feedForwardValueLeft, feedBackValueLeft, feedForwardValueRight, feedBackValueRight, rightOutput, leftOutput, adjustment;
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
    }

    public double calculateFeedForward(double currVel, double currAccel) {
        return kV * currVel + kA * currAccel;
    }

    public double calculateFeedBack(double setpointPos, double currPos, double setpointVel) {
        pidController.setGains(kGyroP,kGyroI,kGyroD);
        error = setpointPos - currPos;
        sumError += error;
        changeError = (prevError - error)/dt - setpointVel;
        PID = (kP * error) + (kI * sumError) + (kD * changeError);
        prevError = error;
        return PID;
    }

    public DrivePower updateCalculations(double currPosLeft, double currPosRight, double currAngle, Trajectory.Point leftPoint, Trajectory.Point rightPoint, boolean backTurn, double radius, boolean inverseTurn) {
        feedForwardValueLeft = calculateFeedForward(leftPoint.getVel(), leftPoint.getAcc());
        feedBackValueLeft = calculateFeedBack(leftPoint.getPos(), currPosLeft, leftPoint.getVel());
        feedForwardValueRight = calculateFeedForward(rightPoint.getVel(), rightPoint.getAcc());
        feedBackValueRight = calculateFeedBack(rightPoint.getPos(), currPosRight, rightPoint.getVel());
        rightOutput = feedBackValueRight + feedForwardValueRight;
        leftOutput = feedBackValueLeft + feedForwardValueLeft;
        double targetAngle = (((leftPoint.getPos()+ rightPoint.getPos())/2)/radius)*180/Math.PI;
        pidController.setTargetPosition(targetAngle);
        pidController.setMinMaxOutput(-1, 1);
        if (!inverseTurn) {
            adjustment = pidController.update(currAngle);
        }
        else {
            adjustment = pidController.update(-currAngle);
        }
        leftOutput += adjustment;
        rightOutput -= adjustment;
        rightOutput = Util.clip(rightOutput, -1, 1);
        leftOutput = Util.clip(leftOutput, -1, 1);
        if (backTurn){
            leftOutput *=-1;
            rightOutput *=-1;
        }
        return new DrivePower(leftOutput, rightOutput);

    }

}
