package frc.team3256.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import frc.team3256.lib.Loop;

public class Superstructure implements Loop {

    private static Superstructure instance;

    private Elevator elevator;
    private Intake intake;
    private ElevatorCarriage carriage;
    private Compressor compressor;

    private static Superstructure getInstance(){
        return instance == null ? instance = new Superstructure() : instance;
    }

    private Superstructure(){

        elevator = Elevator.getInstance();
        intake = Intake.getInstance();
        carriage = ElevatorCarriage.getInstance();

        compressor = new Compressor();
        compressor.setClosedLoopControl(true);
    }

    public enum SystemState{
        STOWED_AND_IDLE,
        INTAKING,
        EXHAUSTING,
        UNJAMMING,
        RAISING_ELEVATOR,
        LOWERING_ELEVATOR,
        SCORING
    }

    public enum WantedState{
        WANTS_TO_STOW_AND_IDLE,
        WANTS_TO_INTAKE_CUBE,
        WANTS_TO_EXHAUST_CUBE,
        WANTS_TO_UNJAM_CUBE,
        WANTS_TO_TOGGLE_INTAKE_PIVOT,
        WANTS_TO_TOGGLE_INTAKE_FLOP,
        WANTS_TO_SCORE_SWITCH,
        WANTS_TO_SCORE_LOW_SCALE,
        WANTS_TO_SCORE_MID_SCALE,
        WANTS_TO_SCORE_HIGH_SCALE,
        WANTS_TO_SPIT_BALL,
        WANTS_TO_TOGGLE_SQUEEZE
    }

    @Override
    public void init(double timestamp) {

    }

    @Override
    public void update(double timestamp) {

    }

    @Override
    public void end(double timestamp) {

    }
}
