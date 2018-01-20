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
    public Translation getLookAheadPoint(double lookaheadDistance) {
        return null;
    }
}
