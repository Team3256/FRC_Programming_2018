package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.VictorSP;

public class Robot extends IterativeRobot {

    VictorSP motorOne,
             motorTwo,
             motorThree,
             motorFour,
             motorFive,
             motorSix,
             motorSeven,
             motorEight;

    @Override
    public void robotInit() {
        motorOne = new VictorSP(Constants.kPortOne);
        motorTwo = new VictorSP(Constants.kPortTwo);
        motorThree = new VictorSP(Constants.kPortThree);
        motorFour = new VictorSP(Constants.kPortFour);
        motorFive = new VictorSP(Constants.kPortFive);
        motorSix = new VictorSP(Constants.kPortSix);
        motorSeven = new VictorSP(Constants.kPortSeven);
        motorEight = new VictorSP(Constants.kPortEight);
    }

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
        double val = OI.getLeftY();
        motorSeven.set(val);
        motorEight.set(-val);
    }

    @Override
    public void testPeriodic() { }
}