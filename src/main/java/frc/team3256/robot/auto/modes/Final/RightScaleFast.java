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

public class RightScaleFast extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side scaleSide = GameDataAccessor.getScaleSide();
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
            runAction(new ParallelAction(Arrays.asList(new FollowArcTrajectoryAction(0,48,65,-37,false), new ElevatorIntakePositionAction())));
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
            runAction(new RaiseElevatorLowScaleAction());
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
        }
                }
                }
