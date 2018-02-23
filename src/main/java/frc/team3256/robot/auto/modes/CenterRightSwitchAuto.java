package frc.team3256.robot.auto.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.subsystems.DriveTrain;

public class CenterRightSwitchAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain.getInstance().setBrake();
        double initTime = Timer.getFPGATimestamp();
        double currVel = 60.0;
        runAction(new FollowArcTrajectoryAction(currVel, 60, 40, -30, false));
        System.out.println("Initial 45 degree Arc --------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 60, 60, -30)); //radius 65
        System.out.println("30\" forward -----------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 60, 25, 0, false));
        System.out.println("Gyro Angle:   " + DriveTrain.getInstance().getAngle().degrees());
        DriveTrain.getInstance().setBrake();
        /*currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 24, 20, 0));*/
        //DriveTrain.getInstance().setBrake();
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}
