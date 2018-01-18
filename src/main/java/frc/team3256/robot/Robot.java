package frc.team3256.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
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

    DoubleSolenoid solenoidOne;

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
        solenoidOne = new DoubleSolenoid(0,7);
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

    boolean squeeezing = false;
    @Override
    public void teleopPeriodic() {
        boolean A = OI.getA();
        if (A){
            squeeezing = !squeeezing;
        }
        solenoidOne.set(squeeezing ? DoubleSolenoid.Value.kForward : DoubleSolenoid.Value.kReverse);
        boolean intake = OI.rightTrigger();
        boolean outtake = OI.leftTrigger();
        double power = 0.5;
        if (intake){
            motorTwo.set(power);
        }
        else if (outtake){
            motorTwo.set(-power);
        }
        else motorTwo.set(0);
    }

    @Override
    public void testPeriodic() { }
}