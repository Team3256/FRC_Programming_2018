package frc.team3256.lib.control;

import org.junit.Test;

import static org.junit.Assert.*;

public class TeleopDriveControllerTest {

    private final double kEpsilon = 1E-9;

    @Test
    public void testClamp(){
        assertEquals(TeleopDriveController.clamp(1.1), 1.0, kEpsilon);
        assertEquals(TeleopDriveController.clamp( -1.5), -1.0, kEpsilon);
        assertEquals(TeleopDriveController.clamp( 0.5), 0.5, kEpsilon);
        assertEquals(TeleopDriveController.clamp( -0.5), -0.5, kEpsilon);
    }

    @Test
    public void testHandleDeadband(){
        double deadband = 0.2;
        assertEquals(TeleopDriveController.handleDeadband(0.19, deadband), 0.0, kEpsilon);
        assertEquals(TeleopDriveController.handleDeadband(-0.19, deadband), 0.0, kEpsilon);
        assertEquals(TeleopDriveController.handleDeadband(0.6, deadband), 0.5, kEpsilon);
        assertEquals(TeleopDriveController.handleDeadband(-0.6, deadband), -0.5, kEpsilon);
    }
}