package frc.team3256.lib.trajectory;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrajectoryTest {

    @Test
    public void testPoint(){
        Trajectory.Point point = new Trajectory.Point(1, 1, 1, 1);
        Trajectory.Point point2 = new Trajectory.Point(1, 1, 1, 1);
        Trajectory trajectory = new Trajectory(2);
        trajectory.addPoint(0, point);
        trajectory.addPoint(1, point2);
        assertEquals(trajectory.points.size(),2);

    }

}