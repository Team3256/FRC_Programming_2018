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
    public static final int kCarriageSqueezeForward = 2;
    public static final int kCarriageSqueezeReverse = 8;
    public static final int kShifterForward = 7;
    public static final int kShifterReverse = 0;

    //CAN
    public static final int kLeftDriveMaster = 4;
    public static final int kLeftDriveSlave = 5;
    public static final int kRightDriveMaster = 3;
    public static final int kRightDriveSlave = 2;
    public static final int kElevatorMaster = 0;
    public static final int kElevatorSlaveOne = 1;
    public static final int kElevatorSlaveTwo = 6;
    public static final int kElevatorSlaveThree = 7;

    //DIO
    public static final int kHallEffectPort = 0;
    public static final int kTopBumperPort = 1;
    public static final int kBottomBumperPort = 2;

    //Analog Inputs
    public static final int kIntakeSharpIR = 0;

    //Robot constants:
    //everything is in inches, seconds, or degrees
    public static final double kRobotTrack = 25.375;
    public static final double kScrubFactor = 0.3;
    public static final double kWheelDiameter = 6.045;
    public static final double kEncoderScalingFactor = 3.0*60.0/24.0;

    //Control Loop Parameters:
    //Looping period
    public static final double kControlLoopPeriod = 1.0/200.0;
    public static final double kSlowLoopPeriod = 1.0/50.0;

    //Turn Motion Magic: Low Gear
    public static final double kTurnLowGearMotionMagicP = 3.5;
    public static final double kTurnLowGearMotionMagicI = 0.001;
    public static final int kTurnLowGearIZone = 1000;
    public static final double kTurnLowGearMotionMagicD = 100.0;
    public static final double kTurnLowGearMotionMagicF = 0.45;
    public static final double kTurnLowGearMotionMagicAcceleration = 15.0*12.0;
    public static final double kTurnLowGearMotionMagicCruiseVelocity = 5.0*12.0;
    public static final int kTurnMotionMagicProfile = 0;

    //DriveStraightController Gains
    public static final double kDistanceTrajectoryP = 0.04;
    public static final double kDistanceTrajectoryI = 0;
    public static final double kDistanceTrajectoryD = 0;
    public static final double kDistanceTrajectoryV = 1.0/140.0;
    public static final double kDistanceTrajectoryA = 0;
    public static final double kDistanceTrajectoryAccel = 9.0*10.0;
    public static final double kDistanceTrajectoryCruiseVelocity = 10.0*10.0;
    public static final double kStraightP = 0.0;
    public static final double kStraightI = 0.0;
    public static final double kStraightD = 0.0;

    //Trajectory Curve Gains
    public static final double kCurveTrajectoryP = 0.17;
    public static final double kCurveTrajectoryI = 0;
    public static final double kCurveTrajectoryD = 0;
    public static final double kCurveTrajectoryV = 1.0/140.0;
    public static final double kCurveTrajectoryA = 0;
    public static final double kCurveTrajectoryMaxAccel = 9.0*10.0;
    public static final double kCurveTrajectoryCruiseVelocity = 10.0*10.0;

    //Drive Velocity
    public static final double kLeftDriveVelocityP = 0.3;
    public static final double kLeftDriveVelocityI = 0.0003;
    public static final double kLeftDriveVelocityD = 6.5;
    public static final double kLeftDriveVelocityF = 0.214;
    public static final int kLeftDriveVelocityIZone = 0;//72
    public static final double kRightDriveVelocityP = 0.3;
    public static final double kRightDriveVelocityI = 0.0005;
    public static final double kRightDriveVelocityD = 6;
    public static final double kRightDriveVelocityF = 0.214;
    public static final int kRightDriveVelocityIZone = 0;//72
    public static final int kDriveVelocityProfile = 1;

    //Elevator Gains
    public static final int kElevatorHoldSlot = 0;
    public static final int kElevatorHoldP = 0;
    public static final int kElevatorHoldI = 0;
    public static final int kElevatorHoldD = 0;
    public static final int kElevatorFastUpSlot = 1;
    public static final int kElevatorFastUpP = 0;
    public static final int kElevatorFastUpI = 0;
    public static final int kElevatorFastUpD = 0;
    public static final int kElevatorFastDownSlot = 2;
    public static final int kElevatorFastDownP = 0;
    public static final int kElevatorFastDownI = 0;
    public static final int kElevatorFastDownD = 0;

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

    public static final double kScoreForwardMotorPower = 0;
    public static final double kScoreBackwardMotorPower = -0;

    public static final double kElevatorUpManualPower = 0.0;
    public static final double kElevatorDownManualPower = 0.0;
    public static final double kHighScalePreset = 0.0;
    public static final double kMidScalePreset = 0.0;
    public static final double kLowScalePreset = 0.0;
    public static final double kSwitchPreset = 0.0;
    public static final double kIntakePreset = 0.0;
    public static final double kStowPreset = 0.0;
    public static final double kHomeHeight = 0.0;

    //Game Data:
    public static final int kSwitchIndex = 0;
    public static final int kScaleIndex = 1;
    public static final int kOpponentSwitchIndex = 2;
}