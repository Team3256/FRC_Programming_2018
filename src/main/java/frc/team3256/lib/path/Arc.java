package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;

public class Arc extends Segment{

    private Translation center;
    private Translation centerToStart, centerToEnd;
    private double radius;

    public Arc(double startX, double startY, double endX, double endY, double centerX, double centerY){
        type = Type.ARC;
        start = new Translation(startX, startY);
        end = new Translation(endX, endY);
        center = new Translation(centerX, centerY);
        centerToStart = new Translation(center, start);
        centerToEnd = new Translation(center, end);
        radius = centerToStart.norm();
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
        return radius*centerToStart.getAngle(centerToEnd).radians();
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
