package frc.team3256.robot.auto.modes.Center;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.Robot;
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
        if (!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        runAction(new DeployIntakeAction());
        double initTime = Timer.getFPGATimestamp();
        double currVel = 24.0;
        double targetAngle = -40.0;
        runAction(new DeployIntakeAction());
        runAction(new CloseCarriageAction());
        runAction(new FollowArcTrajectoryAction(currVel, 50, 40, targetAngle, false));
        System.out.println("Initial 45 degree Arc --------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 50, 60, targetAngle)); //radius 65
        System.out.println("30\" forward -----------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 50, 28, 0, false));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new WaitAction(0.25));
        runAction(new FollowTrajectoryAction(currVel, 20, 30, 0));
        //runAction(new RaiseElevatorSwitchAction());
        runAction(new FollowTrajectoryAction(currVel, 50, 3, 0));
        runAction(new ScoreForwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new FollowTrajectoryAction(currVel, 50, -12.0, 0));
        //runAction(new ElevatorIntakePositionAction());
        DriveTrain.getInstance().setBrake();
        System.out.println("TOTAL TIME: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}
