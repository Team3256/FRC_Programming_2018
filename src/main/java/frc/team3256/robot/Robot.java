package frc.team3256.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import frc.team3256.lib.hardware.PressureSensor;
import frc.team3256.lib.hardware.SharpIR;


public class Robot extends IterativeRobot{

    TalonSRX talonZero, talonOne, talonTwo, talonThree,
        talonFour, talonFive, talonSix, talonSeven;

    Solenoid solenoidZero, solenoidOne, solenoidTwo, solenoidThree,
        solenoidFour, solenoidFive, solenoidSix, solenoidSeven;

    SharpIR testIR;
    PressureSensor testSensor;

    double delay = 1.0;

    @Override
    public void robotInit(){
        talonZero = new TalonSRX(0);
        talonOne = new TalonSRX(1);
        talonTwo = new TalonSRX(2);
        talonThree = new TalonSRX(3);
        talonFour = new TalonSRX(4);
        talonFive = new TalonSRX(5);
        talonSix = new TalonSRX(6);
        talonSeven = new TalonSRX(7);

        solenoidZero = new Solenoid(0);
        solenoidOne = new Solenoid(1);
        solenoidTwo = new Solenoid(2);
        solenoidThree = new Solenoid(3);
        solenoidFour = new Solenoid(4);
        solenoidFive = new Solenoid(5);
        solenoidSix = new Solenoid(6);
        solenoidSeven = new Solenoid(7);

    }

    @Override
    public void testInit(){
        System.out.println("TESTING TALONS\n");
        testTalon(talonZero, delay);
        testTalon(talonOne, delay);
        testTalon(talonTwo, delay);
        testTalon(talonThree, delay);
        testTalon(talonFour, delay);
        testTalon(talonFive, delay);
        testTalon(talonSix, delay);
        testTalon(talonSeven, delay);
        System.out.println("TESTING SOLENOIDS\n");
        testSolenoid(solenoidZero, 0, delay);
        testSolenoid(solenoidOne, 1, delay);
        testSolenoid(solenoidTwo, 2, delay);
        testSolenoid(solenoidThree, 3, delay);
        testSolenoid(solenoidFour, 4, delay);
        testSolenoid(solenoidFive, 5, delay);
        testSolenoid(solenoidSix, 6, delay);
        testSolenoid(solenoidSeven, 7, delay);
    }

    @Override
    public void teleopInit(){
        testIR = new SharpIR(0, 1, 3);

    }

    @Override
    public void teleopPeriodic(){
        System.out.println(testIR.getDistance());

    }

    private void testTalon(TalonSRX talon, double delay){
        int id = talon.getDeviceID();
        System.out.println("Talon " + id + ": Full Throttle");
        talon.set(ControlMode.PercentOutput, 1.0);
        Timer.delay(delay);
        System.out.println("Talon " + id + ": Full Reverse");
        talon.set(ControlMode.PercentOutput, -1.0);
        Timer.delay(delay);
        System.out.println("Talon " + id + ": Half Throttle");
        talon.set(ControlMode.PercentOutput, 0.5);
        Timer.delay(delay);
        System.out.println("Talon " + id + ": Half Reverse");
        talon.set(ControlMode.PercentOutput, -0.5);
        Timer.delay(delay);
        System.out.println("Talon " + id + ": Zero Throttle");
        talon.set(ControlMode.PercentOutput, 0.0);
        Timer.delay(delay);
    }

    private void testSolenoid(Solenoid solenoid, int id, double delay){
        System.out.println("Solenoid " + id + ": ON");
        solenoid.set(true);
        Timer.delay(delay);
        System.out.println("Solenoid " + id + ": OFF");
        solenoid.set(false);
        Timer.delay(delay);
    }
}
