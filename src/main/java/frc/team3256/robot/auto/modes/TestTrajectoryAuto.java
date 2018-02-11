package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.auto.actions.WaitAction;
import frc.team3256.robot.subsystems.DriveTrain;

public class TestTrajectoryAuto extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowTrajectoryAction(0, 0, 20));
        runAction(new WaitAction(3));
        runAction(new FollowTrajectoryAction(0,0,-20));
        System.out.println("Left: " + DriveTrain.getInstance().getLeftDistance());
        System.out.println("Right: " + DriveTrain.getInstance().getRightDistance());
        System.out.println("angle: " + DriveTrain.getInstance().getAngle().degrees());
    }
}
