package frc.team3256.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class AutoIntakeTimeAction implements Action{

    private double time;
    private double startTime;

    public AutoIntakeTimeAction(double time){
        this.time = time;
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime > time;
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        Intake.getInstance().setWantedState(Intake.WantedState.IDLE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_OPEN_IDLE);
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_RECEIVE);
    }
}
