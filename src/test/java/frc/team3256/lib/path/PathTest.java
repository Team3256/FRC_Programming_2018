package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import frc.team3256.robot.subsystems.Intake;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {

    private double kEpsilon = 1E-9;

    @Test
    public void update() {
        Path p = new Path();
        p.addSegment(new Line(0,0,0,10));
        p.addSegment(new Arc(0,10,5,15,5,10));

        for (int y = 0; y <= 10; y++) {
            Path.PathUpdate pathUpdate = p.update(new Translation(5, y));
            assertEquals(pathUpdate.distanceToPath, 5, kEpsilon);
            assertEquals(pathUpdate.lookaheadPoint.x(), 0, kEpsilon);
            assertEquals(pathUpdate.lookaheadPoint.y(), y+5, kEpsilon);
            assertEquals(pathUpdate.remainingDistance, .5 * Math.PI * 5 + 10 - y, kEpsilon);
        }

        Path.PathUpdate pathUpdate = p.update(new Translation(2, 10));
        assertEquals(pathUpdate.distanceToPath, 2, kEpsilon);
        //assertEquals(pathUpdate.lookaheadPoint.x(), 5-5*Math.cos(2/5), kEpsilon);

        //double lookaheadPointX = 1 - Math.cos(Math.toRadians(deg)+.4);
        //double lookaheadPointY = Math.sin(Math.toRadians(deg)+.4);

        //System.out.println((3*x+2)+"   "+(10+3*y));
        //System.out.println((lookaheadPointX*5)+"   "+(10+5*lookaheadPointY));
        //System.out.println(pathUpdate.lookaheadPoint.x()+"   "+pathUpdate.lookaheadPoint.y());

        //assertEquals(pathUpdate.lookaheadPoint.x(), lookaheadPointX*5, kEpsilon);
        //assertEquals(pathUpdate.lookaheadPoint.y(), 10+5*lookaheadPointY, kEpsilon);
/*
        for (int deg = 0; deg <= 90; deg++) {
            double x = 1 - Math.cos(Math.toRadians(deg));
            double y = Math.sin(Math.toRadians(deg));
            Path.PathUpdate pathUpdate = p.update(new Translation(3*x+2, 10+3*y));
            assertEquals(pathUpdate.distanceToPath, 2, kEpsilon);
            double lookaheadPointX = 1 - Math.cos(Math.toRadians(deg)+.4);
            double lookaheadPointY = Math.sin(Math.toRadians(deg)+.4);

            //System.out.println((3*x+2)+"   "+(10+3*y));
            //System.out.println((lookaheadPointX*5)+"   "+(10+5*lookaheadPointY));
            //System.out.println(pathUpdate.lookaheadPoint.x()+"   "+pathUpdate.lookaheadPoint.y());

            assertEquals(pathUpdate.lookaheadPoint.x(), lookaheadPointX*5, kEpsilon);
            assertEquals(pathUpdate.lookaheadPoint.y(), 10+5*lookaheadPointY, kEpsilon);
        }*/

    }
}