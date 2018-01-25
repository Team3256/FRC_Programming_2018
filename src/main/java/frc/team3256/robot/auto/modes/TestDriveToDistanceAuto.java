package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.Action;
import frc.team3256.robot.auto.actions.DriveToDistanceAction;
import frc.team3256.robot.auto.actions.WaitAction;

public class TestDriveToDistanceAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new DriveToDistanceAction(20));
        runAction(new WaitAction(0.5));
        runAction(new DriveToDistanceAction(100));
    }
            }
