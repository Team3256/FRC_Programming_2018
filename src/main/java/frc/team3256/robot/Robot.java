package frc.team3256.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

    Intake m_intake = Intake.getInstance();
    Elevator m_elevator = Elevator.getInstance();

    boolean prevIntakeToggle = false;
    boolean prevSqueezeToggle = false;

    @Override
    public void robotInit(){

    }

    @Override
    public void teleopInit(){

    }

    @Override
    public void teleopPeriodic(){
        boolean intake = OI.getIntake();
        boolean outtake = OI.getOuttake();
        boolean unjam = OI.getUnjam();
        if (unjam){
            m_intake.runMotors(0);
        }
        else if (intake){
            m_intake.runMotors(-0.6);
        }
        else if (outtake){
            m_intake.runMotors(0.6);
        }
        else{
           m_intake.runMotors(0);
        }

	/*
        boolean toggleIntake = OI.toggleIntake();
        if (toggleIntake && !prevIntakeToggle){
            if (m_intake.isClosed()){
                m_intake.open();
            }
            else{
                m_intake.close();
            }
        }
	*/

        boolean squeezeToggle = OI.toggleSqueeze();
        if (squeezeToggle && !prevSqueezeToggle){
            if (m_elevator.isSqueezing()){
                m_elevator.open();
            }
            else{
                m_elevator.squeeze();
            }
        }

	/*
        double val = OI.elevatorPower();
        m_elevator.runElevator(val);
	*/
	prevSqueezeToggle = squeezeToggle;
    }
}
