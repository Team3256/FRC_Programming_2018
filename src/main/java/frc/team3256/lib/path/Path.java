package frc.team3256.lib.path;

import java.util.ArrayList;

public class Path {

    private ArrayList<Segment> segments;

    public Path(){
        segments = new ArrayList<>();
    }

    public void addSegment(Segment segment){
        segments.add(segment);
    }

}
