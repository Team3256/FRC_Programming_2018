package frc.team3256.lib.trajectory;

import java.util.ArrayList;

public class Trajectory {

    public static class Point {
        private double pos;
        private double vel;
        private double acc;
        private double time;
        private Type type;
        public boolean backTurn, inverse = false;
        public double radius;

        public enum Type{
            STRAIGHT,
            ARC
        }

        public Point(double pos, double vel, double acc, double time, Type type, boolean inverse){
            this.pos = pos;
            this.vel = vel;
            this.acc = acc;
            this.time = time;
            this.type = type;
            this.inverse = inverse;
        }

        public Point(double pos, double vel, double acc, double time, Type type, boolean inverse, boolean backTurn, double radius) {
            this.backTurn = backTurn;
            this.radius = radius;
            new Point(pos, vel, acc, time, type, inverse);
        }

        public double getPos(){
            return pos;
        }

        public double getVel(){
            return vel;
        }

        public double getAcc(){
            return acc;
        }

        public double getTime(){
            return time;
        }

    }

    public ArrayList<Point> points;

    public Trajectory(int size){
        points = new ArrayList<Point>(size);
    }

    public double getLength() {
        return points.size();
    }

    public Point getCurrPoint(int index) {
        return points.get(index);
    }

    public void addPoint(int index, Point point){
        points.add(index, point);
    }

    public void addPoints(ArrayList<Point> points){
        this.points.addAll(points);
    }

    public Enum getType(){
        return points.get(0).type;
    }

    @Override
    public String toString(){
        String rv = "";
        rv += "Pos,Vel,Accel,Time\n";
        for(Point p : points){
            rv += p.pos + "," + p.time + "\n";
        }
        return rv;
    }
}
