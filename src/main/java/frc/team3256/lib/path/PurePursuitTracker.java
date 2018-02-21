package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;
import frc.team3256.lib.trajectory.Trajectory;

import java.util.Optional;

public class PurePursuitTracker {

    private boolean reachedEnd = false;
    private Path path;
    private final double kEpsilon = 1E-9;
    private double pathCompletionTolerance, errorFromPathTolerance;
    private Twist prevOutput = new Twist(0,0,0);
    private double dt;

    public PurePursuitTracker() {
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setTolerances(double pathCompletionTolerance, double errorFromPathTolerance) {
        this.pathCompletionTolerance = pathCompletionTolerance;
        this.errorFromPathTolerance = errorFromPathTolerance;
    }

    public void setLoopTime(double dt) {
        this.dt = dt;
    }

    public static int getDirection(Translation lookaheadPoint, Translation robotCoordinates) {
        return lookaheadPoint.cross(robotCoordinates) > 0.0 ? 1 : -1;
    }

    public Optional<Arc> getArcToSegment(Translation robotCoordinates, Path.PathUpdate p) {
        int direction = getDirection(p.lookaheadPoint, robotCoordinates);

        Segment s = p.currSegment;
        Translation lookaheadPoint = p.lookaheadPoint;

        int lookaheadSlopeDirection = (int) Math.signum(s.getDirection(lookaheadPoint).direction().radians());

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
        if (robotToCenter.norm() < kEpsilon || p.distanceToPath < errorFromPathTolerance) {
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
        if(pathUpdate.remainingDistance < pathCompletionTolerance) {
            reachedEnd = true;
        }
        if(isFinished()) {
            return new Twist(0, 0, 0);
        }
        Segment s = pathUpdate.currSegment;
        //Calculate arc to get us back on path
        Optional<Arc> optionalArc = getArcToSegment(robotCoordinates, pathUpdate);
        //calculate linear and angular velocities
        double vel, angularVel;
        vel = s.checkVelocity(pathUpdate.closestPoint, prevOutput.dx());
        Twist rv;

        //If we have an arc
        Arc arc;
        if (optionalArc.isPresent()){
            arc = optionalArc.get();
            double direction = Math.signum(arc.getDirection(pathUpdate.lookaheadPoint).direction().radians());
            angularVel = vel/arc.getRadius();
            rv = new Twist(vel, 0, angularVel*direction);
        }
        else if(s.type == Segment.Type.ARC) {
            arc = (Arc) s;
            double direction = Math.signum(arc.getDirection(pathUpdate.lookaheadPoint).direction().radians());
            angularVel = vel/arc.getRadius();
            rv = new Twist(vel, 0, angularVel*direction);
        }
        //Otherwise, we are on track, so only return a linear velocity
        else {
            rv = new Twist(vel, 0, 0);
        }

        prevOutput = rv;
        return rv;
    }

    public boolean isFinished() {
        return reachedEnd;
    }

}