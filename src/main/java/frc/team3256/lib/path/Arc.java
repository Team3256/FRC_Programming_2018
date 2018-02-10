package frc.team3256.lib.path;

import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.robot.Constants;

public class Arc extends Segment{

    private Translation center;
    private Translation centerToStart, centerToEnd;
    private double radius;
    private Rotation angle;

    public Arc(double startX, double startY, double endX, double endY, double centerX, double centerY, double goalVel, double maxAccel, double maxVel){
        type = Type.ARC;
        start = new Translation(startX, startY);
        end = new Translation(endX, endY);
        center = new Translation(centerX, centerY);
        centerToStart = new Translation(center, start);
        centerToEnd = new Translation(center, end);
        angle = centerToStart.getAngle(centerToEnd);

        this.goalVel = goalVel;
        this.maxAccel = maxAccel;
        this.maxVel = maxVel;

        //This should not be possible. We are using constant-curvature arcs.
        //If this ever occurs, then one (or more) of the parameters were passed in incorrectly
        if (Math.abs(centerToStart.norm() - centerToEnd.norm()) > 10E-9){
            System.out.println("ERROR: THIS ARC IS NOT CONSTANT_CURVATURE");
            System.out.println("START: " + start);
            System.out.println("END: " + end);
            System.out.println("CENTER: " + center + "\n");
            radius = Double.NaN;
        }
        else radius = centerToStart.norm();
    }


    public Arc(double startX, double startY, double endX, double endY, double centerX, double centerY){
        this(startX, startY, endX, endY, centerX, centerY, 0.0, 0.0, 0.0);
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Translation getStartPoint() {
        return start;
    }

    @Override
    public Translation getEndPoint() {
        return end;
    }

    @Override
    public double getLength() {
        //arc length = radius*theta
        return radius*getAngle().radians();
    }

    public double getRadius(){
        return radius;
    }

    public Translation getCenter(){
        return center;
    }

    public Translation getCenterToStart(){
        return centerToStart;
    }

    public Translation getCenterToEnd(){
        return centerToEnd;
    }

    public Rotation getAngle() { return angle; }

    @Override
    public Translation getClosestPointOnSegment(Translation position) {
        //find vector from the center of the arc to the current position
        Translation delta = new Translation(center, position);
        //rescale the vector to end on the arc
        delta = delta.scale(centerToStart.norm()/delta.norm());
        //if only one of the cross products are negative, the point is within range of the arc
        if (delta.cross(centerToStart)*delta.cross(centerToEnd) < 0){
            //so just return the the point on the arc along the vector
            return center.translate(delta);
        }
        //otherwise, the point is out of the range of the arc,
        //so either return the start or end point accordingly
        else{
            Translation positionToStart = new Translation(position, start);
            Translation positionToEnd = new Translation(position, end);
            if (positionToStart.norm() < positionToEnd.norm()){
                return start;
            }
            else return end;
        }
    }

    @Override
    public Translation getDirection(Translation lookaheadPoint) {
        Translation lookaheadToTarget = new Translation(center, lookaheadPoint).rotate(Rotation.fromDegrees(-90));
        return (lookaheadToTarget.scale(1/lookaheadToTarget.norm()));
    }

    /*public static void main(String args[]) {
        Arc arc = new Arc(0, 0, 5, 5, 5, 0);
        System.out.println(arc.getDirection(new Translation(5-2.5*Math.sqrt(2), 2.5*Math.sqrt(2))));
    }*/

    @Override
    public Translation getLookAheadPoint(double lookaheadDistance, Translation position) {
        /*
        double startToLookAheadAngle = (lookaheadDistance + getCurrDistanceTraveled(closestPoint)) / radius;
        double xDif = radius*(1 - Math.cos(startToLookAheadAngle));
        double yDif = radius*Math.sin(startToLookAheadAngle);
        Translation startToLookAhead = new Translation(xDif, yDif);
        return startToLookAhead.translate(start);
        */
        Translation centerToPosition = new Translation(center, position);
        double angleToRotate = lookaheadDistance/radius;
        if (end.rotate(start.direction().inverse()).y() < 0) {
            angleToRotate = -1.0 * angleToRotate;
        }
        return center.translate(centerToPosition.rotate(Rotation.fromRadians(angleToRotate)));
    }

    @Override
    public double getCurrDistanceTraveled(Translation closestPoint) {
        Translation centerToClosestPoint = new Translation(center, closestPoint);
        double angle = centerToClosestPoint.getAngle(centerToStart).radians();
        return angle*radius;
    }

    @Override
    public double getRemainingDistance(Translation closestPoint) {
        return getLength() - getCurrDistanceTraveled(closestPoint);
    }

    @Override
    public double checkVelocity(Translation closestPoint, double prevVelocity) {
        double remainingDistance = getRemainingDistance(closestPoint);
        double outputVelFromEnd = Math.sqrt(Math.pow(goalVel, 2.0)-2*maxAccel*remainingDistance);
        double outputVel = prevVelocity + maxAccel * Constants.kControlLoopPeriod;
        outputVelFromEnd = Double.isNaN(outputVelFromEnd) ? outputVel : outputVelFromEnd;
        outputVel = Math.min(outputVel, outputVelFromEnd);
        outputVel = Math.min(outputVel, maxVel);
        return outputVel;
    }
}
