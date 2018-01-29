package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import org.junit.Test;

import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {

        PurePursuitTracker pursuit = new PurePursuitTracker();
        Line line = new Line(0, 0, 12, 12);
        Translation lookaheadPoint = new Translation(9, 9);
        Translation robotCoordinates = new Translation(15, 3);
        Arc arcToSegment = pursuit.getArcToSegment(line, lookaheadPoint, robotCoordinates);
        assertEquals(arcToSegment.getRadius(), Math.sqrt(2)*3, kEpsilon);

        robotCoordinates = new Translation(3, 15);
        arcToSegment = pursuit.getArcToSegment(line, lookaheadPoint, robotCoordinates);
        assertEquals(arcToSegment.getRadius(), Math.sqrt(2)*3, kEpsilon);
    }
}