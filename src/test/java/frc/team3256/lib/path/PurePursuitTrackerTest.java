package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import org.junit.Test;
import org.opencv.core.Mat;

import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {

        Path p = new Path();
        p.addSegment(new Line(0, 0, 24, 24));
        p.addSegment(new Arc(24, 24, 48,24, 36, 12));

        PurePursuitTracker pursuit = new PurePursuitTracker(p);
        Translation lookaheadPoint = new Translation(9, 9);
        Translation robotCoordinates = new Translation(15, 3);
        PurePursuitTracker.Command command = pursuit.update(lookaheadPoint, robotCoordinates);
        assertEquals(command.delta.dx(), 0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * 3.0 * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), -Math.PI, kEpsilon);



        robotCoordinates = new Translation(9, 3);
        command = pursuit.update(lookaheadPoint, robotCoordinates);
        assertEquals(command.delta.dx(), 0.0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * 1.5 * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), -Math.PI / 2.0, kEpsilon);


        robotCoordinates = new Translation(3, 9);
        command = pursuit.update(lookaheadPoint, robotCoordinates);
        assertEquals(command.delta.dx(), 0.0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * 1.5 * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), - Math.PI / 2.0, kEpsilon);

        robotCoordinates = new Translation(48, 0);
        lookaheadPoint = new Translation(24, 24);
        command = pursuit.update(lookaheadPoint, robotCoordinates);

        robotCoordinates = new Translation(48,28);
        lookaheadPoint = new Translation(48,24);
        command = pursuit.update(lookaheadPoint,robotCoordinates);
        assertEquals(command.delta.dx(), 0.0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), - Math.PI / 2.0, kEpsilon);

    }
}