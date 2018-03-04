package frc.team3256.robot.auto.modes.Test;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.TurnInPlaceAction;

public class TestTurnInPlaceAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new TurnInPlaceAction(45.0));
    }
}
