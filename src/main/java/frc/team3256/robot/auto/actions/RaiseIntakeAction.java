package frc.team3256.robot.auto.actions;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.subsystems.Intake;

public class RaiseIntakeAction extends RunOnceAction {

    @Override
    public void runOnce() {
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_STOW);
    }
}
