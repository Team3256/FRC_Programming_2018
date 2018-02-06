package frc.team3256.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake{

    private static Intake instance;
    private VictorSP leftRoller;
    private VictorSP rightRoller;
    private DoubleSolenoid flopper;

    private boolean isClosed = true;

    public static Intake getInstance(){
        return instance == null ? instance = new Intake() : instance;
    }

    private Intake(){
        leftRoller = new VictorSP(Constants.kIntakeLeft);
        rightRoller = new VictorSP(Constants.kIntakeRight);
        flopper = new DoubleSolenoid(Constants.kIntakeFlopA, Constants.kIntakeFlopB);

        leftRoller.setInverted(true);
        rightRoller.setInverted(false);
    }

    public void runMotors(double l, double r){
        leftRoller.set(l);
        rightRoller.set(r);
    }

    public void open(){
        flopper.set(Value.kForward);
        isClosed = false;
    }

    public void close(){
        flopper.set(Value.kReverse);
        isClosed = true;
    }

    public boolean isClosed(){
        return isClosed;
    }

}
