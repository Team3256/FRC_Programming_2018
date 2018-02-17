package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.subsystems.DriveTrain;

import java.util.Timer;

public class TestArcTrajectoryAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        double startTime = edu.wpi.first.wpilibj.Timer.getFPGATimestamp();
        runAction(new FollowTrajectoryAction(0, 130, 5, 0));
        double currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 110, 30, 90));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel,96,30, 90));
        System.out.println(edu.wpi.first.wpilibj.Timer.getFPGATimestamp()-startTime);

        //runAction(new FollowArcTrajectoryAction(90, 90, 48, 90));
}
}
