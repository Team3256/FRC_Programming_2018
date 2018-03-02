package frc.team3256.lib.path;

import frc.team3256.lib.math.Translation;

import java.util.ArrayList;

public class Path {

    private ArrayList<Segment> segments;
    private int currSegmentNumber = 0;
    private static final double segmentCompletionTolerance = 0.01;

    public Path(){
        segments = new ArrayList<>();
    }
    PathUpdate prevPathUpdate;

    public void addSegment(Segment segment){
        segments.add(segment);
    }

    public void resetPath() {
        currSegmentNumber = 0;
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
        //lookahead distance
        double lookaheadDistance;
    }

    public int getLength() {
        return segments.size();
    }

    public PathUpdate update(Translation robotCoordinates) {
        return this.update(robotCoordinates, 0);
    }

    public PathUpdate update(Translation robotCoordinates, double lookaheadDistance){
        PathUpdate rv = new PathUpdate();
        rv.lookaheadDistance = lookaheadDistance;
        Segment currSegment = segments.get(currSegmentNumber);

        Translation closestPoint = currSegment.getClosestPointOnSegment(robotCoordinates);

        //remove current segment if we are done with this segment
        double distanceRemainingOnSegment = currSegment.getRemainingDistance(closestPoint);
        if (distanceRemainingOnSegment <= segmentCompletionTolerance) {
            System.out.println("REMOVING SEGMENT");
            System.out.println("remaining distance: " + distanceRemainingOnSegment);
            currSegmentNumber++;
            //segments.remove(0);
            if (currSegmentNumber == segments.size()) {
                return prevPathUpdate;
            }
            //else currSegment = segments.get(0);
            else currSegment = segments.get(currSegmentNumber);
        }

        //calculate closest point to robot on path
        closestPoint = currSegment.getClosestPointOnSegment(robotCoordinates);
        Translation robotToClosestPoint = new Translation(robotCoordinates, closestPoint);
        rv.distanceToPath = robotToClosestPoint.norm();

        /*
        //remove current segment if robot is too far away to make an arc to it
        //probably not needed
        if (robotToClosestPoint.norm() > distanceRemainingOnSegment) {
            System.out.println("REMOVING SEGMENT");
            System.out.println("robot to point distance: "+robotToClosestPoint.norm());
            System.out.println("remaining distance: " + distanceRemainingOnSegment);
            segments.remove(0);
            return(update(robotCoordinates));
        }
        */

        //determine lookahead point
        rv.lookaheadPoint = currSegment.getLookAheadPoint(rv.lookaheadDistance + rv.distanceToPath, closestPoint);

        for (Segment s : segments.subList(currSegmentNumber, segments.size())) {
            rv.remainingDistance += s.getLength();
        }
        rv.remainingDistance -= currSegment.getCurrDistanceTraveled(closestPoint);
        rv.currSegment = currSegment;
        rv.closestPoint = closestPoint;

        prevPathUpdate = rv;

        return rv;
    }

}
