package frc.team3256.robot.auto.modes.Test;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.AutoHomingAction;

public class TestAutoHoming extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new AutoHomingAction());
    }
}
