package frc.team3256.robot.auto.modes;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.auto.actions.Action;
import frc.team3256.robot.auto.actions.DriveToDistanceAction;
import frc.team3256.robot.auto.actions.WaitAction;
import frc.team3256.robot.subsystems.DriveTrain;

import java.io.DataInput;

public class TestDriveToDistanceAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        DriveTrain driveTrain = DriveTrain.getInstance();
        double startTime = Timer.getFPGATimestamp();
        runAction(new DriveToDistanceAction(20, 3));
        System.out.println("Left: " + driveTrain.getLeftDistance());
        System.out.println("Right: " + driveTrain.getRightDistance());
        System.out.println("Elapsed Time: " + (Timer.getFPGATimestamp() - startTime));
        //runAction(new WaitAction(0.5));
        //runAction(new DriveToDistanceAction(100));
    }
}
