package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
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
    }

    public PathUpdate update(Translation robotCoordinates){
        PathUpdate rv = new PathUpdate();
        return rv;
    }

}
