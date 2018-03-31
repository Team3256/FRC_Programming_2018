package frc.team3256.robot.auto.modes.Final;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;

import java.util.ArrayList;
import java.util.Arrays;

public class CrossBaselineBackwardAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side switchSide = GameDataAccessor.getSwitchSide();
        while(!GameDataAccessor.dataFound()){
            System.out.println("GAME DATA FOUND!!!!");
        }
        if (!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        if (switchSide == GameDataAccessor.Side.RIGHT){
            DriveTrain.getInstance().setBrake();
            DriveTrain.getInstance().resetGyro();
            double initTime = Timer.getFPGATimestamp();

            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            double currVel = 0.0;
            DriveTrain.getInstance().setBrake();
            runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(currVel,0.0,-160,0),
                    new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));        runAction(new DeployIntakeAction());
            ArrayList<Action> actions = new ArrayList<>();
            currVel = DriveTrain.getInstance().getAverageVelocity();
            actions.add(new RaiseElevatorSwitchAction());
            actions.add(new FollowArcTrajectoryAction(currVel, 0.0, 15,-90,false));
            runAction(new ParallelAction(actions));
            runAction(new ScoreForwardAction());
            runAction(new WaitAction(1.0));
            runAction(new StopScoreAction());
            runAction(new ElevatorIntakePositionAction());
            DriveTrain.getInstance().setBrake();
            return;
        }
        else {
            runAction(new FollowTrajectoryAction(0, 0, -150, 0));
            return;
        }
    }
}
