package frc.team3256.robot;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.InterruptHandlerFunction;
import edu.wpi.first.wpilibj.Timer;

public class Elevator {

    private static Elevator instance;
    private TalonSRX master, slave;
    private DigitalInput hallEffect;
    private boolean homed = false;
    private double homingTime = -1;
    private final double ELEVATOR_HOME_HEIGHT = 0.0;
    private final double ELEVATOR_LOW_HEIGHT = -30.0;
    private final double ELEVATOR_HIGH_HEIGHT = 0.0;
    private final double ELEVATOR_MIDDLE_HEIGHT = -15.0;
    private final double GEAR_RATIO = 16.0;

    public static Elevator getInstance(){
        return instance == null ? instance = new Elevator() : instance;
    }

    private InterruptHandlerFunction<Elevator> ihr = new InterruptHandlerFunction<Elevator>() {

        @Override
        public void interruptFired(int interruptAssertedMask, Elevator param) {
            if (!homed){
                master.setSelectedSensorPosition(inchesToSensorUnits(ELEVATOR_HOME_HEIGHT), 0, 0);
                homed = true;
                homingTime = Timer.getFPGATimestamp();
                hallEffect.disableInterrupts();
            }
        }
    };


    private Elevator(){
        hallEffect = new DigitalInput(9);
        hallEffect.requestInterrupts(ihr);
        hallEffect.setUpSourceEdge(false, true);
        hallEffect.enableInterrupts();

        master = new TalonSRX(1);
        slave = new TalonSRX(2);

        slave.set(ControlMode.Follower, master.getDeviceID());

        master.setNeutralMode(NeutralMode.Brake);
        slave.setNeutralMode(NeutralMode.Brake);

        //master.setSensorPhase(true);
        master.setInverted(true);
        slave.setInverted(true);

        master.configContinuousCurrentLimit(20, 0);
        master.configPeakCurrentLimit(30, 0);
        master.configPeakCurrentDuration(100, 0);
        master.enableCurrentLimit(true);
        slave.configContinuousCurrentLimit(20, 0);
        slave.configPeakCurrentLimit(30, 0);
        slave.configPeakCurrentDuration(100, 0);
        slave.enableCurrentLimit(true);

        if (master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
           0, 0) != ErrorCode.OK){
            DriverStation.reportError("ENCODER NOT DETECTED", false);
        }

        master.config_kP(0, 0.015, 0);
        master.config_kI(0, 0.0, 0);
        master.config_kD(0, 0.0, 0);
        master.configPeakOutputForward(4.0/12.0, 0);
        master.configPeakOutputReverse(-4.0/12.0, 0);
        //master.configNominalOutputForward(0.5/12.0, 0);
        //master.configNominalOutputReverse(0.5/12.0, 0);
    }

    public void setOpenLoop(double value){
        master.set(ControlMode.PercentOutput, value);
    }

    public double getHomingTime(){
        return homingTime;
    }

    public boolean isTriggered(){
        return !hallEffect.get();
    }

    public void midPreset(){
        if (homed) master.set(ControlMode.Position, inchesToSensorUnits(ELEVATOR_MIDDLE_HEIGHT), 0);
    }

    public boolean isCalibrated(){
        return homed;
    }

    public double getRelativeHeight(){
        return sensorUnitsToInches(getSensorUnitsRelativeHeight());
    }

    public double getSensorUnitsRelativeHeight(){
        return master.getSelectedSensorPosition(0);
    }

    public double getSensorUnitsAbsoluteHeight(){
        if (homed){
            return getSensorUnitsRelativeHeight();
        }
        //Not calibrated
        else return 0.0;
    }

    public double getAbsoluteHeight(){
        if (homed){
            return getRelativeHeight();
        }
        //Not calibrated
        else return 0.0;
    }

    private int inchesToSensorUnits(double inches){
        return (int)(inches/(0.902*Math.PI)*4096*GEAR_RATIO);
    }

    private double sensorUnitsToInches(double sensorUnits){
        return sensorUnits/4096.0*0.902*Math.PI/GEAR_RATIO;
    }

    public double getClosedLoopError(){
        return sensorUnitsToInches(master.getClosedLoopError(0));
    }

    public double getMasterVoltage(){
        return master.getMotorOutputVoltage();
    }

    public double getSlaveVoltage(){
        return slave.getMotorOutputVoltage();
    }
}
