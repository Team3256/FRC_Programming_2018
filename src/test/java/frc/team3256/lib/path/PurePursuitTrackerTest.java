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
        p.addSegment(new Arc(24, 24, 48,24, 36, 12));

        PurePursuitTracker pursuit = new PurePursuitTracker(p, 1.0);
        Translation lookaheadPoint = new Translation(9, 9);
        Translation robotCoordinates = new Translation(15, 3);
        Twist command = pursuit.update(robotCoordinates);

        assertEquals(command.dy(), 0, kEpsilon);
        assertEquals(command.dx(), Math.PI * 3.0 * Math.sqrt(2), kEpsilon);
        assertEquals(command.dtheta(), -Math.PI, kEpsilon);

        System.out.println("CHECK");
        robotCoordinates = new Translation(0, 0);
        command = pursuit.update(robotCoordinates);
        Line line = new Line(0,0,20,20,10,3, 250);
        Translation closestPoint = new Translation(5,5);
        double accel = Math.min((Math.pow(10.0,2.0) - Math.pow(2.0,2))/(2*15*Math.sqrt(2)),3);
        assertEquals(line.checkVelocity(closestPoint,2.0),2.0 + accel*Constants.kControlLoopPeriod ,kEpsilon);

        line = new Line(0,0,0,50,30,8, 250);
        closestPoint = new Translation(0,25);
        accel = Math.min((Math.pow(30.0,2)-Math.pow(10.0,2))/(2.0*25),8);
        assertEquals(line.checkVelocity(closestPoint,10.0),10.0+accel*Constants.kControlLoopPeriod,kEpsilon);

        line = new Line(10.0,10.0,100.0,100.0,200.0,5.0, 250.0);
        double distance = 0;
        double goalVel = 200.0;
        double currVel = 0;
        closestPoint = new Translation(10.0, 10.0);
        double remainingDistance;
        do {
            remainingDistance = line.getRemainingDistance(closestPoint);
            accel = Math.min((Math.pow(goalVel,2)-Math.pow(currVel,2))/(2*remainingDistance), 15.0);
            distance += currVel*Math.pow(Constants.kControlLoopPeriod, 2);
            currVel = line.checkVelocity(closestPoint, currVel);
            closestPoint = new Translation(10.0 + distance*Math.sqrt(2)/2, 10.0 + distance*Math.sqrt(2)/2);
            //System.out.println(currVel);
        } while(remainingDistance > 0);

        System.out.println("CHECK ENDS");

        robotCoordinates = new Translation(9, 3);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dx(), 0.0, kEpsilon);
        assertEquals(command.dy(), Math.PI * 1.5 * Math.sqrt(2), kEpsilon);
        assertEquals(command.dtheta(), -Math.PI / 2.0, kEpsilon);


        robotCoordinates = new Translation(3, 9);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dx(), 0.0, kEpsilon);
        assertEquals(command.dy(), Math.PI * 1.5 * Math.sqrt(2), kEpsilon);
        assertEquals(command.dtheta(), - Math.PI / 2.0, kEpsilon);

        robotCoordinates = new Translation(48, 0);
        lookaheadPoint = new Translation(24, 24);
        command = pursuit.update(robotCoordinates);

        robotCoordinates = new Translation(48,28);
        lookaheadPoint = new Translation(48,24);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dx(), 0.0, kEpsilon);
        assertEquals(command.dy(), Math.PI * Math.sqrt(2), kEpsilon);
        assertEquals(command.dtheta(), - Math.PI / 2.0, kEpsilon);

        /*
        p.addSegment(new Line(0, 0, 100, 100, 120, 20, 250));
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