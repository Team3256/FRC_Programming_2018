package frc.team3256.robot.auto.modes.Test;

import frc.team3256.lib.path.Path;
import frc.team3256.lib.path.PathGenerator;
import frc.team3256.robot.Constants;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.PurePursuitAction;

import java.util.ArrayList;
import java.util.Arrays;

public class TestPurePursuitAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        PathGenerator.Waypoint one = new PathGenerator.Waypoint(0, 0, 0, 0);
        PathGenerator.Waypoint two = new PathGenerator.Waypoint(12, 0, 0, 50);
        Path path = PathGenerator.generate(Arrays.asList(one, two), Constants.kPurePursuitMaxAccel, Constants.kPurePursuitMaxVel);
        runAction(new PurePursuitAction(path));
    }
}
