package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import org.junit.Test;

import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {

        Path p = new Path();
        p.addSegment(new Line(0, 0, 12, 12));

        PurePursuitTracker pursuit = new PurePursuitTracker(p);
        Translation lookaheadPoint = new Translation(9, 9);
        Translation robotCoordinates = new Translation(15, 3);
        Translation robotToLookAhead = new Translation(robotCoordinates, lookaheadPoint);
        PurePursuitTracker.Command arcToSegment = pursuit.update(lookaheadPoint, robotCoordinates);
        System.out.println("x dist: " + robotToLookAhead.x());
        System.out.println("dx: " + arcToSegment.delta.dx());
        System.out.println("dy:" + arcToSegment.delta.dy());
        System.out.println("dtheta: " + arcToSegment.delta.dtheta());
        //assertEquals(arcToSegment.delta.dtheta(), Math.sqrt(2)*3, kEpsilon);



        //robotCoordinates = new Translation(3, 15);
        //arcToSegment = pursuit.update(lookaheadPoint, robotCoordinates);
        //assertEquals(arcToSegment.getRadius(), Math.sqrt(2)*3, kEpsilon);
    }
}