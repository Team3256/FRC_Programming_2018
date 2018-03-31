package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Intake;

public class DeployIntakeAction extends RunOnceAction {
    @Override
    public void runOnce() {
        //Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
        //HACK
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_OPEN_FLOP);

    }
}
