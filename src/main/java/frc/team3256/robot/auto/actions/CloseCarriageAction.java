package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Carriage;

public class CloseCarriageAction extends RunOnceAction{
    @Override
    public void runOnce() {
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
    }
}
