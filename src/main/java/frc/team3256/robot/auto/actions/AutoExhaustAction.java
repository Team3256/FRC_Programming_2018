package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class AutoExhaustAction extends RunOnceAction {
    @Override
    public void runOnce() {
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_EXHAUST);
    }
}
