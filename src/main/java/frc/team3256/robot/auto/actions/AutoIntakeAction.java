package frc.team3256.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class AutoIntakeAction implements Action {

    private double time;
    private double startTime;

    public AutoIntakeAction(double time){
        this.time = time;
    }

    @Override
    public boolean isFinished() {
        return Intake.getInstance().hasCube() || Timer.getFPGATimestamp() - startTime > time;
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        Intake.getInstance().setWantedState(Intake.WantedState.IDLE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_OPEN_IDLE);
        System.out.println("DONE!!!!!!!!!!!!!!");

    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_RECEIVE);
    }
}
