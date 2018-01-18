package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.robot.Constants;

public class ElevatorCarriage extends SubsystemBase{

    private VictorSP rollerLeft, rollerRight;
    private DoubleSolenoid squeezeSolenoid;
    private static ElevatorCarriage elevatorCarriage;
    private CarriageState carriageState;
    private CarriageState previousState;

    public enum CarriageState {
        ROLLING_FORWARD,
        ROLLING_BACKWARD,
        SQUEEZING_IDLE,
    }

    public void update(){
        switch(carriageState){
            case ROLLING_FORWARD:
                if (previousState != carriageState.ROLLING_FORWARD){
                    previousState = carriageState.ROLLING_FORWARD;
                }
                rollerLeft.set(Constants.rollForwardMotorPower);
                rollerRight.set(Constants.rollForwardMotorPower);
                break;
            case ROLLING_BACKWARD:
                if (previousState != carriageState.ROLLING_BACKWARD){
                    previousState = carriageState.ROLLING_BACKWARD;
                }
                rollerLeft.set(Constants.rollBackwardMotorPower);
                rollerRight.set(Constants.rollBackwardMotorPower);
                break;
            case SQUEEZING_IDLE:
                if (previousState != carriageState.SQUEEZING_IDLE){
                    previousState = carriageState.SQUEEZING_IDLE;
                }
                rollerLeft.set(0);
                rollerRight.set(0);
                squeezeSolenoid.set(DoubleSolenoid.Value.kForward);
                break;
        }
    }

    private ElevatorCarriage() {
        rollerLeft = new VictorSP(Constants.kCarriageRollerLeft);
        rollerRight = new VictorSP(Constants.kCarriageRollerRight);
        squeezeSolenoid = new DoubleSolenoid(Constants.kCarriageSqueezeForward, Constants.kCarriageSqueezeReverse);
    }

    public static ElevatorCarriage getInstance(){
        return elevatorCarriage == null ? elevatorCarriage = new ElevatorCarriage(): elevatorCarriage;
    }

    public void setState(CarriageState wantedState){
        carriageState = wantedState;
    }


    @Override
    public void outputToDashboard() {

    }

    @Override
    public void selfTest() {

    }

    @Override
    public void zeroSensors() {

    }
}
