package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import org.junit.Test;

import static org.junit.Assert.*;

public class LineTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testConstructor(){
        Line line = new Line(0, 0, 10, 10);
        assertTrue(line instanceof Segment);
        assertEquals(line.getType(), Segment.Type.LINE);
    }

    @Test
    public void testStart(){
        Line line = new Line(0, 0, 10, 10);
        assertEquals(line.getStartPoint().x(), 0.0, kEpsilon);
        assertEquals(line.getStartPoint().y(), 0.0, kEpsilon);
    }

    @Test
    public void testEnd(){
        Line line = new Line(0, 0, 10, 10);
        assertEquals(line.getEndPoint().x(), 10.0, kEpsilon);
        assertEquals(line.getEndPoint().y(), 10.0, kEpsilon);
    }

    @Test
    public void testSlope(){
        Line line = new Line(2, 5, 9, 4);
        assertEquals(line.getSlope().x(), 7.0, kEpsilon);
        assertEquals(line.getSlope().y(), -1.0, kEpsilon);
        assertEquals(line.getSlope().norm(), Math.sqrt(50.0), kEpsilon);
    }

    @Test
    public void testLength(){
        Line line = new Line(2, 5, 9, 4);
        assertEquals(line.getLength(), Math.sqrt(50.0), kEpsilon);
    }

    @Test
    public void testClosestPoint(){
        Line line = new Line(0, 0, 10, 10);
        Translation currentPosition = new Translation(-1, -1);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 0, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 0, kEpsilon);
        currentPosition = new Translation(0, 10);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 5.0, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 5.0, kEpsilon);
        currentPosition = new Translation(3, 3);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 3.0, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 3.0, kEpsilon);
        currentPosition = new Translation(11, 11);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 10.0, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 10.0, kEpsilon);

        line = new Line(1, 1, 1, 16);
        currentPosition = new Translation(0, 0);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 1, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 1, kEpsilon);
        currentPosition = new Translation(3, 6);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 1, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 6, kEpsilon);
        currentPosition = new Translation(1000, 1000);
        assertEquals(line.getClosestPointOnSegment(currentPosition).x(), 1, kEpsilon);
        assertEquals(line.getClosestPointOnSegment(currentPosition).y(), 16, kEpsilon);
    }

    @Test
    public void testLookAheadPoint() {
        Line line = new Line(-1, -1, 5, 5);
        Translation currPos = new Translation(4, -2);
        Translation closestPoint = line.getClosestPointOnSegment(currPos);
        assertEquals(line.getCurrDistanceTraveled(closestPoint), 2*Math.sqrt(2), kEpsilon);
        assertEquals(line.getLookAheadPoint(3*Math.sqrt(2), closestPoint).x(), 4, kEpsilon);
        assertEquals(line.getLookAheadPoint(3*Math.sqrt(2), closestPoint).y(), 4, kEpsilon);

        line = new Line(0, 0, 0, 3);
        currPos = new Translation(1, 1);
        closestPoint = line.getClosestPointOnSegment(currPos);
        double distanceTraveled = line.getCurrDistanceTraveled(closestPoint);
        assertEquals(distanceTraveled, 1, kEpsilon);
        double remainingDistance = line.getRemainingDistance(closestPoint);
        assertEquals(remainingDistance, line.getLength()-distanceTraveled, kEpsilon);
        Translation lookaheadPoint = line.getLookAheadPoint(2, closestPoint);
        assertEquals(lookaheadPoint.x(), 0, kEpsilon);
        assertEquals(lookaheadPoint.y(), 3, kEpsilon);
    }
}