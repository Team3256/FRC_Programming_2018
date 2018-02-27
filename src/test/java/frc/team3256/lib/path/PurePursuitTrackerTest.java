package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;
import frc.team3256.robot.Constants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {

        Path p = new Path();
        p.addSegment(new Line(0, 0, 24, 24, 120.0, 20.0, 250.0));
        p.addSegment(new Arc(24, 24, 48,24, 36, 24, 120.0, 20.0, 250.0));
        p.addSegment(new Line(48, 12, 72, 12, 10, 10, 15));


        PurePursuitTracker pursuit = new PurePursuitTracker();
        pursuit.setPath(p);
        pursuit.setLoopTime(Constants.kControlLoopPeriod);
        Translation robotCoordinates = new Translation(15, 3);
        Twist command = pursuit.update(robotCoordinates);

        assertEquals(command.dy(), 0, kEpsilon);
        assertEquals(command.dx(), 20.0 * Constants.kControlLoopPeriod, kEpsilon);
        assertEquals(command.dtheta(), 20.0 * Constants.kControlLoopPeriod / (6*Math.sqrt(2)), kEpsilon);

        robotCoordinates = new Translation(9, 3);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dy(), 0.0, kEpsilon);
        assertEquals(command.dx(), 2 * 20.0 * Constants.kControlLoopPeriod, kEpsilon);
        assertEquals(command.dtheta(), 2 * 20.0 * Constants.kControlLoopPeriod / (3* Math.sqrt(2)), kEpsilon);

        robotCoordinates = new Translation(3, 9);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dy(), 0.0, kEpsilon);
        assertEquals(command.dx(), 3 * 20.0 * Constants.kControlLoopPeriod, kEpsilon);
        assertEquals(command.dtheta(), -3 * 20.0 * Constants.kControlLoopPeriod / (3* Math.sqrt(2)), kEpsilon);

        robotCoordinates = new Translation(23.999, 23.999);
        pursuit.update(robotCoordinates);

        robotCoordinates = new Translation(48.2, 24);
        pursuit.update(robotCoordinates);

        robotCoordinates = new Translation(50, 5);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dy(), 0.0, kEpsilon);


    }
}