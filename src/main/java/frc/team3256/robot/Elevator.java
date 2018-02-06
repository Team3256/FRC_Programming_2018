package frc.team3256.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.VictorSP;

public class Elevator {

    private static Elevator instance;

    public static Elevator getInstance(){
        return instance == null ? instance = new Elevator() : instance;
    }

    private VictorSP one;
    private VictorSP two;
    private VictorSP leftRoller;
    private VictorSP rightRoller;
    private DoubleSolenoid squeeze;

    private boolean squeezing = false;

    private Elevator(){
        one = new VictorSP(Constants.kElevatorOne);
        two = new VictorSP(Constants.kElevatorTwo);
        leftRoller = new VictorSP(Constants.kCarriageLeft);
        rightRoller= new VictorSP(Constants.kCarriageRight);
        squeeze = new DoubleSolenoid(Constants.kCarriageSqueezeA, Constants.kCarriageSqueezeB);

        leftRoller.setInverted(true);
        rightRoller.setInverted(false);
    }

    public void runElevator(double power){
        one.set(power);
        two.set(power);
    }

    public void runRoller(double power){
        leftRoller.set(power);
        rightRoller.set(power);
    }

    public void squeeze(){
        squeezing = true;
        squeeze.set(Value.kForward);
    }

    public void open(){
        squeezing = false;
        squeeze.set(Value.kReverse);
    }

    public boolean isSqueezing(){
        return squeezing;
    }
}
