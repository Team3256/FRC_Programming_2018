package frc.team3256.lib;

public class Trajectory {
    public static class Segment {
        private double position, velocity, acceleration, time;

        public Segment(double position, double velocity, double acceleration, double time) {
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
            this.time = time;
        }

        public double getPosition() {
            return position;
        }

        public double getVelocity() {
            return velocity;
        }

        public double getAcceleration() {
            return acceleration;
        }

        public double getTime() {
            return time;
        }
    }

    private Segment[] trajectory;
    private int trajectoryLength;

    public Trajectory(int trajectoryLength) {
        this.trajectoryLength = trajectoryLength;
        this.trajectory = new Segment[trajectoryLength];
    }

    public void addSegment(int index, Segment segment){
        trajectory[index] = segment;
    }

    public int getLength(){
        return trajectoryLength;
    }

    public Segment getCurrentSegment(int index){
        return trajectory[index];
    }

    public String toString(){
        StringBuilder ret = new StringBuilder();
        //Position-Time Values
        for (Segment aTrajectory : trajectory) {
            ret.append(aTrajectory.time).append(" ").append(aTrajectory.getPosition()).append(" \n");
        }
        ret.append("\n\n");
        //Velocity-Time Values
        for (Segment aTrajectory : trajectory) {
            ret.append(aTrajectory.time).append(" ").append(aTrajectory.getVelocity()).append(" \n");
        }
        ret.append("\n\n");
        //Acceleration-Time Values
        for (Segment aTrajectory : trajectory) {
            ret.append(aTrajectory.time).append(" ").append(aTrajectory.getAcceleration()).append(" \n");
        }
        return ret.toString();
    }
}
