package frc.team3256.robot.auto.modes.Final;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.AutoHomingAction;
import frc.team3256.robot.auto.actions.FollowTrajectoryAction;
import frc.team3256.robot.subsystems.Elevator;

public class CrossBaselineBackwardAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        if (!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        runAction(new FollowTrajectoryAction(0, 0, -90, 0));

    }
}
