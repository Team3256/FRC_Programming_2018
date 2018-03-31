package frc.team3256.robot.auto.modes.Final;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.AutoHomingAction;
import frc.team3256.robot.auto.actions.WaitAction;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

public class DoNothingAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        if(!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        //Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_OPEN_FLOP);
    }
}
