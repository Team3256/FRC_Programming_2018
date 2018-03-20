package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;
import frc.team3256.robot.Constants;
import frc.team3256.robot.subsystems.Intake;
import org.junit.Test;
import static org.junit.Assert.*;

public class PurePursuitTrackerTest {

    private double kEpsilon = 1E-9;

    @Test
    public void testArcToSegment() {


        Path p = new Path();
        p.addSegment(new Line(0, 0, 24, 24, 120.0, 20.0, 250.0));


        PurePursuitTracker pursuit = new PurePursuitTracker();
        pursuit.setPath(p);
        pursuit.setLoopTime(Constants.kControlLoopPeriod);
        pursuit.setLookaheadDistance(0);
        pursuit.setPathCompletionTolerance(Constants.kPathCompletionTolerance);

        /*
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

        robotCoordinates = new Translation(48.2, 24);
        pursuit.update(robotCoordinates);


        robotCoordinates = new Translation(50, 5);
        command = pursuit.update(robotCoordinates);
        assertEquals(command.dy(), 0.0, kEpsilon);

        command = pursuit.update(new Translation(71, 12));
        System.out.println(command.dx());
        assertEquals(pursuit.isFinished(), false);

        command = pursuit.update(new Translation(73, 12));
        System.out.println(command.dx());
        assertEquals(pursuit.isFinished(), true);
        */

        pursuit.setLookaheadDistance(2*Math.sqrt(2));

        RigidTransform robotPos = new RigidTransform(new Translation(8, 4), Rotation.fromDegrees(90));
        pursuit.update(robotPos);


        robotPos = new RigidTransform(new Translation(4, 8), Rotation.fromDegrees(-20));
        pursuit.update(robotPos);

        pursuit.setLookaheadDistance(4);

        robotPos = new RigidTransform(new Translation(8, 4), Rotation.fromDegrees(120));
        pursuit.update(robotPos);


        robotPos = new RigidTransform(new Translation(20, 20), Rotation.fromDegrees(-90));
        pursuit.update(robotPos);

        robotPos = new RigidTransform(new Translation(25, 25), Rotation.fromDegrees(-30));
        pursuit.update(robotPos);

        robotPos = new RigidTransform(new Translation(25, 25), Rotation.fromDegrees(-30));
        pursuit.update(robotPos);

    }
}