package frc.team3256.robot.auto.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;

public class CenterLeftSwitchAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain.getInstance().setBrake();
        double initTime = Timer.getFPGATimestamp();
        double currVel = 0.0;
        runAction(new DeployIntakeAction());
        double targetAngle = 45.0;
        runAction(new FollowArcTrajectoryAction(currVel, 36, 40, targetAngle, false));
        runAction(new RaiseElevatorSwitchAction());
        runAction(new WaitAction(0.6));
        System.out.println("Initial 45 degree Arc --------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 36, 65, targetAngle)); //radius 65
        System.out.println("30\" forward -----------------");
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 36, 25, 0, false));
        System.out.println("Gyro Angle:   " + DriveTrain.getInstance().getAngle().degrees());
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 0.0, 10, 0));
        runAction(new ScoreForwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new FollowTrajectoryAction(currVel, 0, -12.0, 0));
        runAction(new ElevatorIntakePositionAction());
        DriveTrain.getInstance().setBrake();
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}


/* working auto (not optimized)
double currVel = 0.0;
        runAction(new FollowTrajectoryAction(currVel, 70, 20,0));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 70, 25, 90, false)); //radius 65
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 110, 5,90));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowArcTrajectoryAction(currVel, 110, 14, -90, false));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 80, 26,-90));
        DriveTrain.getInstance().setBrake();
 */