package frc.team3256.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class RotationTest {
    private final double kEpsilon = 1E-9;

    @Test
    public void testConstructor(){
        Rotation rotation = new Rotation();
        assertEquals(rotation.radians(), 0.0, kEpsilon);
        rotation = new Rotation(Math.sqrt(2)/2, Math.sqrt(2)/2);
        assertEquals(rotation.degrees(), 45, kEpsilon);
        rotation = new Rotation(5, 5, true);
        assertEquals(rotation.degrees(), 45, kEpsilon);
    }

    @Test
    public void testRadians(){
        Rotation rotation = new Rotation(2.0, 2*Math.sqrt(3));
        assertEquals(rotation.radians(), Math.PI/3,kEpsilon);
    }

    @Test
    public void testDegrees(){
        Rotation rotation = new Rotation(2.0, 2*Math.sqrt(3));
        assertEquals(rotation.degrees(), 60.0,kEpsilon);
    }

    @Test
    public void testTan(){
        Rotation rotation = new Rotation(2.0, 2*Math.sqrt(3));
        assertEquals(rotation.tan(), Math.sqrt(3),kEpsilon);
    }

    @Test
    public void testNormal(){
        Rotation rotation = new Rotation(2.0, 2*Math.sqrt(3));
        rotation = rotation.normal();
        assertEquals(rotation.radians(), Math.PI*5/6.0,kEpsilon);
    }

    @Test
    public void testRotate() {
        Rotation rotation = Rotation.fromDegrees(-10.0);
        rotation = rotation.rotate(Rotation.fromDegrees(90.0));
        assertEquals(rotation.degrees(), 80.0, kEpsilon);
    }

    @Test
    public void testInverse(){
        Rotation rotation = Rotation.fromDegrees(-100.0);
        rotation = rotation.inverse();
        assertEquals(rotation.degrees(), 100.0, kEpsilon);
    }

    @Test
    public void testParallel(){
        Rotation rotation = new Rotation(1.0, -2.0);
        assertTrue(rotation.isParallel(new Rotation(-2.0, 4.0)));
    }

    @Test
    public void testTranslation(){
        Rotation rotation = new Rotation(2.0, 3.0);
        Translation translation = rotation.toTranslation();
        assertEquals(translation.x(),2.0,kEpsilon);
        assertEquals(translation.y(),3.0,kEpsilon);
    }

}