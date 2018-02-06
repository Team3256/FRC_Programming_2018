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
    private DoubleSolenoid squeeze;

    private boolean squeezing = false;

    private Elevator(){
        one = new VictorSP(Constants.kElevatorOne);
        two = new VictorSP(Constants.kElevatorTwo);
        squeeze = new DoubleSolenoid(Constants.kCarriageSqueezeA, Constants.kCarriageSqueezeB);
    }

    public void runElevator(double power){
        one.set(power);
        two.set(power);
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
