package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;

public class TestWebappAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowTrajectoryAction(0, 130, 3,0));
        runAction(new FollowArcTrajectoryAction(110, 110, 20, 90));
        runAction(new FollowTrajectoryAction(130, 110, 20,90));
        runAction(new FollowArcTrajectoryAction(96, 96, 20, -90));
        runAction(new FollowTrajectoryAction(110, 96, 10, -90));
    }
}
