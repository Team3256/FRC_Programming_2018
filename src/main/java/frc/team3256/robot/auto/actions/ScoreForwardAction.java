package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.ElevatorCarriage;

public class ScoreForwardAction extends RunOnceAction {
    @Override
    public void runOnce() {
        ElevatorCarriage.getInstance().setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_FORWARD);
    }
}
