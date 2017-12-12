package frc.team3256.lib.control;

import frc.team3256.robot.subsystems.DriveTrain;

public class TeleopDriveController {

    static DriveTrain driveTrain = DriveTrain.getInstance();

    private static boolean isCubed = false;

    //Tank Drive

    public static void tankDrive(double leftPower, double rightPower) {
        if(isCubed) {
            leftPower = Math.pow(leftPower, 3);
            rightPower = Math.pow(rightPower, 3);
        }
        driveTrain.setOpenLoop(leftPower,rightPower);
    }

    //Arcade Drive

    public static void arcadeDrive(double throttle, double turn) {
        if(isCubed) {
            throttle = Math.pow(throttle, 3);
            turn = Math.pow(turn, 3);
        }
        driveTrain.setOpenLoop(throttle+turn,throttle-turn);
    }

}
