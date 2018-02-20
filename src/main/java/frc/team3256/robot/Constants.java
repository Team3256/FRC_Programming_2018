package frc.team3256.robot;

public class Constants {

    //Ports: There can be no duplicates in each set of ports
    //PWM
    public static final int kLeftIntakePort = 0;
    public static final int kRightIntakePort = 1;
    public static final int kCarriageRollerLeft = 7;
    public static final int kCarriageRollerRight = 8;


    //Solenoids
    public static final int kIntakePivotForward = 5;
    public static final int kIntakePivotReverse = 2;
    public static final int kIntakeFlopForward = 1;
    public static final int kIntakeFlopReverse = 6;
    public static final int kCarriageSqueezeForward = 4;
    public static final int kCarriageSqueezeReverse = 3;
    public static final int kShifterForward = 7;
    public static final int kShifterReverse = 0;

    //CAN
    public static final int kLeftDriveMaster = 4;
    public static final int kLeftDriveSlave = 5;
    public static final int kRightDriveMaster = 3;
    public static final int kRightDriveSlave = 2;
    public static final int kElevatorMaster = 6;
    public static final int kElevatorSlaveOne = 1;
    public static final int kElevatorSlaveTwo = 0;
    public static final int kElevatorSlaveThree = 7;

    //DIO
    public static final int kHallEffectPort = 0;
    public static final int kTopBumperPort = 1;
    public static final int kBottomBumperPort = 2;

    //Analog Inputs
    public static final int kIntakeSharpIR = 0;

    //Robot constants:
    //everything is in inches, seconds, or degrees
    public static final double kRobotTrack = 25.146; //25.146
    public static final double kScrubFactor = 1.0;
    public static final double kWheelDiameter = 6.045;
    public static final double kDriveEncoderScalingFactor = 3.0*60.0/24.0;
    public static final double kElevatorEncoderScalingFactor = 1.0;

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
    public static final double kDistanceTrajectoryV = 1.0/170.0;
    public static final double kDistanceTrajectoryA = 0;
    public static final double kDistanceTrajectoryAccel = 12.0*12.0;
    public static final double kDistanceTrajectoryCruiseVelocity = 12.0*12.0;
    public static final double kStraightP = 0.02;//0.05
    public static final double kStraightI = 0.0;
    public static final double kStraightD = 0.0;

    //Trajectory Curve Gains
    public static final double kCurveTrajectoryP = 0.0; //0.006
    public static final double kCurveTrajectoryI = 0.0;
    public static final double kCurveTrajectoryD = 0.0;
    public static final double kCurveTrajectoryV = 1.0/325.0;//1.0/300.0
    public static final double kCurveP = 0.07;//0.04
    public static final double kCurveI = 0.0; //0.001
    public static final double kCurveTrajectoryA = 0;
    public static final double kCurveD = 0.0;
    public static final double kCurveTrajectoryMaxAccel = 12.0*12.0;
    public static final double kCurveTrajectoryCruiseVelocity = 10.0*12.0;

    //Drive Velocity
    public static final double kDriveVelocityP = 0.0;//0.3
    public static final double kDriveVelocityI = 0.0;//0.0003
    public static final double kDriveVelocityD = 0.0;//6.5
    public static final double kDriveVelocityF = 0.0355;//0.214
    public static final int kDriveVelocityIZone = 0;//72 //0
    public static final int kDriveVelocityProfile = 1; //1

    //Pure Pursuit
    public static final double pathCompletionTolerance = 0.1;

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
    public static final double kIntakeSharpIRMaxVoltage = 5.0;
    public static final double kIntakeSharpIRMinVoltage = 0.0;
    public static final double kLeftIntakePower = 0.75;
    public static final double kRightIntakePower = 0.6;
    public static final double kIntakeExhaustPower = 0.6;
    public static final double kUnjamMaxDuration = 1000.0/1000.0;
    public static final double kCarriageReceivePower = 0;
    public static final double kCarriageSecurePower = 0;
    public static final double kCarriageScoreForwardPower = 0;
    public static final double kCarriageScoreBackwardPower = -0;

    public static final double kScoreForwardMotorPower = 0;
    public static final double kScoreBackwardMotorPower = -0;
    public static final double kScoreForwardHeight = 0;
    public static final double kScoreBackwardHeight = 0;

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