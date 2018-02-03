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
        public double vel;

        public Command(Twist delta, double vel) {
            this.delta = delta;
            this.vel = vel;
        }
    }

    public PurePursuitTracker(Path path) {
        this.path = path;
    }

    public static int getDirection(Translation lookaheadPoint, Translation robotCoordinates) {
        if (lookaheadPoint.cross(robotCoordinates) > 0.0) {
            return 1;
        } else {
            return -1;
        }
    }

    public Arc getArcToSegment(Segment s, Translation lookaheadPoint, Translation robotCoordinates) {

        int direction = getDirection(lookaheadPoint, robotCoordinates);
        int lookaheadSlopeDirection = s.getDirection(lookaheadPoint).direction().tan() > 0 ? 1 : -1;

        Translation lookaheadPointToCenter = s.getDirection(lookaheadPoint).rotate(Rotation.fromDegrees(90.0 * direction * lookaheadSlopeDirection));

        Translation lookaheadPointToRobot = new Translation(lookaheadPoint, robotCoordinates);
        Rotation angleDifference = Rotation.fromRadians(lookaheadPointToCenter.getAngle(lookaheadPointToRobot).radians()*direction*-1);
        Translation robotToCenter = lookaheadPointToRobot.inverse().rotate(angleDifference.inverse()).scale(.5);

        RigidTransform lookaheadPointToCenterTransform = new RigidTransform(lookaheadPoint, lookaheadPointToCenter.direction());
        RigidTransform robotToCenterTransform = new RigidTransform(robotCoordinates, robotToCenter.direction());

        Translation center = lookaheadPointToCenterTransform.intersection(robotToCenterTransform);

        if (center.norm() == Double.POSITIVE_INFINITY) {
            center = robotCoordinates.translate(robotToCenter);
        }

        if (robotToCenter.norm() < .001) { //allowed error from path
            // generate a line to some point
        }

        Arc robotToLookaheadPointArc = new Arc(robotCoordinates.x(), robotCoordinates.y(), lookaheadPoint.x(), lookaheadPoint.y(), center.x(), center.y());

        return robotToLookaheadPointArc;
    }

    public Command update(Translation robotCoordinates, double currVel) {
        Path.PathUpdate pathUpdate = path.update(robotCoordinates);
        Translation lookaheadPoint = pathUpdate.lookaheadPoint;
        Segment s = pathUpdate.currSegment;
        Arc arc = getArcToSegment(s, lookaheadPoint, robotCoordinates);
        double vel = s.runVelocity(pathUpdate.closestPoint, currVel);

        if(isFinished()) {
            //return new Command(new Twist(0, 0, 0));
        }
        if(pathUpdate.remainingDistance < arc.getLength()) {
            reachedEnd = true;
            path.removeSegment();
            System.out.println("END REACHED");
        }

        return new Command(new Twist(0, arc.getLength(), -arc.getAngle().radians()), vel);
    }

    public Command update(Translation lookaheadPoint, Translation robotCoordinates, double currVel) {
        Path.PathUpdate pathUpdate = path.update(robotCoordinates);
        Segment s = pathUpdate.currSegment;
        Arc arc = getArcToSegment(s, lookaheadPoint, robotCoordinates);
        double vel = s.runVelocity(pathUpdate.closestPoint, currVel);

        if(isFinished()) {
            //return new Command(new Twist(0, 0, 0));
        }
        if(pathUpdate.remainingDistance < arc.getLength()) {
            reachedEnd = true;
            path.removeSegment();
            System.out.println("END REACHED");
        }

        return new Command(new Twist(0, arc.getLength(), -arc.getAngle().radians()), vel);
    }

    private boolean isFinished() {
        return reachedEnd;
    }

}