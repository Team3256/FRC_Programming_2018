package frc.team3256.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class IntakeAction implements Action {

    private double startTime;

    @Override
    public boolean isFinished() {
        return Intake.getInstance().hasCube();
    }

    @Override
    public void update() {
        if ((Timer.getFPGATimestamp() - startTime) % 1.5 == 0){
            Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        }
        else {
            Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        }
    }

    @Override
    public void done() {
        Intake.getInstance().setWantedState(Intake.WantedState.IDLE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
    }

    @Override
    public void start() {
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_OPEN_IDLE);
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        startTime = Timer.getFPGATimestamp();
    }
}
