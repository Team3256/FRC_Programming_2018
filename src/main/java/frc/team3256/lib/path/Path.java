package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;
import java.util.ArrayList;

public class Path {

    private ArrayList<Segment> segments;

    public Path(){
        segments = new ArrayList<>();
    }

    public void addSegment(Segment segment){
        segments.add(segment);
    }

    public static class PathUpdate{
        //lookahead point location
        Translation lookaheadPoint;
        //remaining distance in the path
        double remainingDistance;
        //distance from our current pose to the closest point on the path
        double distanceToPath;
        //current segment
        Segment currSegment;
        //closest point
        Translation closestPoint;
    }

    public void removeSegment() {
        segments.remove(0);
    }

    public PathUpdate update(Translation robotCoordinates){
        PathUpdate rv = new PathUpdate();
        Segment currSegment = segments.get(0);
        Translation closestPoint = currSegment.getClosestPointOnSegment(robotCoordinates);
        Translation robotToClosestPoint = new Translation(robotCoordinates, closestPoint);



        rv.distanceToPath = robotToClosestPoint.norm();
        rv.lookaheadPoint = currSegment.getLookAheadPoint(rv.distanceToPath, closestPoint);

        for (Segment s : segments) {
            rv.remainingDistance += s.getLength();
        }

        rv.remainingDistance -= currSegment.getCurrDistanceTraveled(closestPoint);

        rv.currSegment = currSegment;

        rv.closestPoint = closestPoint;

        return rv;
    }

}
