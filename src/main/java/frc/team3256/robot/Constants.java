package frc.team3256.robot;

public class Constants {

    //Ports: There can be no duplicates in each set of ports
    //PWM
    public static final int kLeftIntakePort = 0;
    public static final int kRightIntakePort = 1;
    public static final int kCarriageRollerLeft = 2;
    public static final int kCarriageRollerRight = 3;

    //Solenoids
    public static final int kIntakePivotForward = 5;
    public static final int kIntakePivotReverse = 2;
    public static final int kIntakeFlopForward = 6;
    public static final int kIntakeFlopReverse = 1;
    public static final int kCarriageSqueezeForward = 3;
    public static final int kCarriageSqueezeReverse = 4;
    public static final int kShifterForward = 0;
    public static final int kShifterReverse = 7;

    public static final int kLEDPCMPort = 1;
    public static final int kLightR = 0;
    public static final int kLightG = 1;
    public static final int kLightB = 2;

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

    //Analog Inputs
    public static final int kIntakeSharpIR = 4;

    //Robot constants:
    //everything is in inches, seconds, or degrees
    public static final double kRobotTrack = 43.5139561597; //25.146
    public static final double kScrubFactor = 1.0;
    public static final double kWheelDiameter = 6.045;
    public static final double kDriveEncoderScalingFactor = 3.0*60.0/24.0; //3:1 GR on Encoder Stage, plus 60:24 GR on third stage
    public static final double kElevatorPulleyDiameter = 1.353*Math.PI;
    public static final double kElevatorGearRatio = 36.0/24.0;
    public static final double kElevatorMaxHeight = 51.0; //49.5
    public static final double kElevatorMinHeight = 6.0;
    public static final double kRampRate = 0.25;

    //Control Loop Parameters:
    //Looping period
    public static final double kControlLoopPeriod = 1.0/200.0;
    public static final double kSlowLoopPeriod = 1.0/50.0;

    //Turn Motion Magic: Low Gear
    public static final double kTurnLowGearMotionMagicP = 1.0;
    public static final double kTurnLowGearMotionMagicI = 0.0;
    public static final double kTurnLowGearMotionMagicD = 5.0;
    public static final double kTurnLowGearMotionMagicF = 0.08; //0.0815
    public static final double kTurnLowGearMotionMagicAcceleration = 160.0;
    public static final double kTurnLowGearMotionMagicCruiseVelocity = 80.0;
    public static final int kTurnMotionMagicProfile = 0;

    //DriveStraightController Gains
    public static final double kDistanceTrajectoryP = 0.1;
    public static final double kDistanceTrajectoryI = 0;
    public static final double kDistanceTrajectoryD = 0;
    public static final double kDistanceTrajectoryV = 1.0/120.0;
    public static final double kDistanceTrajectoryA = 0;
    public static final double kDistanceTrajectoryAccel = 10.0*10.0;
    public static final double kDistanceTrajectoryCruiseVelocity = 12.0*12.0;
    public static final double kStraightP = 0.08;//0.05
    public static final double kStraightI = 0.0;
    public static final double kStraightD = 0.0;

    //Trajectory Curve Gains
    public static final double kCurveTrajectoryP = 0.02; //0.006
    public static final double kCurveTrajectoryI = 0.0;
    public static final double kCurveTrajectoryD = 0.0;
    public static final double kCurveTrajectoryV = 1.0/200.0;//1.0/300.0
    public static final double kCurveTrajectoryA = 0;
    public static final double kCurveP = 0.05;//0.04
    public static final double kCurveI = 0.0; //0.001
    public static final double kCurveD = 0.0;
    public static final double kCurveTrajectoryMaxAccel = 10.0*12.0;
    public static final double kCurveTrajectoryCruiseVelocity = 8.0*12.0;

    //Drive Velocity
    public static final double kDriveVelocityP = 0.03;//0.3
    public static final double kDriveVelocityI = 0.0;//0.0003
    public static final double kDriveVelocityD = 0.0;//6.5
    public static final double kDriveVelocityF = 0.0387;//0.214
    public static final int kDriveVelocityIZone = 0;//72 //0
    public static final int kDriveVelocityProfile = 1; //1

