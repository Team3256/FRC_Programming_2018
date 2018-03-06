package frc.team3256.robot.auto.modes.Test;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.AutoHomingAction;
import frc.team3256.robot.auto.actions.ElevatorIntakePositionAction;
import frc.team3256.robot.auto.actions.RaiseElevatorHighScaleAction;
import frc.team3256.robot.auto.actions.WaitAction;
import frc.team3256.robot.subsystems.Elevator;

public class ElevatorTestAuto extends AutoModeBase{
    @Override
    protected void routine() throws AutoModeEndedException {
        if (!Elevator.getInstance().isHomed()){
            runAction(new AutoHomingAction());
        }
        double initTime = Timer.getFPGATimestamp();
        runAction(new RaiseElevatorHighScaleAction());
        System.out.println("Time: " + Double.toString(Timer.getFPGATimestamp() - initTime));
        runAction(new WaitAction(3.0));
        runAction(new ElevatorIntakePositionAction());
    }
}
