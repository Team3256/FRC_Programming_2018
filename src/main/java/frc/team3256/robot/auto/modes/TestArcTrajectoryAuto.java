package frc.team3256.robot.auto.modes;

import frc.team3256.lib.DrivePower;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.subsystems.DriveTrain;


public class TestArcTrajectoryAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowArcTrajectoryAction(108, 108, 72, 90));
        //double currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new FollowTrajectoryAction(currVel, 96, 10, 90));

        //runAction(new FollowArcTrajectoryAction(90, 90, 48, 90));
}
}
