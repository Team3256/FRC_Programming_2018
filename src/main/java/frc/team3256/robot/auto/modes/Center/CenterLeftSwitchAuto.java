package frc.team3256.robot.auto.modes.Center;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;

public class CenterLeftSwitchAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
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
        runAction(new FollowArcTrajectoryAction(currVel, 50, 42, targetAngle, false));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        DriveTrain.getInstance().setBrake();
        runAction(new FollowTrajectoryAction(currVel, 20, 56, targetAngle)); //radius 65
        currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new FollowArcTrajectoryAction(currVel, 24.0, 20, 0, false));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new WaitAction(0.75));
        runAction(new FollowTrajectoryAction(currVel, 20, 26, 0));
        runAction(new WaitAction(0.75));
        //runAction(new RaiseElevatorSwitchAction());
        runAction(new FollowTrajectoryAction(currVel, 0.0, 3, 0));
        runAction(new ScoreForwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new FollowTrajectoryAction(currVel, 0, -12.0, 0));
        //runAction(new ElevatorIntakePositionAction());
        DriveTrain.getInstance().setBrake();
        System.out.println("TOTAL TIME: " + Double.toString(Timer.getFPGATimestamp() - initTime));
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
