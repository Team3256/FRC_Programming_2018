package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Elevator;

public class RaiseElevatorLowScaleAction extends RunOnceAction{
    @Override
    public void runOnce() {
        Elevator.getInstance().setWantedState(Elevator.WantedState.WANTS_TO_HIGH_SCALE_POS);
    }
}
