package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;

public class PurePursuitTracker {

    public boolean reachedEnd = false;
    public Path path;

    public class Command {
        public Twist delta;

        public Command(Twist delta) {
            this.delta = delta;
        }
    }

    public PurePursuitTracker(Path path) {
        this.path = path;
    }

    public static int getDirection(Translation lookaheadPoint, Translation robotCoordinates) {
        if (lookaheadPoint.direction().rotate(robotCoordinates.direction().inverse()).radians() < 0.0) {
            return 1;
        } else {
            return -1;
        }
    }

    public Arc getArcToSegment(Segment s, Translation lookaheadPoint, Translation robotCoordinates) {
        Translation lookaheadPointToCenter;

        int direction = getDirection(lookaheadPoint, robotCoordinates);

        lookaheadPointToCenter = s.getDirection(lookaheadPoint).rotate(Rotation.fromDegrees(90 * direction));

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

    public Command update(Translation lookaheadPoint, Translation robotCoordinates) {
        Path.PathUpdate pathUpdate = path.update(robotCoordinates);
        Segment s = pathUpdate.currSegment;
        Arc arc = getArcToSegment(s, lookaheadPoint, robotCoordinates);

        if(isFinished()) {
            return new Command(new Twist(0, 0, 0));
        }

        if(pathUpdate.remainingDistance < arc.getLength()) {
            reachedEnd = true;
        }

        return new Command(new Twist(0, arc.getLength(), arc.getAngle().radians()*getDirection(lookaheadPoint, robotCoordinates)));
    }

    private boolean isFinished() {
        return reachedEnd;
    }

}
