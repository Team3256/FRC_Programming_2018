package frc.team3256.robot.auto.modes.Test;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.FollowArcTrajectoryAction;
import frc.team3256.robot.auto.actions.TurnInPlaceAction;
import frc.team3256.robot.subsystems.DriveTrain;

public class TestBackwardsTurn extends AutoModeBase{
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain.getInstance().setBrake();
        runAction(new TurnInPlaceAction(10));
        //runAction(new FollowArcTrajectoryAction(0,0,10,-10, true));
    }
}
