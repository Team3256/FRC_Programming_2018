package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Elevator;

public class ElevatorIntakePositionAction extends RunOnceAction{
    @Override
    public void runOnce() {
        Elevator.getInstance().setWantedState(Elevator.WantedState.WANTS_TO_INTAKE_POS);
    }
}
