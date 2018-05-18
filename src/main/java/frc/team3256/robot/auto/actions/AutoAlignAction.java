package frc.team3256.robot.auto.actions;

import edu.wpi.first.wpilibj.Timer;
import frc.team3256.robot.subsystems.DriveTrain;
import sun.font.TrueTypeFont;

public class AutoAlignAction implements Action {

    private double startTime;

    @Override
    public boolean isFinished() {
        return DriveTrain.getInstance().isTurnInPlaceFinished();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        DriveTrain.getInstance().setHighGear(true);
    }

    @Override
    public void start() {
        startTime = Timer.getFPGATimestamp();
        DriveTrain.getInstance().setTurnInPlaceSetpoint(-1.0 * DriveTrain.getInstance().getCubeOffsetAngle());
    }
}
