package frc.team3256.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class RigidTransformTest {

    private static final double kEpsilon = 1E-9;

    @Test
    public void testConstructor(){
        RigidTransform pose = new RigidTransform();
        assertEquals(pose.getTranslation().x(), 0, kEpsilon);
        assertEquals(pose.getTranslation().y(), 0, kEpsilon);
        assertEquals(pose.getRotation().radians(), 0, kEpsilon);
        pose = new RigidTransform(new Translation(10.0, 15.0), Rotation.fromDegrees(45.0));
        assertEquals(pose.getTranslation().x(), 10.0, kEpsilon);
        assertEquals(pose.getTranslation().y(), 15.0, kEpsilon);
        assertEquals(pose.getRotation().radians(), Math.PI/4.0, kEpsilon);
    }

    @Test
    public void testTransform(){
        RigidTransform pose = new RigidTransform();
        RigidTransform delta_pose = new RigidTransform(new Translation(10.0, 15.0), Rotation.fromDegrees(45.0));
        RigidTransform newPose = pose.transform(delta_pose);
        assertEquals(newPose.getTranslation().x(), 10.0, kEpsilon);
        assertEquals(newPose.getTranslation().y(), 15.0, kEpsilon);
        assertEquals(newPose.getRotation().radians(), Math.PI/4.0, kEpsilon);

        pose = new RigidTransform(new Translation(10.0, 10.0), Rotation.fromDegrees(45.0));
        newPose = pose.transform(delta_pose);
        //Used this link to verify: http://www.wolframalpha.com/widgets/view.jsp?id=bd71841fce4a834c804930bd48e7b6cf
        assertEquals(newPose.getTranslation().x(), 10.0-(25.0/Math.sqrt(2.0))+10.0*Math.sqrt(2.0), kEpsilon);
        assertEquals(newPose.getTranslation().y(), 25.0/Math.sqrt(2.0)+10.0*Math.sqrt(2.0)-10.0*(-1.0+Math.sqrt(2)), kEpsilon);
        assertEquals(newPose.getRotation().radians(), Math.PI/4.0+Math.PI/4.0, kEpsilon);
    }

    @Test
    public void testInverse(){
        RigidTransform pose = new RigidTransform(new Translation(10.0, 15.0), Rotation.fromDegrees(45.0));
        RigidTransform inverse = pose.inverse();
        pose = pose.transform(inverse);
        assertEquals(pose.getTranslation().x(), 0, kEpsilon);
        assertEquals(pose.getTranslation().y(), 0, kEpsilon);
        assertEquals(pose.getRotation().radians(), 0, kEpsilon);
    }

    @Test
    public void testIntersection(){
        RigidTransform pose = new RigidTransform();
        RigidTransform otherPose = new RigidTransform(new Translation(10.0, 15.0), Rotation.fromDegrees(45.0));
        Translation intersectionPoint = pose.intersection(otherPose);
        assertEquals(intersectionPoint.x(), -5.0, kEpsilon);
        assertEquals(intersectionPoint.y(), 0.0, kEpsilon);

        pose = new RigidTransform(new Translation(48.0, 24.0), Rotation.fromDegrees(45.0));
        otherPose = new RigidTransform(new Translation(48.0, 28.0), Rotation.fromDegrees(-45.0));
        intersectionPoint = pose.intersection(otherPose);
        assertEquals(intersectionPoint.x(), 50.0, kEpsilon);
        assertEquals(intersectionPoint.y(), 26.0, kEpsilon);
    }

    @Test
    public void testIsColinear(){
        RigidTransform pose = new RigidTransform();
        RigidTransform otherPose = new RigidTransform(new Translation(10.0, 15.0), Rotation.fromDegrees(45.0));
        boolean isColinear = pose.isColinear(otherPose);
        assertFalse(isColinear);
        pose = new RigidTransform(new Translation(0.0, 0.0), Rotation.fromDegrees(45.0));
        otherPose = new RigidTransform(new Translation(10.0, 10.0), Rotation.fromDegrees(45.0));
        isColinear = pose.isColinear(otherPose);
        assertTrue(isColinear);

    }
}