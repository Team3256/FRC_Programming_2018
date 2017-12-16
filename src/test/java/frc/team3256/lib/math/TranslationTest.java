package frc.team3256.lib.math;

import org.junit.Test;

import static org.junit.Assert.*;

public class TranslationTest {

    private final double kEpsilon = 1E-9;

    @Test
    public void testConstructor(){
        Translation translation = new Translation();
        assertEquals(translation.x(), 0.0, kEpsilon);
        assertEquals(translation.y(), 0.0, kEpsilon);
        translation = new Translation(8.0, 9.0);
        assertEquals(translation.x(), 8.0, kEpsilon);
        assertEquals(translation.y(), 9.0, kEpsilon);
        translation = new Translation(new Translation(1.0, 5.0), new Translation(2.0,6.0));
        assertEquals(translation.x(), 1.0, kEpsilon);
        assertEquals(translation.y(), 1.0, kEpsilon);
    }

    @Test
    public void testTranslate(){
        Translation translation = new Translation(2.0,3.0);
        translation = translation.translate(new Translation(2.0, 3.0));
        assertEquals(translation.x(), 4.0, kEpsilon);
        assertEquals(translation.y(), 6.0, kEpsilon);
    }

    @Test
    public void testRotate(){
        Translation translation = new Translation(1.0, 0.0);
        translation = translation.rotate(Rotation.fromDegrees(45));
        assertEquals(translation.x(), Math.sqrt(2)/2, kEpsilon);
        assertEquals(translation.y(), Math.sqrt(2)/2, kEpsilon);
    }

    @Test
    public void testInverse(){
        Translation translation = new Translation(2.0, 3.0);
        translation = translation.inverse();
        assertEquals(translation.x(), -2.0, kEpsilon);
        assertEquals(translation.y(), -3.0, kEpsilon);
    }

    @Test
    public void testNorm(){
        Translation translation = new Translation(-3.0, -4.0);
        assertEquals(translation.norm(), 5.0, kEpsilon);
    }

    @Test
    public void testDotProduct(){
        Translation translation = new Translation(2.0, 3.0);
        assertEquals(translation.dot(new Translation(1.0, 2.0)), 8.0, kEpsilon);
    }

    @Test
    public void testCrossProduct(){
    }

    @Test
    public void testScale(){
        Translation translation = new Translation(2.0, 3.0);
        translation = translation.scale(-2.0);
        assertEquals(translation.x(), -4.0, kEpsilon);
        assertEquals(translation.y(), -6.0, kEpsilon);
    }

    @Test
    public void testDirection(){
        Translation translation = new Translation(-5.0,-5.0);
        assertEquals(translation.direction().degrees(), -135.0, kEpsilon);
    }

    @Test
    public void testAngleBetween(){
        Translation translation = new Translation(3.0, 4.0);
        Rotation angle = translation.getAngle(new Translation(-5.0, 12.0));
        assertEquals(angle.degrees(), 59.489762593884, kEpsilon);
    }
}