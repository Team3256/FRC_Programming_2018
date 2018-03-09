package frc.team3256.robot.auto.modes.Final;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.gamedata.GameDataAccessor;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;

public class CenterSwitchAuto extends AutoModeBase{

    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side switchSide = GameDataAccessor.getSwitchSide();
        if (!GameDataAccessor.dataFound()){
            switchSide = GameDataAccessor.getSwitchSide();
        }
        if (switchSide == GameDataAccessor.Side.LEFT){
            DriveTrain.getInstance().setBrake();
            DriveTrain.getInstance().resetGyro();
            runAction(new DeployIntakeAction());
            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            runAction(new DeployIntakeAction());
            runAction(new CloseCarriageAction());
            DriveTrain.getInstance().setBrake();
            double initTime = Timer.getFPGATimestamp();
            double currVel = 0.0;
            double targetAngle = 45.0;
            System.out.println("Angle: " + DriveTrain.getInstance().getAngle());
            runAction(new FollowArcTrajectoryAction(currVel, 24, 42, targetAngle, false));
            currVel = DriveTrain.getInstance().getAverageVelocity();
            DriveTrain.getInstance().setBrake();
            runAction(new FollowTrajectoryAction(currVel, 10, 67, targetAngle)); //radius 65
            currVel = DriveTrain.getInstance().getAverageVelocity();
            //runAction(new FollowArcTrajectoryAction(currVel, 24.0, 20, 0, false));
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new WaitAction(0.75));
            runAction(new FollowTrajectoryAction(currVel, 0.0, 26, 0));
            runAction(new WaitAction(0.75));
            runAction(new RaiseElevatorSwitchAction());
            runAction(new FollowTrajectoryAction(currVel, 0.0, 3, 0));
            runAction(new ScoreForwardAction());
            runAction(new WaitAction(0.75));
            runAction(new StopScoreAction());
            runAction(new FollowTrajectoryAction(currVel, 0, -12.0, 0));
            runAction(new ElevatorIntakePositionAction());
            DriveTrain.getInstance().setBrake();

        }
        else if (switchSide == GameDataAccessor.Side.RIGHT){
            DriveTrain.getInstance().setBrake();
            DriveTrain.getInstance().resetGyro();
            runAction(new DeployIntakeAction());
            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            double initTime = Timer.getFPGATimestamp();
            double currVel = 24.0;
            double targetAngle = -40.0;
            runAction(new DeployIntakeAction());
            runAction(new CloseCarriageAction());
            runAction(new FollowArcTrajectoryAction(currVel, 24, 40, targetAngle, false));
            System.out.println("Initial 45 degree Arc --------------");
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new FollowTrajectoryAction(currVel, 24, 60, targetAngle)); //radius 65
            System.out.println("30\" forward -----------------");
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new FollowArcTrajectoryAction(currVel, 10, 28, 0, false));
            currVel = DriveTrain.getInstance().getAverageVelocity();
            runAction(new WaitAction(0.75));
            runAction(new FollowTrajectoryAction(currVel, 0, 30, 0));
            runAction(new WaitAction(0.75));
            runAction(new RaiseElevatorSwitchAction());
            runAction(new FollowTrajectoryAction(currVel, 0.0, 3, 0));
            runAction(new ScoreForwardAction());
            runAction(new WaitAction(0.75));
            runAction(new StopScoreAction());
            runAction(new FollowTrajectoryAction(currVel, 0, -12.0, 0));
            runAction(new ElevatorIntakePositionAction());
            DriveTrain.getInstance().setBrake();
        }
        else{
            if (!Elevator.getInstance().isHomed()){
                runAction(new AutoHomingAction());
            }
            runAction(new FollowTrajectoryAction(0, 0, 90, 0));
        }
    }
}
