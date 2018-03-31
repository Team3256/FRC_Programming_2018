package frc.team3256.robot.auto.modes.Right;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

import java.util.Arrays;

public class RightRobotRightScaleRightSwitchThreeCubeAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
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
        currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new WaitAction(0.5));
        //runAction(new FollowTrajectoryAction(currVel, 0.0, -68,-29));
        runAction(new ParallelAction(Arrays.asList(new FollowArcTrajectoryAction(60, 0, 100, -39, true), new RaiseElevatorHighScaleAction())));
        runAction(new ScoreBackwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new ParallelAction(Arrays.asList(new FollowArcTrajectoryAction(0,48,35,-45,false), new ElevatorIntakePositionAction())));
        System.out.println("--------------------------------------");
        runAction(new FollowArcTrajectoryAction(48,0,45,1,false));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        /*runAction(new RaiseElevatorHighScaleAction());
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 0.0, -10,-29));
        DriveTrain.getInstance().setBrake();
        runAction(new ScoreBackwardAction());
        runAction(new WaitAction(0.75));
        runAction(new StopScoreAction());
        runAction(new WaitAction(0.25));
        currVel = DriveTrain.getInstance().getAverageVelocity();
        DriveTrain.getInstance().resetGyro();
        runAction(new FollowTrajectoryAction(currVel, 0.0, 15.0, 0));
        System.out.println("Gyro Angle: " + DriveTrain.getInstance().getAngle());
        runAction(new ElevatorIntakePositionAction());*/
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));

    }
}
