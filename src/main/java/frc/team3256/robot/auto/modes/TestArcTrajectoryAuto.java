package frc.team3256.robot.auto.modes;

import frc.team3256.lib.trajectory.DriveArcController;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.Action;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;

public class TestArcTrajectoryAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowArcTrajectoryAction(0, 0, 48, 90));
    }
}
