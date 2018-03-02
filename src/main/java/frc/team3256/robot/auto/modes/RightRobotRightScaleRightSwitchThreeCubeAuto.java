package frc.team3256.robot.auto.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;

public class RightRobotRightScaleRightSwitchThreeCubeAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain.getInstance().setBrake();
        DriveTrain.getInstance().resetGyro();
        double initTime = Timer.getFPGATimestamp();
        //runAction(new DeployIntakeAction());
        if (!Elevator.getInstance().isHomed()){
            //runAction(new AutoHomingAction());
        }
        double currVel = 24.0;
        DriveTrain.getInstance().setBrake();
        runAction(new FollowTrajectoryAction(currVel,0.0,-200,0));//-292.65
        runAction(new WaitAction(3));
        DriveTrain.getInstance().setBrake();
        runAction(new FollowArcTrajectoryAction(0,0,10,-10, true));        DriveTrain.getInstance().setBrake();
        System.out.println("Total Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
    }
}
