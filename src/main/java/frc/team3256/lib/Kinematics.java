package frc.team3256.lib;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Twist;

/**
 * Kinematic functions for a differential drive robot
 */
public class Kinematics {

    private static final double kEpsilon = 1E-9;

    /**
     * Forward kinematics:
     *      Starting in the pose (x, y, theta) at time t, determine the pose at time t + delta_t (x', y', theta')
     *      given the robot's left and right wheel velocities.
     * Note: we don't take time into account here because we are assuming the control loop is running at a constant period.
     * @param currentPose current pose of the robot
     * @param deltaPos the change in (x, y, theta) of the robot over the past iteration
     * @return the new pose of the robot
     */
    public static RigidTransform forwardKinematics(RigidTransform currentPose, Twist deltaPos){
        return currentPose.transform(RigidTransform.exp(deltaPos));
    }

    public static RigidTransform forwardKinematics(RigidTransform currentPose, double left_delta, double right_delta, double theta_delta){
        double dx = (left_delta + right_delta)/2.0;
        //dy is 0, as differential drive robots don't move sideways
        Twist twist = new Twist(dx, 0.0, theta_delta);
        return forwardKinematics(currentPose, twist);
    }

    /**
     * Class representing a velocities (or delta in positions) in the left and right sides of a differential drive.
     */
    public static class DriveVelocity{

        public double left, right;

        public DriveVelocity(double left, double right){
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Inverse kinematics:
     *      Starting in the pose (x, y, theta) at time t, determine the left and right wheel velocities that the pose
     *      at time t + delta_t is pose (x', y', theta')
     *      In other words, given a delta_x and delta_theta, calculate the left and right wheel velocities (or delta_left/delta_right)
     *      needed to achieve that motion.
     * @param deltaPos
     * @return
     */
    public static DriveVelocity inverseKinematics(Twist deltaPos, double robotTrack){
        //If dtheta is 0, then we are not rotating, so our "velocities" are just dx
        if (Math.abs(deltaPos.dtheta()) < kEpsilon){
            return new DriveVelocity(deltaPos.dx(), deltaPos.dx());
        }
        double angularOffset =  robotTrack * deltaPos.dtheta() / 2.0;
        return new DriveVelocity(deltaPos.dx() - angularOffset, deltaPos.dx() + angularOffset);
    }
}
