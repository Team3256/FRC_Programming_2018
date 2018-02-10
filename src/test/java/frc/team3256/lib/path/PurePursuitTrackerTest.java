package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;
import frc.team3256.robot.Constants;
import org.junit.Test;

import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {

        Path p = new Path();
        p.addSegment(new Line(0, 0, 24, 24, 120.0, 20.0, 250.0));
        p.addSegment(new Arc(24, 24, 48,24, 36, 24));
        p.addSegment(new Line(48, 24, 70, 24, 10, 5, 15));

        PurePursuitTracker pursuit = new PurePursuitTracker(p, 1.0);
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

        robotCoordinates = new Translation(48, 24);
        pursuit.update(robotCoordinates);






        /*
        robotCoordinates = new Translation(30, 0);
        Twist pursuitUpdate = pursuit.update(robotCoordinates, 20);
        System.out.println(pursuitUpdate.delta);
        System.out.println(pursuitUpdate.vel);


        robotCoordinates = new Translation(14, 15);
        pursuitUpdate = pursuit.update(robotCoordinates, 0);
        System.out.println(pursuitUpdate.delta);
        System.out.println(pursuitUpdate.vel);


        robotCoordinates = new Translation(0, 30);
        pursuitUpdate = pursuit.update(robotCoordinates, 100);
        System.out.println(pursuitUpdate.delta);
        System.out.println(pursuitUpdate.vel);

        robotCoordinates = new Translation(0, 30);
        pursuitUpdate = pursuit.update(robotCoordinates, 200);
        System.out.println(pursuitUpdate.delta);
        System.out.println(pursuitUpdate.vel);

        robotCoordinates = new Translation(0, 100);
        pursuitUpdate = pursuit.update(robotCoordinates, 300);
        System.out.println(pursuitUpdate.delta);
        System.out.println(pursuitUpdate.vel);
        */

    }
}