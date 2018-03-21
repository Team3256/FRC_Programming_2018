package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class AutoIntakeAction extends RunOnceAction {
    @Override
    public void runOnce() {
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_RECEIVE);
    }
}
