package frc.team3256.robot;

import frc.team3256.lib.Kinematics;
import frc.team3256.lib.Loop;
import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Twist;
import frc.team3256.robot.subsystems.DriveTrain;

public class PoseEstimator implements Loop {

    private static PoseEstimator instance;
    private Twist velocity;
    private RigidTransform pose;
    private RigidTransform prevPose;
    private double prevLeftDist = 0;
    private double prevRightDist = 0;
    private DriveTrain driveTrain = DriveTrain.getInstance();

    private PoseEstimator(){
        reset(new RigidTransform());
    }

    public static PoseEstimator getInstance(){
        return instance == null ? instance = new PoseEstimator() : instance;
    }

    public RigidTransform getPose() {
        return pose;
    }

    public Twist getVelocity() {
        return velocity;
    }

    public void reset(RigidTransform startingPose){
        velocity = new Twist();
        pose = startingPose;
        prevPose = new RigidTransform();
    }

    @Override
    public void init(double timestamp) {
        prevLeftDist = driveTrain.getLeftDistance();
        prevRightDist = driveTrain.getRightDistance();
    }

    @Override
    public void update(double timestamp) {
        double deltaLeftDist = driveTrain.getLeftDistance() - prevLeftDist;
        double deltaRightDist = driveTrain.getRightDistance() - prevRightDist;
        Rotation deltaHeading = prevPose.getRotation().inverse().rotate(driveTrain.getAngle());
        //Use encoders + gyro to determine our velocity
        velocity = Kinematics.forwardKinematics(deltaLeftDist, deltaRightDist,
               deltaHeading.radians());
        //use velocity to determine our pose
        pose = Kinematics.integrateForwardKinematics(prevPose, velocity);
        //update for next iteration
        prevLeftDist = driveTrain.getLeftDistance();
        prevRightDist = driveTrain.getRightDistance();
        prevPose = pose;
    }

    @Override
    public void end(double timestamp) {

    }

}
