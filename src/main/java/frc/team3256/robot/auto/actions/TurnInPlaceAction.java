package frc.team3256.robot.auto.actions;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.subsystems.DriveTrain;

public class TurnInPlaceAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double degrees;

    public TurnInPlaceAction(double degrees) {
        this.degrees = degrees;
    }

    @Override
    public boolean isFinished() {
        return drive.isTurnFinished();
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
        drive.setOpenLoop(0,0);
    }

    @Override
    public void start() {
        drive.setTurnSetpoint(degrees);
    }
}
