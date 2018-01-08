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

        if (Math.abs(leftPower) <= 0.15){
            leftPower = 0;
        }
        if (Math.abs(rightPower) <= 0.15){
                rightPower = 0;
        }

        return new DrivePower(leftPower, rightPower);
    }

    //Arcade Drive
    public static DrivePower arcadeDrive(double throttle, double turn) {
        if (Math.abs(throttle) <= 0.3){
            throttle = 0;
        }
        if (Math.abs(turn) <= 0.3){
            turn = 0;
        }
        if(isCubed) {
            throttle = Math.pow(throttle, 3);
            turn = Math.pow(turn, 3);
        }

        double left = throttle + turn;
        double right = throttle - turn;

        /*if (left > right && left > 1){
            skim = left - 1;
            left = 1;
            right += skim;
        }

        else if (right > left && right > 1){
            skim = right - 1;
            right = 1;
            left += skim;
        } */

        return new DrivePower(left, right);
    }

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
