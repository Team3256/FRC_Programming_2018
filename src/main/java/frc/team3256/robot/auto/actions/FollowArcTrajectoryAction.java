package frc.team3256.robot.auto.actions;
import frc.team3256.robot.subsystems.DriveTrain;

public class FollowArcTrajectoryAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double startVel, endVel, radius, angle;

    public FollowArcTrajectoryAction(double startVel, double endVel, double radius, double angle) {
        this.startVel = startVel;
        this.endVel = endVel;
        this.radius = radius;
        this.angle = angle;
    }

    @Override
    public boolean isFinished() {
        return drive.isArcControllerFinished();
    }

    @Override
    public void update() {
    }

    @Override
    public void done() {
       // drive.setOpenLoop(0,0);
        System.out.println("Finished....");
        drive.resetDriveArcController();
    }

    @Override
    public void start() {
        drive.configureDriveArc(startVel, endVel, angle, radius);
        System.out.println("Started...");
        drive.resetDriveArcController();
        drive.getGyro().reset();
        drive.resetEncoders();
    }
}
