package frc.team3256.lib.control;

import frc.team3256.lib.DrivePower;

public class TeleopDriveController {

    private static boolean isCubed = false;
    private static final int turnScalar = 1;
    private static double skim = 0;

    //Tank Drive
    public static DrivePower tankDrive(double leftPower, double rightPower) {
        if(isCubed) {
            leftPower = Math.pow(leftPower, 3);
            rightPower = Math.pow(rightPower, 3);
        }
        return new DrivePower(leftPower, rightPower);
    }

    //Arcade Drive
    public static DrivePower arcadeDrive(double throttle, double turn) { }

    //Curvature or Cheesy Drive
    public static DrivePower curvatureDrive(double throttle, double turn, boolean turnInPlace){
        if(isCubed) {
            throttle = Math.pow(throttle, 3);
            turn = Math.pow(turn, 3);
        }
        if (!turnInPlace){
            turn = turn * turnScalar * Math.abs(throttle);
        }
        double left = throttle + turn;
        double right = throttle - turn;
        return new DrivePower(left, right);
    }
}
