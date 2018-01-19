package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;

public class Line extends Segment{

    private Translation slope;

    public Line(double startX, double startY, double endX, double endY){
        type = Type.LINE;
        start = new Translation(startX, startY);
        end = new Translation(endX, endY);
        slope = new Translation(start, end);
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

    public Translation getSlope() {
        return slope;
    }

    @Override
    public double getLength() {
        return slope.norm();
    }

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
    public Translation getLookAheadPoint(double lookaheadDistance) {
        return null;
    }
}