    //Pure Pursuit
    public static final double kPathCompletionTolerance = 0.01;
    public static final double kLookaheadDistance = 5.0;
    public static final double kPurePursuitMaxVel = 10.0*12.0;
    public static final double kPurePursuitMaxAccel = 12.0*12.0;

    //Elevator Gains
    public static final int kElevatorHoldSlot = 0;
    public static final double kElevatorHoldP = 0.5;
    public static final double kElevatorHoldI = 0.002;
    public static final double kElevatorHoldD = 0;

    public static final int kElevatorMotionMagicDownSlot = 2;
    public static final double kElevatorMotionMagicDownF = 0.25;
    public static final double kElevatorMotionMagicDownP = 0.35;
    public static final double kElevatorMotionMagicDownI = 0;
    public static final double kElevatorMotionMagicDownD = 0;

    public static final double kElevatorMotionMagicUpF = 0.45;
    public static final double kElevatorMotionMagicUpP = 1.0;
    public static final double kElevatorMotionMagicUpI = 0.0;
    public static final double kElevatorMotionMagicUpD = 0.6;
    public static final int kElevatorMotionMagicUpSlot = 1;

    //Presets:
    public static final double kIntakeSharpIRMaxVoltage = 4.0;
    public static final double kIntakeSharpIRMinVoltage = 1.8;//2.5 --- 2.3
    public static final double kLeftIntakePower = 0.8;//0.75
    public static final double kRightIntakePower = 0.65;//0.6
    public static final double kIntakeExhaustPower = -0.6;
    public static final double kIntakeUnjamPower = -0.35;
    public static final double kUnjamMaxDuration = 6.0/1000.0;
    public static final double kCarriageReceivePower = 0.8;
    public static final double kCarriageScoreForwardPower = -0.7;
    public static final double kCarriageScoreForwardAutoPower = -0.5;
    public static final double kCarriageScoreBackwardPower = 0.7;
    public static final double kCarriageScoreForwardSlowPower = -0.4;
    public static final double kCarriageScoreBackwardSlowPower = 0.4;
    public static final double kCarriageScoreBackwardAutoPower = 1.0;
    public static final double kCarriageSmashBackwardPower = 0.8;
    public static final double kCarriageExhaustPower = -0.75;

    public static final double kElevatorUpManualPower = 0.4;
    public static final double kElevatorDownManualPower = -0.15;
    public static final double kElevatorUpSlowPower = 0.2;
    public static final double kHighScalePreset = 48; //in inches //48.5 measured
    public static final double kMidScalePreset = 42.0; //36.5
    public static final double kLowScalePreset = 36.0; //30
    public static final double kSwitchPreset = 22.0; //20.0
    public static final double kIntakePreset = 7.5;
    public static final double kDropPreset = 10.0;
    public static final double kBottomHomeHeight = 7.875; //measured in inches
    public static final double kCompHomeHeight = 8.25; //measured in inches //8.5
    public static final double kCompBottomHomeHeight = 7.5; //7.575
    public static final double kTopHomeHeight = 8.85;    //placeholder
    public static final double kElevatorHomingUpTime = 2.0;
    public static final double kElevatorTolerance = 1.0; //measured in inches
    public static final double kElevatorMaxUpVoltage = 12.0; //8.0
    public static final double kElevatorMaxDownVoltage = -8.0;//-5.0
    public static final double kElevatorMinHoldVoltage = 0.75;//0.5
    public static final double kElevatorHangPower = -1.0;

    public static final double kElevatorMaxVelocityUp = 50.0;
    public static final double kElevatorMaxAccelerationUp = 650.0;

    public static final double kElevatorMaxVelocityDown = 35.0;
    public static final double kElevatorMaxAccelerationDown = 300.0;

    //Game Data:
    public static final int kSwitchIndex = 0;
    public static final int kScaleIndex = 1;
    public static final int kOpponentSwitchIndex = 2;
}