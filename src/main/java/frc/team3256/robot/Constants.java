package frc.team3256.robot;

public class Constants {

    public static final int kLeftDriveMaster = 1;
    public static final int kLeftDriveSlave = 2;
    public static final int kRightDriveMaster = 3;
    public static final int kRightDriveSlave = 4;

    public static final double ROBOT_TRACK = 25; //TBD
    public static final double WHEEL_DIAMETER = 4.0;

    //Maximum free speed is 18 ft/sec, so we will go with 75 percent of that as our actual top speed
    public static final double kMaxVelocityHighGearInPerSec = 18.0*12.0*0.75;

    public static final double kControlLoopPeriod = 1.0/200.0;
    public static final double kSlowLoopPeriod = 1.0/50.0;

    // These are all for Motion Magic
    public static final double kDriveMotionMagicP = 0;
    public static final double kDriveMotionMagicI = 0;
    public static final double kDriveMotionMagicD = 0;
    public static final double kDriveMotionMagicF = 0;
    public static final double kDriveMotionMagicCloseLoopRampRate = 0;
    public static final double kDriveMotionMagicAcceleration = 0;
    public static final double kDriveMotionMagicCruiseVelocity = 0;
    public static final int kDriveMotionMagicIZone = 0;
    public static final int kDriveMotionMagicProfile = 0;


    // These are all for Velocity
    public static final double kLeftDriveVelocityP = 0.3;
    public static final double kLeftDriveVelocityI = 0.0003;
    public static final double kLeftDriveVelocityD = 6.5;
    public static final double kLeftDriveVelocityF = 0.214;

    public static final double kLeftDriveVelocityCloseLoopRampRate = 240.0; //240
    public static final int kLeftDriveVelocityIZone = 0;//72
    public static final int kDriveVelocityProfile = 1;

    public static final double kRightDriveVelocityP = 0.3;
    public static final double kRightDriveVelocityI = 0.0005;
    public static final double kRightDriveVelocityD = 6;
    public static final double kRightDriveVelocityF = 0.214;

    public static final double kRightDriveVelocityCloseLoopRampRate = 120.0; //240
    public static final int kRightDriveVelocityIZone = 0;//72

    //Game Data
    public static final int SWITCH_INDEX = 0;
    public static final int SCALE_INDEX = 1;
    public static final int OPPONENT_SWITCH_INDEX = 2;
}
