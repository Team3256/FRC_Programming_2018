package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;

import java.util.Optional;

public class PurePursuitTracker {

    private boolean reachedEnd = false;
    private Path path;
    private final double kEpsilon = 1E-9;
    private double tolerance;
    private Twist prevOutput = new Twist(0,0,0);

    public PurePursuitTracker(Path path, double tolerance) {
        this.path = path;
        this.tolerance = tolerance;
    }

    public static int getDirection(Translation lookaheadPoint, Translation robotCoordinates) {
        if (lookaheadPoint.cross(robotCoordinates) > 0.0) {
            return 1;
        } else {
            return -1;
        }
    }

    public Optional<Arc> getArcToSegment(Segment s, Translation lookaheadPoint, Translation robotCoordinates) {
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
        if (robotToCenter.norm() < kEpsilon) { //allowed error from path
            //Cross track error is 0, so no arc (just continue driving straight)
            return Optional.empty();
        }

        Arc robotToLookaheadPointArc = new Arc(robotCoordinates.x(), robotCoordinates.y(), lookaheadPoint.x(), lookaheadPoint.y(), center.x(), center.y());
        return Optional.of(robotToLookaheadPointArc);
    }

    public Twist update(Translation robotCoordinates) {
        //Update path
        Path.PathUpdate pathUpdate = path.update(robotCoordinates);
        //Check if we are done
        if(pathUpdate.remainingDistance < tolerance) {
            reachedEnd = true;
        }
        if(isFinished()) {
            return new Twist(0, 0, 0);
        }
        Segment s = pathUpdate.currSegment;
        //Calculate arc to get us back on path
        Optional<Arc> optionalArc = getArcToSegment(s, pathUpdate.lookaheadPoint, robotCoordinates);
        //calculate linear and angular velocities
        double vel, angularVel = 0;
        vel = s.checkVelocity(pathUpdate.closestPoint, prevOutput.dx());
        Twist rv;
        //If we have an arc
        if (optionalArc.isPresent()){
            Arc arc = optionalArc.get();
            double direction = arc.getAngle().radians() / Math.abs(arc.getAngle().radians());
            angularVel = vel /arc.getRadius();
            rv = new Twist(vel, 0, angularVel*direction);
        }
        //Otherwise, we are on track, so only return a linear velocity
        else rv = new Twist(vel, 0, 0);
        prevOutput = rv;
        return rv;
    }

    private boolean isFinished() {
        return reachedEnd;
    }

}