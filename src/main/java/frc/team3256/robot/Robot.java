package frc.team3256.robot;

import com.sun.corba.se.impl.orbutil.closure.Constant;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
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
        if (OI.presetOne()){
            motorOne.set(Constants.kPresetOnePower);
        } else motorOne.set(0);

        if (OI.presetTwo()){
            motorTwo.set(Constants.kPresetOnePower);
        } else motorTwo.set(0);

        if (OI.presetThree()){
            motorThree.set(Constants.kPresetOnePower);
        } else motorThree.set(0);

        if (OI.presetFour()){
            motorFour.set(Constants.kPresetOnePower);
        } else motorFour.set(0);

        if (OI.presetFive()){
            motorFive.set(Constants.kPresetOnePower);
        } else motorFive.set(0);

        if (OI.presetSix()){
            motorSix.set(Constants.kPresetOnePower);
        } else motorSix.set(0);

        if (OI.presetSeven()){
            motorSeven.set(Constants.kPresetOnePower);
        } else motorSeven.set(0);

        if (OI.presetEight()){
            motorEight.set(Constants.kPresetOnePower);
        } else motorEight.set(0);
    }

    @Override
    public void testPeriodic() { }
}