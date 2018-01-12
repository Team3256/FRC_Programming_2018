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
    public Translation getClosestPointOnSegment(double pose) {
        return null;
    }

    @Override
    public Translation getLookAheadPoint(double lookaheadDistance) {
        return null;
    }
}
