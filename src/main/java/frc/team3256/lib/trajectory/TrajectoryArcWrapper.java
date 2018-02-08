package frc.team3256.lib.trajectory;

public class TrajectoryArcWrapper {

    private double kP, kI, kD, kV, kA, dt;
    double PID, error, sumError, changeError = 0, prevError = 0;
    private int curr_segment;
    private Trajectory trajectoryCurveLead;
    private Trajectory trajectoryCurveFollow;
    private double output, feedForwardValue, feedBackValue;

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
        return PID;
    }

    public double updateCalculations(double currPos) {
        if (!isFinished()){
            Trajectory.Point point = trajectoryCurveLead.getCurrPoint(curr_segment);
            feedForwardValue = calculateFeedForward(point.getVel(), point.getAcc());
            feedBackValue = calculateFeedBack(point.getPos(), currPos, point.getVel());
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

    public boolean isFinished() {
        return curr_segment >= trajectoryCurveLead.getLength();
    }

    public void configureArcTrajectory(double startVel, double endVel, double degrees, double turnRadius) {
        TrajectoryCurveGenerator trajectoryCurveGenerator = new TrajectoryCurveGenerator();
        trajectoryCurveGenerator.generateTrajectoryCurve(startVel, endVel, degrees, turnRadius);
        this.trajectoryCurveLead = trajectoryCurveGenerator.getLeadPath();
        this.trajectoryCurveFollow = trajectoryCurveGenerator.getFollowPath();
    }
}

