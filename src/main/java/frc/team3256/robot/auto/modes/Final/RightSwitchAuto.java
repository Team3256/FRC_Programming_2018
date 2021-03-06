package frc.team3256.robot.auto.modes.Final;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

import java.util.ArrayList;
import java.util.Arrays;

public class RightSwitchAuto extends AutoModeBase {

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
            runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(currVel,60,-215,0),
                    new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));
            runAction(new ParallelAction(Arrays.asList(new FollowArcTrajectoryAction(60, 0, 100, -39, true), new RaiseElevatorHighScaleAction())));
            runAction(new ScoreBackwardAction());
            runAction(new WaitAction(0.75));
            runAction(new StopScoreAction());
            runAction(new ParallelAction(Arrays.asList(new FollowArcTrajectoryAction(0,48,65,-35,false), new ElevatorIntakePositionAction())));
            runAction(new FollowArcTrajectoryAction(48,0,20,1,false));
            SmartDashboard.putBoolean("neural_network", true);
            runAction(new WaitAction(0.5));
            System.out.println("Angle: " + DriveTrain.getInstance().getCubeOffsetAngle());
            runAction(new FollowTrajectoryAction(0.0,0.0,25,DriveTrain.getInstance().getCubeOffsetAngle()+DriveTrain.getInstance().getAngle().degrees()));
            SmartDashboard.putBoolean("neural_network", false);
            //New Intake Stuff
            runAction(new AutoIntakeAction(1.0));
            runAction(new AutoIntakeTimeAction(0.2));
            runAction(new WaitAction(0.5));
            if (!Intake.getInstance().hasCube()){
                runAction(new AutoExhaustAction());
                runAction(new WaitAction(0.1));
                runAction(new StopIntakeAction());
                runAction(new AutoIntakeAction(1.0));
                runAction(new AutoIntakeTimeAction(0.2));
            }
            Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
            //Intake end stuff
            if (Intake.getInstance().hasCube()) {
                runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(0.0,0.0,-50.0, DriveTrain.getInstance().getAngle().degrees()), new RaiseElevatorHighScaleAction())));
                //runAction(new RaiseElevatorHighScaleAction());
                runAction(new ScoreBackwardsFast());
                runAction(new WaitAction(0.75));
                runAction(new StopScoreAction());
                runAction(new ElevatorIntakePositionAction());
                System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
                return;
            }
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
        }
        else{
            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            runAction(new FollowTrajectoryAction(0, 0, -150, 0));
        }
    }
}
