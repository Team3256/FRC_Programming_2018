package frc.team3256.robot.auto.modes.Test;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Intake;

public class IntakeTestAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new AutoIntakeAction(1.0));
        runAction(new AutoIntakeTimeAction(0.2));
        runAction(new WaitAction(0.5));
        if (!Intake.getInstance().hasCube()){
            runAction(new AutoExhaustAction());
            runAction(new WaitAction(0.1));
            runAction(new StopIntakeAction());
            runAction(new AutoIntakeAction(1.0));
            runAction(new AutoIntakeTimeAction(0.2));
        }
        Carriage.getInstance().setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
    }
}
