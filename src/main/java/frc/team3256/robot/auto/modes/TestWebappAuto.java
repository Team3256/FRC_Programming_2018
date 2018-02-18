package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.subsystems.DriveTrain;

public class TestWebappAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        double startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        runAction(new FollowTrajectoryAction(0, 110, 3, 0));
        double currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 80, 20, 90));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel,110,20, 90));
        //runAction(new WaitAction(1.5));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new WaitAction(2.0));
        runAction(new FollowArcTrajectoryAction(currVel, 80, 15, -90)); //replace start with currVel
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 50, 10, -90));
        System.out.println(edu.wpi.first.wpilibj.Timer.getFPGATimestamp()-startTime);

    }
}
