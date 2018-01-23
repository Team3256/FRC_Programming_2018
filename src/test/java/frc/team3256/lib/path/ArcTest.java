package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArcTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testConstructor(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 10);
        assertTrue(arc instanceof Segment);
        assertEquals(arc.getType(), Segment.Type.ARC);

    }

    @Test
    public void testStart(){
        Arc arc = new Arc(0, 0,0, 0, 0, 0);
        assertEquals(arc.getStartPoint().x(), 0, kEpsilon);
        assertEquals(arc.getStartPoint().y(), 0, kEpsilon);
        arc = new Arc(5.4, 6.4, 0, 0, 0, 0);
        assertEquals(arc.getStartPoint().x(), 5.4, kEpsilon);
        assertEquals(arc.getStartPoint().y(), 6.4, kEpsilon);
    }

    @Test
    public void testEnd(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 0);
        assertEquals(arc.getEndPoint().x(), 10.0, kEpsilon);
        assertEquals(arc.getEndPoint().y(), 10.0, kEpsilon);
        arc = new Arc(0, 0, 6.7, 2.3, 0, 0);
        assertEquals(arc.getEndPoint().x(), 6.7, kEpsilon);
        assertEquals(arc.getEndPoint().y(), 2.3, kEpsilon);
    }

    @Test
    public void testCenter(){
        Arc arc = new Arc(0, 0, 0, 0, 10, 10);
        assertEquals(arc.getCenter().x(), 10.0, kEpsilon);
        assertEquals(arc.getCenter().y(), 10.0, kEpsilon);
        arc = new Arc(0, 0, 0, 0, 2.3, 4.9);
        assertEquals(arc.getCenter().x(), 2.3, kEpsilon);
        assertEquals(arc.getCenter().y(), 4.9, kEpsilon);
    }

    @Test
    public void testRadius(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 10);
        assertEquals(arc.getRadius(), 10.0, kEpsilon);
        arc = new Arc(2, 2, 4, 4, 4, 2);
        assertEquals(arc.getRadius(), 2, kEpsilon);
        arc = new Arc(2, 2, 5, 5, 2, 2);
        assertEquals(arc.getRadius(), Double.NaN, kEpsilon);
    }

    @Test
    public void testLength(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 10);
        assertEquals(arc.getLength(), 10.0*Math.PI/2.0, kEpsilon);
        arc = new Arc(2, 2, 5, 5, 2, 2);
        assertEquals(arc.getLength(), Double.NaN, kEpsilon);
    }

    @Test
    public void testCenterToStart(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 10);
        assertEquals(arc.getCenterToStart().x(), 0, kEpsilon);
        assertEquals(arc.getCenterToStart().y(), -10.0, kEpsilon);
        assertEquals(arc.getCenterToStart().norm(), 10.0, kEpsilon);
    }

    @Test
    public void testCenterToEnd(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 10);
        assertEquals(arc.getCenterToEnd().x(), 10, kEpsilon);
        assertEquals(arc.getCenterToEnd().y(), 0, kEpsilon);
        assertEquals(arc.getCenterToEnd().norm(), 10.0, kEpsilon);
    }

    @Test
    public void getClosestPointOnSegment(){
        Arc arc = new Arc(0, 0, 10, 10, 0, 10);
        Translation currPosition = new Translation(-1, -1);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), 0, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 0, kEpsilon);
        currPosition = new Translation(11, 11);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), 10, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 10, kEpsilon);
        currPosition = new Translation(5, 5);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), Math.sqrt(2)/2*10, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 10.0-Math.sqrt(2)/2*10, kEpsilon);
        currPosition = new Translation(0, 0);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), 0, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 0, kEpsilon);

        arc = new Arc(1, 1, 11, 11, 1, 11);
        currPosition = new Translation(0, 0);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), 1.0, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 1.0, kEpsilon);
        currPosition = new Translation(1000, 1000000);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), 11, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 11, kEpsilon);
        currPosition = new Translation(6, 6);
        assertEquals(arc.getClosestPointOnSegment(currPosition).x(), Math.sqrt(2)/2*10+1, kEpsilon);
        assertEquals(arc.getClosestPointOnSegment(currPosition).y(), 10-Math.sqrt(2)/2*10+1, kEpsilon);
    }

    @Test
    public void testLookAheadPoint() {
        Arc arc = new Arc(2, 0, -2, 0, 0, 0);
        Translation currPos = new Translation(Math.sqrt(2)/2, Math.sqrt(2)/2);
        Translation closestPoint = arc.getClosestPointOnSegment(currPos);
        assertEquals(closestPoint.x(), Math.sqrt(2), kEpsilon);
        assertEquals(closestPoint.y(), Math.sqrt(2), kEpsilon);

        assertEquals(arc.getCurrDistanceTraveled(closestPoint), Math.PI/2, kEpsilon);
        assertEquals(arc.getLookAheadPoint(2.0*15.0/180.0*Math.PI, closestPoint).x(), 1.0, kEpsilon);
        assertEquals(arc.getLookAheadPoint(2.0*15.0/180.0*Math.PI, closestPoint).y(), Math.sqrt(3), kEpsilon);


        arc = new Arc(2, 0, Math.sqrt(2), -Math.sqrt(2), 0, 0);
        currPos = new Translation(Math.sqrt(2)/2, Math.sqrt(2)/2);
        closestPoint = arc.getClosestPointOnSegment(currPos);
        assertEquals(closestPoint.x(), 2, kEpsilon);
        assertEquals(closestPoint.y(), 0, kEpsilon);

        arc = new Arc(2, 0, -2, 0, 0, 0);
        currPos = new Translation(-1, Math.sqrt(3));
        closestPoint = arc.getClosestPointOnSegment(currPos);
        assertEquals(arc.getCurrDistanceTraveled(closestPoint), 4*Math.PI/3, kEpsilon);
        assertEquals(arc.getLookAheadPoint(Math.PI/3, closestPoint).x(), -Math.sqrt(3), kEpsilon);
        assertEquals(arc.getLookAheadPoint(Math.PI/3, closestPoint).y(), 1, kEpsilon);

        arc = new Arc(0.0, 10.0, 5.0, 15.0, 5.0, 10.0);
        currPos = new Translation(2.0, 10.0);
        closestPoint = arc.getClosestPointOnSegment(currPos);
        assertEquals(closestPoint.x(), 0, kEpsilon);
        assertEquals(closestPoint.y(), 10.0, kEpsilon);

        assertEquals(arc.getLookAheadPoint(2.0, closestPoint).x(), 5.0-5.0*Math.cos(2.0/5.0), kEpsilon);
        assertEquals(arc.getLookAheadPoint(2.0, closestPoint).y(), 5.0*Math.sin(2.0/5.0), kEpsilon);

    }
}