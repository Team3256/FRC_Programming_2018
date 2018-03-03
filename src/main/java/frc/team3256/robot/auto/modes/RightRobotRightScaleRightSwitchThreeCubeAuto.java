package frc.team3256.robot.auto.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

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
        runAction(new FollowTrajectoryAction(currVel,0.0,-215,0));//-292.65
        runAction(new DeployIntakeAction());
        runAction(new WaitAction(0.5));
        DriveTrain.getInstance().setBrake();
        //currVel = DriveTrain.getInstance().getAverageVelocity();
        //runAction(new FollowArcTrajectoryAction(currVel,0,10,-8, true));
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        runAction(new RaiseElevatorHighScaleAction());
        currVel = DriveTrain.getInstance().getAverageVelocity();
        runAction(new FollowTrajectoryAction(currVel, 0.0, -71, -23));
        DriveTrain.getInstance().setBrake();
        runAction(new ScoreBackwardAction());
        runAction(new WaitAction(1.0));
        runAction(new StopScoreAction());
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}
