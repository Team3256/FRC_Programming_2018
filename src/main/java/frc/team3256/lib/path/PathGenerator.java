package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;

import java.util.List;

public class PathGenerator {

    public static class Waypoint{

        Translation position;
        double radius;

        public Waypoint(double x, double y, double radius){
            this.position = new Translation(x, y);
            this.radius = radius;
        }
    }

    public static class LineGenerator{
        Waypoint a, b;
        Translation start, end, slope;

        public LineGenerator(Waypoint a, Waypoint b){
            this.a = a;
            this.b = b;
            slope = new Translation(a.position, b.position);
            //determine the start and end points of this segment:
            //if this line is connected to an arc, then this line needs to be shorter
            //so we adjust the start and end points respectively
            this.start = a.position.translate(slope.scale(a.radius/slope.norm()));
            this.end = b.position.translate(slope.scale(-1.0*b.radius/slope.norm()));
        }

        private void addToPath(Path path){
            double len = new Translation(start, end).norm();
            if (len > 1E-9){
                path.addSegment(new Line(start.x(), start.y(), end.x(), end.y()));
            }
        }
    }

    public static class ArcGenerator{
        LineGenerator a, b;
        Translation center;
        double radius;

        public ArcGenerator(LineGenerator a, LineGenerator b){
            this.a = a;
            this.b = b;
            //find center of the arc by intersecting two lines
            RigidTransform normalLineOne = new RigidTransform(a.end, new Rotation(a.slope).normal());
            RigidTransform normalLineTwo = new RigidTransform(b.start, new Rotation(b.slope).normal());
            this.center = normalLineOne.intersection(normalLineTwo);
            this.radius = new Translation(center, a.end).norm();
        }

        public ArcGenerator(Waypoint a, Waypoint b, Waypoint c){
            this(new LineGenerator(a, b), new LineGenerator(b, c));
        }

        public void addToPath(Path path){
            a.addToPath(path);
            if (radius > 1E-9 && radius < 1E9){
                path.addSegment(new Arc(a.end.x(), a.end.y(), b.start.x(), b.start.y(),
                        center.x(), center.y()));
            }
        }
    }

    public static Path generate(List<Waypoint> waypoints){
        Path path = new Path();
        for(int i=0; i<waypoints.size()-2; i++){
            new ArcGenerator(waypoints.get(i), waypoints.get(i+1), waypoints.get(i+2)).addToPath(path);
        }
        new LineGenerator(waypoints.get(waypoints.size()-2), waypoints.get(waypoints.size()-1)).addToPath(path);
        return path;
    }
}
