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
        runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(currVel,30,-207,0),
                new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));
        DriveTrain.getInstance().setBrake();
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 20, 36, -90, true));
        runAction(new WaitAction(0.25));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 0.0, -136, -90));//-165
        DriveTrain.getInstance().resetGyro();
        runAction(new FollowArcTrajectoryAction(currVel, 20, 30, 97, true));
        runAction(new RaiseElevatorHighScaleAction());
        DriveTrain.getInstance().setBrake();
        runAction(new ScoreBackwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new WaitAction(0.15));
        runAction(new ElevatorIntakePositionAction());
        /*currVel = DriveTrain.getInstance().getAverageVelocity();
        DriveTrain.getInstance().resetGyro();
        runAction(new FollowTrajectoryAction(currVel, 0.0, -50, 90));//-165
        runAction(new RaiseElevatorHighScaleAction());
        runAction(new FollowTrajectoryAction(currVel, 0.0, -9, 95));//-165
        DriveTrain.getInstance().setBrake();
        runAction(new ScoreBackwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new WaitAction(0.75));
        DriveTrain.getInstance().resetGyro();
        runAction(new FollowTrajectoryAction(currVel, 0.0, 10, 0));
        runAction(new ElevatorIntakePositionAction());*/
        DriveTrain.getInstance().setBrake();
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}
