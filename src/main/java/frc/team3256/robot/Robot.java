package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends IterativeRobot {

    Elevator elevator = Elevator.getInstance();
    Intake intake = Intake.getInstance();

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
        System.out.println("CLE: " + elevator.getClosedLoopError());
        boolean in = joystick.getRawAxis(3) > 0.25;
        boolean out = joystick.getRawAxis(2) > 0.25;
        boolean preset = joystick.getRawButton(4);
        if (Math.abs(joystick.getRawAxis(1)) > 0.05 ){
            elevator.setOpenLoop(-joystick.getRawAxis(1));
        }
        else if (preset){
            System.out.println("PRESET");
            elevator.midPreset();
        }
        else{
            elevator.setOpenLoop(0);
        }
        if (in && out){
            intake.stop();
        }
        else if (in){
            intake.intake();
        }
        else if (out){
            intake.outtake();
        }
        else intake.stop();
        System.out.println("HEIGHT: " + elevator.getAbsoluteHeight());
        System.out.println(elevator.getMasterVoltage() + " " + elevator.getSlaveVoltage());
    }

    @Override
    public void testPeriodic() {

    }

}
