package frc.team3256.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class Intake {

    private static Intake instance;
    private VictorSP rollerLeft, rollerRight;

    private final double LEFT_INTAKE = 0.5;
    private final double RIGHT_INTAKE = 0.6;

    public static Intake getInstance(){
        return instance == null ? instance = new Intake() : instance;
    }

    private Intake(){
        rollerLeft = new VictorSP(2);
        rollerRight = new VictorSP(7);
        rollerRight.setInverted(false);
        rollerLeft.setInverted(true);
    }

    public void intake(){
        rollerLeft.set(LEFT_INTAKE);
        rollerRight.set(RIGHT_INTAKE);
    }

    public void outtake(){
        rollerLeft.set(-LEFT_INTAKE);
        rollerRight.set(-RIGHT_INTAKE);
    }

    public void stop(){
        rollerLeft.set(0);
        rollerRight.set(0);
    }
}
