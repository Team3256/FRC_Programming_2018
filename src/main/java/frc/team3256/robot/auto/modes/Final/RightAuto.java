package frc.team3256.robot.auto.modes.Final;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

import java.util.ArrayList;
import java.util.Arrays;

public class RightAuto extends AutoModeBase {

    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side scaleSide = GameDataAccessor.getScaleSide();
        GameDataAccessor.Side switchSide = GameDataAccessor.getSwitchSide();
        while(!GameDataAccessor.dataFound()){
            System.out.println("GAME DATA FOUND!!!!");
            scaleSide = GameDataAccessor.getScaleSide();
            System.out.println("SCALE SIDE:   " + scaleSide);
        }

        if(scaleSide == GameDataAccessor.Side.RIGHT){
            DriveTrain.getInstance().setBrake();
            DriveTrain.getInstance().resetGyro();
            double initTime = Timer.getFPGATimestamp();

            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            double currVel = 0.0;
            runAction(new CloseCarriageAction());
            DriveTrain.getInstance().setBrake();
            runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(currVel,0.0,-215,0),
                    new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));
            DriveTrain.getInstance().setBrake();
            Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new WaitAction(0.5));
            //runAction(new ParallelAction(Arrays.asList(new RaiseElevatorHighScaleAction(), new FollowTrajectoryAction(currVel, 0.0, -68,-27))));
            runAction(new FollowTrajectoryAction(currVel, 0.0, -68,-29));
            runAction(new RaiseElevatorHighScaleAction());
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new FollowTrajectoryAction(currVel, 0.0, -10,-29));
            DriveTrain.getInstance().setBrake();
            runAction(new ScoreBackwardAction());
            runAction(new WaitAction(0.75));
            runAction(new StopScoreAction());
            runAction(new WaitAction(0.75));
            currVel = DriveTrain.getInstance().getAverageVelocity();
            DriveTrain.getInstance().resetGyro();
            runAction(new FollowTrajectoryAction(currVel, 0.0, 15.0, 0));
            System.out.println("Gyro Angle: " + DriveTrain.getInstance().getAngle());
            runAction(new ElevatorIntakePositionAction());
            System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
            return;
        }

        else if (scaleSide == GameDataAccessor.Side.LEFT && switchSide == GameDataAccessor.Side.RIGHT){
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
        }
        else{
            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            runAction(new FollowTrajectoryAction(0, 0, -90, 0));
        }
    }
}
