package frc.team3256.lib.path;

import frc.team3256.lib.math.RigidTransform;
import frc.team3256.lib.math.Rotation;
import frc.team3256.lib.math.Translation;

public abstract class Segment {

    protected Type type = null;
    protected Translation start = null;
    protected Translation end = null;
    protected double goalVel = 0;
    protected double maxAccel = 0;
    protected double maxVel = 0;

    //Type of segment: is this an arc or a line?
    public enum Type{
        LINE,
        ARC
    }

    //Methods that are common to both types of segments

    public abstract Type getType();

    public abstract Translation getStartPoint();

    public abstract Translation getEndPoint();

    public abstract double getLength();

    public abstract Translation getClosestPointOnSegment(Translation position);

    public abstract Translation getDirection(Translation lookaheadPoint);

    public abstract Translation getLookAheadPoint(double lookaheadDistance, Translation position);

    public abstract double getCurrDistanceTraveled(Translation closestPoint);

    public abstract double getRemainingDistance(Translation closestPoint);

    public abstract double checkVelocity(Translation closestPoint, double prevVelocity, double dt);
}
