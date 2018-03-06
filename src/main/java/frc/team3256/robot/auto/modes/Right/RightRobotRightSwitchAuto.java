package frc.team3256.robot.auto.modes.Right;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

import java.util.ArrayList;
import java.util.Arrays;

public class RightRobotRightSwitchAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain.getInstance().setBrake();
        DriveTrain.getInstance().resetGyro();
        double initTime = Timer.getFPGATimestamp();

        if (!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        double currVel = 0.0;
        DriveTrain.getInstance().setBrake();
        runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(0,0.0,-160,0),
                new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));        runAction(new DeployIntakeAction());
        ArrayList<Action> actions = new ArrayList<>();
        currVel = DriveTrain.getInstance().getAverageVelocity();
        actions.add(new RaiseElevatorSwitchAction());
        actions.add(new FollowArcTrajectoryAction(currVel, 0.0, 10,-90,false));
        runAction(new ParallelAction(actions));
        runAction(new ScoreForwardAction());
        runAction(new WaitAction(1.0));
        runAction(new StopScoreAction());
        runAction(new ElevatorIntakePositionAction());
        DriveTrain.getInstance().setBrake();
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}
