package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class StopIntakeAction extends RunOnceAction {
    @Override
    public void runOnce() {
        Intake.getInstance().setWantedState(Intake.WantedState.IDLE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_OPEN_IDLE);
    }
}
