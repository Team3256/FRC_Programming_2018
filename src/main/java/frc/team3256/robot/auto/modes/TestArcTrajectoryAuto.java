package frc.team3256.robot.auto.modes;

import frc.team3256.lib.trajectory.DriveArcController;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.Action;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.auto.actions.WaitAction;

public class TestArcTrajectoryAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowArcTrajectoryAction(0, 12, 48, 90));
        runAction(new FollowTrajectoryAction(12,0,15, 90));
    }
}
