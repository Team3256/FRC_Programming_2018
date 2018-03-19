package frc.team3256.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.subsystems.Carriage;

public class ScoreForwardSingleAction implements Action{

    private double duration;
    private double starttime;

    public ScoreForwardSingleAction(double duration) {
        this.duration = duration;
    }

    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() > (starttime + duration);
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
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SCORE_FORWARD_AUTO);
        starttime = Timer.getFPGATimestamp();
    }
}
