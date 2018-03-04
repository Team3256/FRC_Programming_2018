package frc.team3256.robot.auto.modes.Final;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.AutoHomingAction;
import frc.team3256.robot.auto.actions.WaitAction;
import frc.team3256.robot.subsystems.Elevator;

public class DoNothingAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        if(!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
    }
}
