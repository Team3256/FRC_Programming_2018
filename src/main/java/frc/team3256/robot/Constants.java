package frc.team3256.robot;

public class Constants {

    public static final int kLeftDriveMaster = 1;
    public static final int kLeftDriveSlave = 2;
    public static final int kRightDriveMaster = 3;
    public static final int kRightDriveSlave = 4;

    public static final double ROBOT_TRACK = 25; //TBD

    //Maximum free speed is 18 ft/sec, so we will go with 75 percent of that as our actual top speed
    public static final double kMaxVelocityHighGearInPerSec = 18.0*12.0*0.75;

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
    public static final double kDriveVelocityP = 0.2;
    public static final double kDriveVelocityI = 0;
    public static final double kDriveVelocityD = 0;
    public static final double kDriveVelocityF = 0.214;
    public static final double kDriveVelocityCloseLoopRampRate = 0;
    public static final int kDriveVelocityIZone = 0;
    public static final int kDriveVelocityProfile = 1;
}
