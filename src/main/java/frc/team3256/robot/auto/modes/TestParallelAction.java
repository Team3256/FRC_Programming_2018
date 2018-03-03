package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.*;
import frc.team3256.robot.subsystems.DriveTrain;

import java.util.ArrayList;
import java.util.Arrays;

public class TestParallelAction extends AutoModeBase{
    @Override
    protected void routine() throws AutoModeEndedException {
        runAction(new ParallelAction(Arrays.asList(new FollowTrajectoryAction(0,0.0,-160,0),
                new SeriesAction(Arrays.asList(new WaitAction(2.0), new DeployIntakeAction())))));
    }
}

