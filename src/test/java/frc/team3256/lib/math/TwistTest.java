package frc.team3256.lib.math;

import junit.framework.TestCase;
import org.junit.Test;

public class TwistTest extends TestCase {

    private final double kEpsilon = 1E-9;

    @Test
    public void testConstructor(){
        Twist twist = new Twist();
        assertEquals(twist.dx(), 0, kEpsilon);
        assertEquals(twist.dy(), 0, kEpsilon);
        assertEquals(twist.dtheta(), 0, kEpsilon);
        twist = new Twist(10.0, 1.0, 5.0);
        assertEquals(twist.dx(), 10, kEpsilon);
        assertEquals(twist.dy(), 1, kEpsilon);
        assertEquals(twist.dtheta(), 5, kEpsilon);
    }

    @Test
    public void testScale(){
        Twist twist = new Twist(10.0, 1.0, 5.0);
        twist = twist.scale(2.0);
        assertEquals(twist.dx(), 20.0, kEpsilon);
        assertEquals(twist.dy(), 2.0, kEpsilon);
        assertEquals(twist.dtheta(), 10.0, kEpsilon);
    }

    @Test
    public void testExp(){
        Twist twist = new Twist(1, 0, 0);
        RigidTransform pose = RigidTransform.exp(twist);
        assertEquals(pose.getTranslation().x(), 1.0);
        assertEquals(pose.getTranslation().y(), 0.0);
        assertEquals(pose.getRotation().radians(), 0.0);
    }
}