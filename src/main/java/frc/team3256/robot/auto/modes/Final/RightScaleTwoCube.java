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

import java.util.Arrays;

public class RightScaleTwoCube extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side scaleSide = GameDataAccessor.getScaleSide();
        while(!GameDataAccessor.dataFound()){
        System.out.println("GAME DATA FOUND!!!!");
        scaleSide = GameDataAccessor.getScaleSide();
        System.out.println("SCALE SIDE:   " + scaleSide);
        }

        if(scaleSide == GameDataAccessor.Side.RIGHT){
            SmartDashboard.putBoolean("neural_network", false);
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
            runAction(new ParallelAction(Arrays.asList(new FollowArcTrajectoryAction(0,48,65,-32,false), new ElevatorIntakePositionAction())));
            runAction(new FollowArcTrajectoryAction(48,0,20,1,false));
            runAction(new WaitAction(0.5));
            //System.out.println("Angle: " + DriveTrain.getInstance().getCubeOffsetAngle());
            SmartDashboard.putBoolean("neural_network", true);
            runAction(new WaitAction(1.5));
            runAction(new AutoAlignAction());
            //runAction(new FollowTrajectoryAction(0.0,0.0,25,DriveTrain.getInstance().getCubeOffsetAngle()+DriveTrain.getInstance().getAngle().degrees()));
            SmartDashboard.putBoolean("neural_network", false);
            //New Intake Stuff
            runAction(new WaitAction(999999999));
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

        else if (scaleSide == GameDataAccessor.Side.LEFT){
            DriveTrain.getInstance().setBrake();
            DriveTrain.getInstance().resetGyro();

            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            double currVel = 0.0;
            runAction(new CloseCarriageAction());
            DriveTrain.getInstance().setBrake();
            runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(currVel,30,-201,0), //-207
                    new SeriesAction(Arrays.asList(new WaitAction(1.0), new DeployIntakeAction())))));
            DriveTrain.getInstance().setBrake();
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new FollowArcTrajectoryAction(currVel, 20, 36, -90, true));
            runAction(new WaitAction(0.25));
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new FollowTrajectoryAction(currVel, 0.0, -142, -90));//-165 //-136
            DriveTrain.getInstance().resetGyro();
            runAction(new FollowArcTrajectoryAction(currVel, 20, 30, 102, true));
            runAction(new RaiseElevatorHighScaleAction());
            DriveTrain.getInstance().setBrake();
            runAction(new ScoreBackwardAction());
            runAction(new WaitAction(0.75));
            runAction(new StopScoreAction());
            runAction(new WaitAction(0.15));
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
