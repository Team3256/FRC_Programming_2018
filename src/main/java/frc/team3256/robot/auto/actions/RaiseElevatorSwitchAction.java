package frc.team3256.robot.auto.actions;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.subsystems.Elevator;

public class RaiseElevatorSwitchAction extends RunOnceAction{

    @Override
    public void runOnce() {
        Elevator.getInstance().setWantedState(Elevator.WantedState.SWITCH);
    }
}
