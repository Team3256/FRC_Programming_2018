package frc.team3256.lib.path;

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
}