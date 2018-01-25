package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;

public class PurePursuitTracker {


    public PurePursuitTracker() {

    }

    public Arc getArcToSegment(Segment s, Translation lookaheadPoint, Translation robotCoordinates) {
        Translation lookaheadPointToCenter;

        if (lookaheadPoint.direction().rotate(robotCoordinates.direction().inverse()).radians() < 0.0) {
            lookaheadPointToCenter = s.getDirection(lookaheadPoint).rotate(Rotation.fromDegrees(90));
        } else {
            lookaheadPointToCenter = s.getDirection(lookaheadPoint).rotate(Rotation.fromDegrees(-90));
        }

        Translation lookaheadPointToRobot = new Translation(lookaheadPoint, robotCoordinates);
        Rotation angleDifference = lookaheadPointToCenter.getAngle(lookaheadPointToRobot);
        Translation robotToCenter = lookaheadPointToRobot.inverse().rotate(angleDifference.inverse()).scale(.5);
        RigidTransform lookaheadPointToCenterTransform = new RigidTransform(lookaheadPoint, lookaheadPointToCenter.direction());
        RigidTransform robotToCenterTransform = new RigidTransform(robotCoordinates, robotToCenter.direction());
        Translation center = lookaheadPointToCenterTransform.intersection(robotToCenterTransform);

        if (center.norm() == Double.POSITIVE_INFINITY) {
            center = robotCoordinates.translate(robotToCenter);
        }
        Arc robotToLookaheadPointArc = new Arc(robotCoordinates.x(), robotCoordinates.y(), lookaheadPoint.x(), lookaheadPoint.y(), center.x(), center.y());

        return robotToLookaheadPointArc;
    }

}
