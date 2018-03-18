package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.lib.math.Twist;
import frc.team3256.robot.subsystems.Intake;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

    public Optional<Arc> getArcToSegment(RigidTransform robotPos, Path.PathUpdate p) {

        Translation robotCoordinates = new Translation(robotPos.getTranslation().x(), robotPos.getTranslation().y());

        Rotation robotAngle = robotPos.getRotation();

        Translation lookaheadPoint = p.lookaheadPoint;


        /*
        Wrong

        Segment s = p.currSegment;
        // whether the angle from the robot to the lookahead point is positive or negative
        int direction = getDirection(p.lookaheadPoint, robotCoordinates);
        // whether the angle of the slope (or tangent slope) at the lookahead point is positive or negative
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
        */

        Translation robotToLookaheadPoint = new Translation(robotCoordinates, lookaheadPoint);
        // rotates another -90 degrees because angle is based off of the unit circle (0deg at y=0) instead of from the robot's heading (0deg at x=0)
        Rotation turnAngle = robotToLookaheadPoint.direction().rotate(Rotation.fromDegrees(-90)).rotate(robotAngle.inverse());
        int turnDirection = (int) Math.signum(turnAngle.degrees());

        Rotation robotToCenterAngle = robotAngle.rotate(Rotation.fromDegrees(90 * turnDirection));
        Rotation lookaheadPointToCenterAngle = robotToLookaheadPoint.direction().rotate(Rotation.fromDegrees(180)).rotate(turnAngle);

        RigidTransform robotToCenterTransform = new RigidTransform(lookaheadPoint, robotToCenterAngle);
        RigidTransform lookaheadPointToCenterTransform = new RigidTransform(robotCoordinates, lookaheadPointToCenterAngle);
        Translation center = lookaheadPointToCenterTransform.intersection(robotToCenterTransform);

        /*
        System.out.println("ROBOT POS   "+robotCoordinates);
        System.out.println("--------x------"+lookaheadPoint);
        System.out.println("--------------"+robotToLookaheadPoint);
        System.out.println("ROBOT ANGLE  "+robotAngle);
        System.out.println("TURN ANGLE  "+turnAngle);
        System.out.println("CENTER     "+center);
        System.out.println("ROBOT TO CENTER ANGLE    "+robotToCenterAngle);
        System.out.println("LOOKAHEAD TO ROBOT DIRECTION    "+robotToLookaheadPoint.direction().rotate(Rotation.fromDegrees(180)));
        System.out.println("LOOKAHEAD TO CENTER ANGLE    "+lookaheadPointToCenterAngle);
        */

        /*
        // if there is no intersection then...
        // probably not needed
        if (center.norm() == Double.POSITIVE_INFINITY) {
            center = robotCoordinates.translate(robotToCenter);
        }
        */

        if (Math.abs(robotToLookaheadPoint.norm() - p.lookaheadDistance) < kEpsilon) {
            //If the lookahead distance is the same as the distance from the robot to the lookahead point, then the robot will make a line and not an arc to the lookahead point
            return Optional.empty();
        }

        Arc robotToLookaheadPointArc = new Arc(robotCoordinates.x(), robotCoordinates.y(), lookaheadPoint.x(), lookaheadPoint.y(), center.x(), center.y());
        return Optional.of(robotToLookaheadPointArc);
    }

    public Twist update(RigidTransform robotPos) {
        Translation robotCoordinates = new Translation(robotPos.getTranslation().x(), robotPos.getTranslation().y());
        //Update path
        Path.PathUpdate pathUpdate = path.update(robotCoordinates, lookaheadDistance);
        //Check if we are done
        System.out.println(pathUpdate.remainingDistance);
        if(pathUpdate.remainingDistance < pathCompletionTolerance) {
            reachedEnd = true;
        }
        if(isFinished()) {
            return new Twist(0, 0, 0);
        }
        Segment s = pathUpdate.currSegment;
        //Calculate arc to get us back on path
        Optional<Arc> optionalArc = getArcToSegment(robotPos, pathUpdate);
        //calculate linear and angular velocities
        double vel, angularVel;
        vel = s.checkVelocity(pathUpdate.closestPoint, prevOutput.dx(), dt);

        Twist rv;
        //If we have an arc
        if (optionalArc.isPresent()){
            Arc arc = optionalArc.get();
            double direction = Math.signum(arc.getDirection(pathUpdate.lookaheadPoint).direction().radians());
            angularVel = vel / arc.getRadius();
            rv = new Twist(vel, 0, angularVel * direction);
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