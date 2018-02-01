package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import frc.team3256.robot.Constants;
import org.junit.Test;
import org.opencv.core.Mat;

import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {

        Path p = new Path();
        p.addSegment(new Line(0, 0, 24, 24, 120.0, 20.0));
        p.addSegment(new Arc(24, 24, 48,24, 36, 12));

        PurePursuitTracker pursuit = new PurePursuitTracker(p);
        Translation lookaheadPoint = new Translation(9, 9);
        Translation robotCoordinates = new Translation(15, 3);
        PurePursuitTracker.Command command = pursuit.update(lookaheadPoint, robotCoordinates, 100.0);

        assertEquals(command.delta.dx(), 0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * 3.0 * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), -Math.PI, kEpsilon);


        System.out.println("CHECK");
        lookaheadPoint = new Translation(9, 9);
        robotCoordinates = new Translation(0, 0);
        command = pursuit.update(lookaheadPoint, robotCoordinates, 115.0);
        Line line = new Line(0,0,20,20,10,3);
        Translation closestPoint = new Translation(5,5);
        double accel = Math.min((Math.pow(10.0,2.0) - Math.pow(2.0,2))/(2*15*Math.sqrt(2)),3);
        assertEquals(line.runVelocity(closestPoint,2.0),2.0 + accel*Constants.kControlLoopPeriod ,kEpsilon);

        line = new Line(0,0,0,50,30,8);
        closestPoint = new Translation(0,25);
        accel = Math.min((Math.pow(30.0,2)-Math.pow(10.0,2))/(2.0*25),8);
        assertEquals(line.runVelocity(closestPoint,10.0),10.0+accel*Constants.kControlLoopPeriod,kEpsilon);

        line = new Line(10.0,10.0,100.0,100.0,50.0,15.0);
        double distance = 0;
        double goalVel = 50.0;
        double currVel = 0;
        closestPoint = new Translation(10.0, 10.0);
        double remainingDistance;
        do {
            remainingDistance = line.getRemainingDistance(closestPoint);
            accel = Math.min((Math.pow(goalVel,2)-Math.pow(currVel,2))/(2*remainingDistance), 15.0);
            System.out.println(accel);
            distance+=currVel*Math.pow(Constants.kControlLoopPeriod, 2);
            currVel = line.runVelocity(closestPoint, currVel);
            closestPoint = new Translation(10.0 + distance*Math.sqrt(2)/2, 10.0 + distance*Math.sqrt(2)/2);

        } while(remainingDistance > 0);

        //assertEquals(line.runVelocity(closestPoint,20.0),20.0+accel*Constants.kControlLoopPeriod,kEpsilon);





        System.out.println("CHECK ENDS");

        robotCoordinates = new Translation(9, 3);
        command = pursuit.update(lookaheadPoint, robotCoordinates, 100.0);
        assertEquals(command.delta.dx(), 0.0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * 1.5 * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), -Math.PI / 2.0, kEpsilon);


        robotCoordinates = new Translation(3, 9);
        command = pursuit.update(lookaheadPoint, robotCoordinates, 100.0);
        assertEquals(command.delta.dx(), 0.0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * 1.5 * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), - Math.PI / 2.0, kEpsilon);

        robotCoordinates = new Translation(48, 0);
        lookaheadPoint = new Translation(24, 24);
        command = pursuit.update(lookaheadPoint, robotCoordinates, 100.0);

        robotCoordinates = new Translation(48,28);
        lookaheadPoint = new Translation(48,24);
        command = pursuit.update(lookaheadPoint, robotCoordinates, 100.0);
        assertEquals(command.delta.dx(), 0.0, kEpsilon);
        assertEquals(command.delta.dy(), Math.PI * Math.sqrt(2), kEpsilon);
        assertEquals(command.delta.dtheta(), - Math.PI / 2.0, kEpsilon);


    }
}