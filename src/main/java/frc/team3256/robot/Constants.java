package frc.team3256.robot;

public class Constants {

    //Ports: There can be no duplicates in each set of ports
    //PWM
    public static final int kLeftIntakePort = 0;
    public static final int kRightIntakePort = 1;
    public static final int kCarriageRollerLeft = 7;
    public static final int kCarriageRollerRight = 8;




    //Solenoids
    public static final int kIntakePivotForward = 3;
    public static final int kIntakePivotReverse = 4;
    public static final int kIntakeFlopForward = 5;
    public static final int kIntakeFlopReverse = 6;
    public static final int kCarriageSqueezeForward = 7;
    public static final int kCarriageSqueezeReverse = 8;

    //CAN
    public static final int kLeftDriveMaster = 1;
    public static final int kLeftDriveSlave = 2;
    public static final int kRightDriveMaster = 3;
    public static final int kRightDriveSlave = 4;
    public static final int kElevatorMaster = 5;
    public static final int kElevatorSlave = 6;

    //DIO
    public static final int kHallEffectPort = 0;
    public static final int kTopBumperPort = 1;
    public static final int kBottomBumperPort = 2;

    //Analog Inputs
    public static final int kIntakeSharpIR = 0;

    //Robot constants:
    //everything is in inches, seconds, or degrees
    public static final double kRobotTrack = 25; //TODO: TBD
    public static final double kWheelDiameter = 6.0;
    //12 fps
    public static final double kMaxVelocityHighGearInPerSec = 12.0*12.0;

    //Control Loop Parameters:
    //Looping period
    public static final double kControlLoopPeriod = 1.0/200.0;
    public static final double kSlowLoopPeriod = 1.0/50.0;

    //Drive Motion Magic
    public static final double kDriveMotionMagicP = 0;
    public static final double kDriveMotionMagicI = 0;
    public static final double kDriveMotionMagicD = 0;
    public static final double kDriveMotionMagicF = 0;
    public static final double kDriveMotionMagicCloseLoopRampRate = 0;
    public static final double kDriveMotionMagicAcceleration = 0;
    public static final double kDriveMotionMagicCruiseVelocity = 0;
    public static final int kDriveMotionMagicIZone = 0;
    public static final int kDriveMotionMagicProfile = 0;

    //Drive Velocity
    public static final double kLeftDriveVelocityP = 0.3;
    public static final double kLeftDriveVelocityI = 0.0003;
    public static final double kLeftDriveVelocityD = 6.5;
    public static final double kLeftDriveVelocityF = 0.214;
    public static final double kLeftDriveVelocityCloseLoopRampRate = 240.0; //240
    public static final int kLeftDriveVelocityIZone = 0;//72
    public static final double kRightDriveVelocityP = 0.3;
    public static final double kRightDriveVelocityI = 0.0005;
    public static final double kRightDriveVelocityD = 6;
    public static final double kRightDriveVelocityF = 0.214;
    public static final double kRightDriveVelocityCloseLoopRampRate = 120.0; //240
    public static final int kRightDriveVelocityIZone = 0;//72
    public static final int kDriveVelocityProfile = 1;

    //Presets:
    public static final double kIntakeSharpIRMaxVoltage = 12.0;
    public static final double kIntakeSharpIRMinVoltage = 2.0;
    public static final double kLeftIntakePower = 0;
    public static final double kRightIntakePower = 0;
    public static final double kIntakeExhaustPower = 0;
    public static final double kUnjamMaxDuration = 100.0/1000.0;
    public static final int kCarriageReceivePower = 0;
    public static final int kCarriageScoreForwardPower = 0;
    public static final int kCarriageScoreBackwardPower = -0;

    public static final double rollForwardMotorPower = 0;
    public static final double rollBackwardMotorPower = -0;

    //Game Data:
    public static final int kSwitchIndex = 0;
    public static final int kScaleIndex = 1;
    public static final int kOpponentSwitchIndex = 2;
}