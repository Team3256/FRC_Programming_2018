package frc.team3256.robot.auto.modes.Right;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;

import java.util.Arrays;

public class RightRobotLeftScaleRightSwitchThreeCubeAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain.getInstance().setBrake();
        DriveTrain.getInstance().resetGyro();
        double initTime = Timer.getFPGATimestamp();

        if (!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        double currVel = 0.0;
        runAction(new CloseCarriageAction());
        DriveTrain.getInstance().setBrake();
        runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(currVel,0.0,-210,0),
                new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));
        DriveTrain.getInstance().setBrake();
        runAction(new WaitAction(2.0));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 0.0, -50, -90));//-165
        DriveTrain.getInstance().setBrake();
    }
}
