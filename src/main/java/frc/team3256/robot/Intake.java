package frc.team3256.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake{

    private static Intake instance;
    private VictorSP rollerLeft;
    private VictorSP rollerRight;
    private DoubleSolenoid flopper;

    private boolean isClosed = true;

    public static Intake getInstance(){
        return instance == null ? instance = new Intake() : instance;
    }

    private Intake(){
        rollerLeft = new VictorSP(Constants.kIntakeLeft);
        rollerRight = new VictorSP(Constants.kIntakeRight);
        flopper = new DoubleSolenoid(Constants.kIntakeFlopA, Constants.kIntakeFlopB);
    }

    public void runMotors(double val){
	    rollerLeft.set(val);
	    rollerRight.set(-val);
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
