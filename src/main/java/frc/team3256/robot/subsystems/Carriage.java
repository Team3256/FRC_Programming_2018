package frc.team3256.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import frc.team3256.lib.hardware.SharpIR;
import frc.team3256.lib.hardware.TalonUtil;
import frc.team3256.robot.Constants;
import org.omg.PortableInterceptor.HOLDING;

public class Carriage {

    private TalonSRX pivotArm;
    private VictorSP rightIntake, leftIntake;
    private DoubleSolenoid flopper;
    private SharpIR cubeDetector;
    private DigitalInput hallEffect;

    private SystemState currentState = SystemState.STOWED;
    private SystemState previousState = currentState;
    private WantedState wantedState;
    private WantedState prevWantedState;

    private boolean stateChanged = true;
    private boolean isHomed = false;
    private boolean wantedStateChanged = true;

    private double unjamTimeStart;
    private SystemState unjamPreviousState;
    private boolean wantsToToggle = false;
    private double m_closedLoopTarget;
    private boolean m_usingClosedLoop;

    private double homingTimeStart;

    private double kLeftIntakePower = Constants.kLeftIntakePower;
    private double kRightIntakePower = Constants.kRightIntakePower;
    private double kIntakeExhaustPower = Constants.kIntakeExhaustPower;
    private double kUnjamMaxDuration = Constants.kUnjamMaxDuration;

    private static Carriage instance;

    public static Carriage getInstance() {
        return instance == null ? instance = new Carriage() : instance;
    }

    public enum SystemState{
        INTAKING,
        EXHASTING,
        UNJAMMING,

        STOWED,
        DEPLOYED_FRONT,
        DEPLOYED_BACK,
        DEPLOYED_BACK_ANGLED,

        SCORING_FORWARD,
        SCORING_REVERSE,
        SCORING_SLOW_FORWARD,
        SCORINT_SLOW_REVERSE,

        MANUAL_FORWARD,
        MANUAL_REVERSE,

        OPENED,
        CLOSED,

        HOMING,
    }

    public enum WantedState{
        WANTS_TO_INTAKE,
        WANTS_TO_EXHAUST,
        WANTS_TO_UNJAM,

        WANTS_TO_STOW,
        FRONT_PRESET,
        BACK_PRESET,
        FRONT_SHOOT_PRESET,
        BACK_SHOOT_PRESET,

        WANTS_TO_SCORE_FORWARD,
        WANTS_TO_SCORE_SLOW_FORWARD,
        WANTS_TO_SCORE_REVERSE,
        WANTS_TO_SCORE_SLOW_REVERSE,

        WANTS_TO_MANUAL_FORWARD,
        WANTS_TO_MANUAL_REVERSE,

        WANTS_TO_OPEN,
        WANTS_TO_CLOSE,

        WANTS_TO_HOME,
    }

    private Carriage() {

        pivotArm = TalonUtil.generateGenericTalon(Constants.kPivotArm);
        TalonUtil.configMagEncoder(pivotArm);
        pivotArm.setSelectedSensorPosition(0, 0, 0);
        
        //configure hold PID values
        TalonUtil.setPIDGains(pivotArm, Constants.kArmHoldSlot, Constants.kArmHoldP,
                Constants.kArmHoldI, Constants.kArmHoldD, 0);
        //configure moving PID values
        TalonUtil.setPIDGains(pivotArm, Constants.kArmMovingSlot, Constants.kArmMovingP,
                Constants.kArmMovingI, Constants.kArmMovingD, 0);

        //voltage limiting
        TalonUtil.setPeakOutput(Constants.kArmMaxForwardVoltage/12.0,
                Constants.kArmMaxReverseVoltage/12.0, pivotArm);
        TalonUtil.setMinOutput(Constants.kArmMinHoldVoltage/12.0,
                0, pivotArm);

        //soft limits
        pivotArm.configForwardSoftLimitThreshold((int)(angleToSensorUnits(Constants.kArmMaxAngle)), 0);
        pivotArm.configReverseSoftLimitThreshold((int)(angleToSensorUnits(Constants.kArmMinAngle)), 0);
        pivotArm.configForwardSoftLimitEnable(false, 0);
        pivotArm.configReverseSoftLimitEnable(false,0);

        TalonUtil.setCoastMode(pivotArm);
        

        leftIntake = new VictorSP(Constants.kLeftIntakePort);
        rightIntake = new VictorSP(Constants.kRightIntakePort);

        flopper = new DoubleSolenoid(Constants.kIntakeFlopForward, Constants.kIntakeFlopReverse);

        cubeDetector = new SharpIR(Constants.kIntakeSharpIR, Constants.kIntakeSharpIRMinVoltage, Constants.kIntakeSharpIRMaxVoltage);

        leftIntake.setInverted(true);
        rightIntake.setInverted(false);
    }

    public WantedState getWantedState() { return wantedState; }

    public SystemState getCurrentState() { return currentState; }

    //TODO: fix formulas for angle to sensor units and sensor units to angle
    private double angleToSensorUnits(double degrees) {
        return degrees * 4096.0 * Constants.kArmGearRatio;
    }
    private double sensorUnitsToAngle(double ticks) {
        return ticks / 4096.0 / Constants.kArmGearRatio;
    }

    public void setWantedState(WantedState wantedState){
        this.wantedState = wantedState;
    }

}
