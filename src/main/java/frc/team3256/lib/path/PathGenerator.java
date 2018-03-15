package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;
import frc.team3256.robot.Constants;

import java.util.ArrayList;
import java.util.List;

public class PathGenerator {

    static double maxAccel;
    static double maxVel;

    public static class Waypoint{

        Translation position;
        double radius;
        double vel;
        double arcLength;

        public Waypoint(double x, double y, double radius, double vel){
            this.position = new Translation(x, y);
            this.radius = radius;
            this.vel = vel;
        }

        public Waypoint(double x, double y, double radius){
            this(x, y, radius, 0.0);
        }

        public void setArcLength(double arcLength) {
            this.arcLength = arcLength;
        }
    }

    public static class LineGenerator{
        Waypoint a, b;
        Translation start, end, slope;
        double startVel, endVel;

        public LineGenerator(Waypoint a, Waypoint b){
            this.a = a;
            this.b = b;
            slope = new Translation(a.position, b.position);
            //determine the start and end points of this segment:
            //if this line is connected to an arc, then this line needs to be shorter
            //so we adjust the start and end points respectively
            Translation startDiff = slope.scale(a.radius/slope.norm());
            Translation endDiff = slope.scale(-1.0*b.radius/slope.norm());
            this.start = a.position.translate(startDiff);
            this.end = b.position.translate(endDiff);
        }

        private void addToPath(Path path){
            double len = new Translation(start, end).norm();
            if (len > 1E-9){
                path.addSegment(new Line(start.x(), start.y(), end.x(), end.y(), endVel, maxAccel, maxVel));
            }
        }

        private void updateVel() {
            double totalLength = new Translation(this.start, this.end).norm() + a.arcLength / 2 + b.arcLength / 2;
            double startScale = (a.arcLength / 2) / totalLength;
            double endScale = (b.arcLength / 2) / totalLength;
            this.startVel = a.vel + (b.vel - a.vel) * startScale;
            this.endVel = b.vel - (b.vel - a.vel) * endScale;
        }
    }

    public static class ArcGenerator{
        LineGenerator a, b;
        Translation center;
        double radius;
        double startVel, endVel;
        double angle;

        private ArcGenerator(LineGenerator a, LineGenerator b){
            this.a = a;
            this.b = b;
            //find center of the arc by intersecting two lines
            RigidTransform normalLineOne = new RigidTransform(a.end, new Rotation(a.slope).normal());
            RigidTransform normalLineTwo = new RigidTransform(b.start, new Rotation(b.slope).normal());
            this.center = normalLineOne.intersection(normalLineTwo);

            this.angle = Math.abs(normalLineOne.getRotation().radians() - normalLineTwo.getRotation().radians());
            this.radius = new Translation(center, a.end).norm();

        }

        public ArcGenerator(Waypoint a, Waypoint b, Waypoint c){
            this(new LineGenerator(a, b), new LineGenerator(b, c));
            b.setArcLength(angle * radius);
            this.a.updateVel();
            this.b.updateVel();
        }

        private void addToPath(Path path){
            a.addToPath(path);
            if (radius > 1E-9 && radius < 1E9){
                path.addSegment(new Arc(a.end.x(), a.end.y(), b.start.x(), b.start.y(),
                        center.x(), center.y(), endVel, maxAccel, maxVel));
            }
        }

        private void updateVel() {
            this.startVel = a.endVel;
            this.endVel = b.startVel;
        }

    }

    public static Path generate(List<Waypoint> waypoints, double maxAccel, double maxVel){
        PathGenerator.maxAccel = maxAccel;
        PathGenerator.maxVel = maxVel;
        Path path = new Path();
        ArrayList<ArcGenerator> generators = new ArrayList<>(waypoints.size() - 2);
        for(int i=0; i<waypoints.size()-2; i++){
            generators.add(new ArcGenerator(waypoints.get(i), waypoints.get(i+1), waypoints.get(i+2)));
        }
        for(ArcGenerator g : generators) {
            g.updateVel();
            g.addToPath(path);
        }
        new LineGenerator(waypoints.get(waypoints.size()-2), waypoints.get(waypoints.size()-1)).addToPath(path);
        return path;
    }
}
