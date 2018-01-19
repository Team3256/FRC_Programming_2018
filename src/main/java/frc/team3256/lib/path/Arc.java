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
        //This should not be possible. We are using ocnstant-curvature arcs.
        //If this ever occurs, then one (or more) of the parameters were passed in incorrectly
        if (centerToStart.norm() != centerToEnd.norm()){
            /*
            System.out.println("ERROR: THIS ARC IS NOT CONSTANT_CURVATURE");
            System.out.println("START: " + start);
            System.out.println("END: " + end);
            System.out.println("CENTER: " + center + "\n");
            */
            radius = Double.NaN;
        }
        else radius = centerToStart.norm();
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

    @Override
    public Translation getClosestPointOnSegment(Translation position) {
        return null;
    }

    @Override
    public Translation getLookAheadPoint(double lookaheadDistance) {
        return null;
    }
}
