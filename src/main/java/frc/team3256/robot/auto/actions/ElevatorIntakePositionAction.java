package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Elevator;

public class ElevatorIntakePositionAction implements Action{

    @Override
    public boolean isFinished() {
        return Elevator.getInstance().atClosedLoopTarget();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {

    }

    @Override
    public void start() {
        Elevator.getInstance().setWantedState(Elevator.WantedState.WANTS_TO_INTAKE_POS);
    }
}
