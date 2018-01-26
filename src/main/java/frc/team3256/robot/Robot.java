package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {

    Elevator elevator = Elevator.getInstance();
    Joystick joystick = new Joystick(0);

    @Override
    public void robotInit() {

    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void testInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopPeriodic() {
        elevator.setOpenLoop(joystick.getRawAxis(1));
        System.out.println("TRIGGERED? " + elevator.isTriggered());
        System.out.println("HEIGHT: " + elevator.getAbsoluteHeight());
    }

    @Override
    public void testPeriodic() {

    }

}
