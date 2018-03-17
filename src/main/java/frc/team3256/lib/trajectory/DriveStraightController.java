package frc.team3256.lib.trajectory;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.Util;
import frc.team3256.lib.control.PIDController;
import frc.team3256.robot.Constants;

public class DriveStraightController {

    private double kP, kI, kD, kV, kA, dt;
    private double kStraightP, kStraightI, kStraightD;
    double PID, error, sumError, changeError = 0, prevError = 0;
    private double feedForwardValue, feedBackValue, adjustment;
    private PIDController pidController = new PIDController();

    public void setGains(double kP, double kI, double kD, double kV, double kA, double kStraightP, double kStraightI, double kStraightD) {
        this.kP = kP;
        this.kI = kI;
        this.kV = kV;
        this.kD = kD;
        this.kA = kA;
        this.kStraightP = kStraightP;
        this.kStraightI = kStraightI;
        this.kStraightD = kStraightD;
        pidController.setGains(kStraightP, kStraightI, kStraightD);
    }

    public void setLoopTime(double dt) {
        this.dt = dt;
    }

    public void resetController() {
        PID = 0;
        error = 0;
        sumError = 0;
        changeError = 0;
        prevError = 0;
    }

    private double calculateFeedForward(double currVel, double currAccel) {
        return kV * currVel + kA * currAccel;
    }

    private double calculateFeedBack(double setpointPos, double currPos, double setpointVel) {
        error = setpointPos - currPos;
        sumError += error;
        changeError = (prevError - error)/dt - setpointVel;
        PID = (kP * error) + (kI * sumError) + (kD * changeError);
        prevError = error;
        return PID;
    }

    public DrivePower update(double currPos, double currAngle, Trajectory.Point point, boolean reversed) {
        feedForwardValue = calculateFeedForward(point.getVel(), point.getAcc());
        if (reversed){
            currPos *= -1;
            currAngle *= -1;
        }
        feedBackValue = calculateFeedBack(point.getPos(), currPos, point.getVel());
        //System.out.println(PID);

        double leftOutput = feedBackValue + feedForwardValue;
        double rightOutput = feedBackValue + feedForwardValue;
        adjustment = -pidController.update(currAngle);
        leftOutput += adjustment;
        rightOutput -= adjustment;
        if (reversed){
            leftOutput *= -1.0;
            rightOutput *= -1.0;
        }
        leftOutput = Util.clip(leftOutput, -1, 1);
        rightOutput = Util.clip(rightOutput, -1, 1);
        return new DrivePower(leftOutput, rightOutput);
    }
}
