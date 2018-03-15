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
    private double pathCompletionTolerance, lookaheadDistance;
    private Twist prevOutput = new Twist(0,0,0);
    private double dt;

    public PurePursuitTracker() { }

    public void reset() {
        this.path.resetPath();
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public void setPathCompletionTolerance(double pathCompletionTolerance) {
        this.pathCompletionTolerance = pathCompletionTolerance;
    }

    public void setLookaheadDistance(double lookaheadDistance) {
        this.lookaheadDistance = lookaheadDistance;
    }

    public void setLoopTime(double dt) {
        this.dt = dt;
    }

    // uses cross product to get 1 for positive angle and -1 for negative
    public int getDirection(Translation lookaheadPoint, Translation robotCoordinates) {
        return lookaheadPoint.cross(robotCoordinates) > 0 ? 1 : -1;
    }

    public Optional<Arc> getArcToSegment(Translation robotCoordinates, Path.PathUpdate p) {

        Segment s = p.currSegment;
        Translation lookaheadPoint = p.lookaheadPoint;

        // whether the angle from the robot to the lookahead point is positive or negative
        int direction = getDirection(p.lookaheadPoint, robotCoordinates);
        // whether the angle of the slope (tor tangent slope) at the lookahead point is positive or negative
        int lookaheadSlopeDirection = (int) Math.signum(s.getDirection(lookaheadPoint).direction().radians());

        // perpendicular to the lookahead slope
        Translation lookaheadPointToCenter = s.getDirection(lookaheadPoint);
        lookaheadPointToCenter = lookaheadPointToCenter.rotate(Rotation.fromDegrees(90.0 * direction * lookaheadSlopeDirection));

        Translation lookaheadPointToRobot = new Translation(lookaheadPoint, robotCoordinates);
        Rotation angleDifference = Rotation.fromRadians(lookaheadPointToCenter.getAngle(lookaheadPointToRobot).radians()*direction*-1);

        // angle formed by a tangent and a chord is half the angle of the intercepted minor arc
        Translation robotToCenter = lookaheadPointToRobot.inverse().rotate(angleDifference.inverse()).scale(.5);

        RigidTransform lookaheadPointToCenterTransform = new RigidTransform(lookaheadPoint, lookaheadPointToCenter.direction());
        RigidTransform robotToCenterTransform = new RigidTransform(robotCoordinates, robotToCenter.direction());

        // the center is the intersection of the robotToCenter line and the lookaheadPointToCenter line
        Translation center = lookaheadPointToCenterTransform.intersection(robotToCenterTransform);

        /*
        // if there is no intersection then...
        // probably not needed
        if (center.norm() == Double.POSITIVE_INFINITY) {
            center = robotCoordinates.translate(robotToCenter);
        }
        */

        if (Math.abs(lookaheadPointToRobot.norm() - p.lookaheadDistance) < 0.1) {
            //If the lookahead distance is the same as the distance from the robot to the lookahead point, then the robot will make a line and not an arc to the lookahead point
            return Optional.empty();
        }

        Arc robotToLookaheadPointArc = new Arc(robotCoordinates.x(), robotCoordinates.y(), lookaheadPoint.x(), lookaheadPoint.y(), center.x(), center.y());
        return Optional.of(robotToLookaheadPointArc);
    }

    public Twist update(Translation robotCoordinates) {
        //Update path
        Path.PathUpdate pathUpdate = path.update(robotCoordinates, lookaheadDistance);
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
        vel = s.checkVelocity(pathUpdate.closestPoint, prevOutput.dx(), dt);
        Twist rv;

        //If we have an arc
        Arc arc;
        if (optionalArc.isPresent()){
            arc = optionalArc.get();
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