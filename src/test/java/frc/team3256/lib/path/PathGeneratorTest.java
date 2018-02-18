package frc.team3256.lib.path;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PathGeneratorTest {

    private final double kEpsilon = 1E-9;

    @Test
    public void testWaypoint(){
        PathGenerator.Waypoint waypoint = new PathGenerator.Waypoint(0,0,0);
        assertEquals(waypoint.position.x(), 0, kEpsilon);
        assertEquals(waypoint.position.y(), 0, kEpsilon);
        assertEquals(waypoint.radius, 0, kEpsilon);
    }

    @Test
    public void testLineGenerator(){
        PathGenerator.Waypoint one = new PathGenerator.Waypoint(1, 3, 0);
        PathGenerator.Waypoint two = new PathGenerator.Waypoint(4, 10, 0);
        PathGenerator.LineGenerator lineGenerator = new PathGenerator.LineGenerator(one, two);
        assertEquals(lineGenerator.slope.x(), 3, kEpsilon);
        assertEquals(lineGenerator.slope.y(), 7, kEpsilon);
        assertEquals(lineGenerator.start.x(), 1, kEpsilon);
        assertEquals(lineGenerator.start.y(), 3, kEpsilon);
        assertEquals(lineGenerator.end.x(), 4, kEpsilon);
        assertEquals(lineGenerator.end.y(), 10, kEpsilon);
        one = new PathGenerator.Waypoint(0, 0, 0);
        two = new PathGenerator.Waypoint(0, 10, 1);
        lineGenerator = new PathGenerator.LineGenerator(one, two);
        assertEquals(lineGenerator.slope.x(), 0, kEpsilon);
        assertEquals(lineGenerator.slope.y(), 10, kEpsilon);
        assertEquals(lineGenerator.start.x(), 0, kEpsilon);
        assertEquals(lineGenerator.start.y(), 0, kEpsilon);
        assertEquals(lineGenerator.end.x(), 0, kEpsilon);
        //Line end is 9, due to the non-zero radius (the extra 1 unit is part of the arc)
        assertEquals(lineGenerator.end.y(), 9, kEpsilon);

    }

    @Test
    public void testArcGenerator(){
        PathGenerator.Waypoint one = new PathGenerator.Waypoint(0, 0, 0);
        PathGenerator.Waypoint two = new PathGenerator.Waypoint(0, 10, 5);
        PathGenerator.Waypoint three = new PathGenerator.Waypoint(10, 10, 0);
        PathGenerator.ArcGenerator arcGenerator = new PathGenerator.ArcGenerator(one, two, three);
        assertEquals(arcGenerator.a.start.x(), 0, kEpsilon);
        assertEquals(arcGenerator.a.start.y(), 0, kEpsilon);
        assertEquals(arcGenerator.a.end.x(), 0, kEpsilon);
        //End of the first line is (0, 5) instead of (0, 10) due to the 5 unit radius
        assertEquals(arcGenerator.a.end.y(), 5, kEpsilon);
        //Start of second line is (5, 10) instead of the (0, 10) due to the 5 unit radius
        assertEquals(arcGenerator.b.start.x(), 5, kEpsilon);
        assertEquals(arcGenerator.b.start.y(), 10, kEpsilon);
        assertEquals(arcGenerator.radius, 5, kEpsilon);
        assertEquals(arcGenerator.center.x(), 5, kEpsilon);
        assertEquals(arcGenerator.center.y(), 5, kEpsilon);
    }

    @Test
    public void testGenerator() {

        PathGenerator.Waypoint one = new PathGenerator.Waypoint(0, 0, 0, 0);
        PathGenerator.Waypoint two = new PathGenerator.Waypoint(0, 10, 4, 20);
        PathGenerator.Waypoint three = new PathGenerator.Waypoint(10, 10, 4, 40);
        PathGenerator.Waypoint four = new PathGenerator.Waypoint(10, 0, 0, 0);
        List<PathGenerator.Waypoint> waypointList = Arrays.asList(one, two, three, four);
        Path path = PathGenerator.generate(waypointList);
        for (Segment s : path.segments) {
            System.out.println(s.goalVel);
        }
        assertEquals(path.getLength(), 5, kEpsilon);
    }


}