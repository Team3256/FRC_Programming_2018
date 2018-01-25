package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;

public class PurePursuitTracker {

    public PurePursuitTracker() {

    }

    public Arc getArcToSegment(Segment s, Translation lookaheadPoint, Translation robotCoordinates) {
        Translation lookaheadPointToCenter = s.getDirection(lookaheadPoint).rotate(Rotation.fromDegrees(-90));
        Translation lookaheadPointToRobot = new Translation(lookaheadPoint, robotCoordinates);
        Rotation angleDifference = lookaheadPointToCenter.getAngle(lookaheadPointToRobot);
        System.out.println(lookaheadPointToCenter);
        System.out.println(lookaheadPointToRobot);
        Translation robotToCenter = lookaheadPointToRobot.inverse().rotate(angleDifference.inverse());
        System.out.println(robotToCenter);
        RigidTransform lookaheadPointToCenterTransform = new RigidTransform(lookaheadPoint, lookaheadPointToCenter.direction());
        RigidTransform robotToCenterTransform = new RigidTransform(robotCoordinates, robotToCenter.direction());
        Translation center = lookaheadPointToCenterTransform.intersection(robotToCenterTransform);
        System.out.println(robotCoordinates+"   "+lookaheadPoint+"   "+center);
        Arc robotToLookaheadPointArc = new Arc(robotCoordinates.x(), robotCoordinates.y(), lookaheadPoint.x(), lookaheadPoint.y(), center.x(), center.y());

        return robotToLookaheadPointArc;
    }

    public static void main(String args[]) {
        PurePursuitTracker pursuit = new PurePursuitTracker();
        Line line = new Line(0, 0, 12, 12);
        Translation lookaheadPoint = new Translation(6, 6);
        Translation robotCoordinates = new Translation(7, 5-3*Math.sqrt(2));
        Arc arcToSegment = pursuit.getArcToSegment(line, lookaheadPoint, robotCoordinates);
        System.out.println(arcToSegment.getCenter());
    }

}
