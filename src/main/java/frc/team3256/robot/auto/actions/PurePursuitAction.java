package frc.team3256.robot.auto.actions;

import frc.team3256.lib.path.Path;
import frc.team3256.lib.path.PurePursuitTracker;
import frc.team3256.robot.subsystems.DriveTrain;

public class PurePursuitAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private Path path;

    public PurePursuitAction(Path path) {
        this.path = path;
    }

    @Override
    public boolean isFinished() {
        return drive.isPurePursuitFinished();
    }

    @Override
    public void update() {

    }

    @Override
    public void done() {
        drive.setOpenLoop(0, 0);
        System.out.println("Finished");
    }

    @Override
    public void start() {
        drive.configurePurePursuit(path);
        System.out.println("Started");
        drive.resetEncoders();
    }
}
