package frc.team3256.robot.auto.actions;
import frc.team3256.robot.subsystems.DriveTrain;

public class FollowArcTrajectoryAction implements Action {

    private DriveTrain drive = DriveTrain.getInstance();
    private double startVel, endVel, radius, angle;
    private boolean backTurn;

    public FollowArcTrajectoryAction(double startVel, double endVel, double radius, double angle, boolean backwardsTurn) {
        this.startVel = startVel;
        this.endVel = endVel;
        this.radius = radius;
        this.angle = angle;
        this.backTurn = backwardsTurn;
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
        drive.setOpenLoop(0,0);
        System.out.println("Finished....");
    }

    @Override
    public void start() {
        drive.resetDriveArcController();
        drive.configureDriveArc(startVel, endVel, angle, radius, backTurn);
        System.out.println("Started...");
        drive.resetDriveArcController();
        drive.getGyro().reset();
        drive.resetEncoders();
    }
}
