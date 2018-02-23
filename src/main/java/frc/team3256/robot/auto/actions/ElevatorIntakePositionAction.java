package frc.team3256.robot.auto.actions;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import frc.team3256.robot.Constants;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.subsystems.Elevator;

public class ElevatorIntakePositionAction extends RunOnceAction{
    @Override
    public void runOnce() {
        Elevator.getInstance().setWantedState(Elevator.WantedState.INTAKE_POS);
    }
}
