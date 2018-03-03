package frc.team3256.robot.operation;

import frc.team3256.robot.Constants;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;
import frc.team3256.robot.subsystems.Superstructure;
import org.omg.CORBA.INTERNAL;

public class TeleopUpdater {

    ControlsInterface controls = new LogitechButtonBoardConfig();
    
    Intake m_intake = Intake.getInstance();
    Elevator m_elevator = Elevator.getInstance();
    Carriage m_carriage = Carriage.getInstance();

    private boolean prevPivotToggle = false, prevFlopToggle = false;

    public void update(){
        boolean pivotToggle = controls.togglePivot();
        boolean flopToggle = controls.toggleFlop();

        boolean intake = controls.getIntake();
        boolean exhaust = controls.getExhaust();
        boolean unjam = controls.getUnjam();

        boolean scoreFront = controls.scoreFront();
        boolean scoreRear = controls.scoreRear();

        boolean switchPos = controls.switchPreset();
        boolean lowScalePos = controls.scalePresetLow();
        boolean midScalePos = controls.scalePresetMid();
        boolean highScalePos = controls.scalePresetHigh();

        boolean manualRaise = controls.manualElevatorUp();
        boolean manualLower = controls.manualElevatorDown();

        //Intake based systems
        if (unjam){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        }
        else if (intake){
            if (m_elevator.getHeight() < Constants.kIntakePreset + 2.0){
                m_intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            }
            else m_intake.setWantedState(Intake.WantedState.IDLE);
        }
        else if (exhaust){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
        }
        else if (pivotToggle && !prevPivotToggle){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }
        else if (flopToggle && !prevFlopToggle){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }


        //Carriage based systems
        if (scoreFront){
            m_carriage.setWantedState(Carriage.WantedState.WANTS_TO_SCORE_FORWARD);
        }

        else if (scoreRear){
            m_carriage.setWantedState(Carriage.WantedState.WANTS_TO_SCORE_BACKWARD);
        }
        else{
            m_carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }


        //Elevator based systems
        if (manualRaise){
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_MANUAL_UP);
        }
        else if (manualLower){
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_MANUAL_DOWN);
        }
        else if (switchPos){
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_SWITCH_POS);
        }

        else if (lowScalePos){
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_LOW_SCALE_POS);
        }

        else if (midScalePos){
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_MID_SCALE_POS);
        }

        else if (highScalePos){
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_HIGH_SCALE_POS);
        }
        else{
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_HOLD);
        }

         prevFlopToggle = flopToggle;
         prevPivotToggle = pivotToggle;
    }
}
