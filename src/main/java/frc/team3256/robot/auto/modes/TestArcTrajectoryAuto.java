package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.subsystems.DriveTrain;


public class TestArcTrajectoryAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new FollowArcTrajectoryAction(0, 96, 25, -30, false));
        System.out.println("Gyro Angle   " + DriveTrain.getInstance().getAngle());
        //double currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new FollowTrajectoryAction(currVel, 96, 10, 90));

        //runAction(new FollowArcTrajectoryAction(90, 90, 48, 90));
}
}
