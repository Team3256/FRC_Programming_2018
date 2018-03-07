package frc.team3256.robot.auto.modes.Center;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;

public class CenterRightSwitchAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
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
        runAction(new FollowTrajectoryAction(currVel, 24, 50, targetAngle)); //radius 65
        System.out.println("30\" forward -----------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 10, 28, 0, false));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new WaitAction(0.75));
        runAction(new FollowTrajectoryAction(currVel, 0, 22, 0));
        runAction(new WaitAction(0.75));
        runAction(new RaiseElevatorSwitchAction());
        runAction(new ScoreForwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new FollowTrajectoryAction(currVel, 0, -12.0, 0));
        runAction(new ElevatorIntakePositionAction());
        DriveTrain.getInstance().setBrake();
        //DriveTrain.getInstance().setBrake();
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}