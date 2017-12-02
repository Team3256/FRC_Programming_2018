package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import frc.team3256.robot.subsystems.DriveTrain;

public class Robot extends IterativeRobot {

    @Override
    public void robotInit() { }

    @Override
    public void disabledInit() { }

    @Override
    public void autonomousInit() { }

    @Override
    public void teleopInit() { }

    @Override
    public void testInit() { }


    @Override
    public void disabledPeriodic() { }
    
    @Override
    public void autonomousPeriodic() { }

    @Override
    public void teleopPeriodic() {
        System.out.println("ENCODER: " + (int)(DriveTrain.getInstance().getLeftDistance()*1000));
    }

    @Override
    public void testPeriodic() { }
}