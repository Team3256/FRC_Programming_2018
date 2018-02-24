package frc.team3256.robot.auto.actions;

import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class IntakeAction implements Action {

    @Override
    public boolean isFinished() {
        return Intake.getInstance().hasCube();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
    }

    @Override
    public void start() {
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_OPEN);
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
    }
}
