package frc.team3256.lib.path;

import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;

public class Line extends Segment{

    private Translation slope;

    /**
     * @param startX X-Coordinate of the starting position
     * @param startY Y-Coordinate of the starting position
     * @param endX X-Coordinate of the ending position
     * @param endY Y-Coordinate of the ending position
     */
    public Line(double startX, double startY, double endX, double endY){
        type = Type.LINE;
        start = new Translation(startX, startY);
        end = new Translation(endX, endY);
        slope = new Translation(start, end);
    }

    /**
     * @param startX X-Coordinate of the starting position
     * @param startY Y-Coordinate of the starting position
     * @param endX X-Coordinate of the ending position
     * @param endY Y-Coordinate of the ending position
     * @param startVel starting velocity
     * @param accel acceleration
     */
    public Line(double startX, double startY, double endX, double endY, double startVel, double accel){
        this(startX, startY, endX, endY);
        this.startVel = startVel;
        this.accel = accel;
        this.endVel = Math.sqrt(Math.pow(startVel, 2) + 2 * accel * getLength());
    }

    /**
     * @return type of the segment, in this case a line
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * @return start position of line
     */
    @Override
    public Translation getStartPoint() {
        return start;
    }

    /**
     * @return end position of line
     */
    @Override
    public Translation getEndPoint() {
        return end;
    }

    /**
     * @return slope of line
     */
    public Translation getSlope() {
        return slope;
    }

    /**
     * @return length of line, .norm is pythagorean theorem
     */
    @Override
    public double getLength() {
        return slope.norm();
    }

    /**
     * dot = dot product of u & v
     * interpolationParameter = result of proj u onto v equation
     *
     * @param position current position of robot
     * @return if proj u onto v < 0, start pos of line | if proj u onto v > 1, end pos of line | otherwise scale vector to lookahead distance
     */
    @Override
    public Translation getClosestPointOnSegment(Translation position) {
        //Projection of the position onto the line
        //Proj b a = (a dot b)/norm(b)^2 * b
        Translation startToPosition = new Translation(start, position);
        //Calculate interpolation parameter: (a dot b)/norm(b)^2
        double dot = startToPosition.dot(slope);
        double interpolationParameter = dot/Math.pow(slope.norm(), 2);
        //Interpolate accordingly
        if (interpolationParameter < 0) return start;
        else if (interpolationParameter > 1) return end;
        return start.translate(slope.scale(interpolationParameter));
    }

    @Override
    public Translation getDirection(Translation lookaheadPoint) {
        return slope.scale(1.0/slope.norm());
    }

    /**
     * Finds our lookahead point
     *
     * @param lookaheadDistance distance of how far to look ahead of robot
     * @param position current position of robot
     * @return lookahead point
     */
    @Override
    public Translation getLookAheadPoint(double lookaheadDistance, Translation position) {
        //If our lookahead distance is higher than the line length, make the distance the line length
        if (lookaheadDistance > getLength()){
            lookaheadDistance = getLength();
        }
        Translation closestPoint = getClosestPointOnSegment(position);
        //scales based on (currDist + lookaheadDist) / line length
        Translation scaledSlope = slope.scale((lookaheadDistance+getCurrDistanceTraveled(closestPoint))/(slope.norm()));
        return start.translate(scaledSlope);
    }

    /**
     * @param closestPoint closest point
     * @return current distance traveled
     */
    @Override
    public double getCurrDistanceTraveled(Translation closestPoint) {
        Translation startToClosestPoint = new Translation(start, closestPoint);
        return startToClosestPoint.norm();
    }

    /**
     * @param closestPoint closest point
     * @return remaining distance based of current distance traveled
     */
    @Override
    public double getRemainingDistance(Translation closestPoint){
        return getLength() - getCurrDistanceTraveled(closestPoint);
    }
}
