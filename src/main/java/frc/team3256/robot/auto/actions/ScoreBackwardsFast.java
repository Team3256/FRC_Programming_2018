package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Carriage;

public class ScoreBackwardsFast extends RunOnceAction {
    @Override
    public void runOnce() {
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SMASH_BACKWARD);
    }
}
