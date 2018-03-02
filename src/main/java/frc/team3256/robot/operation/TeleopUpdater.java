package frc.team3256.robot.operation;

import frc.team3256.robot.subsystems.Intake;
import frc.team3256.robot.subsystems.Superstructure;

public class TeleopUpdater {

    ControlsInterface controls = new LogitechButtonBoardConfig();
    Superstructure superstructure = Superstructure.getInstance();;
    Intake intake = Intake.getInstance();;
    boolean prevPivotToggle, prevFlopToggle;

    public void update(){
        boolean pivotToggle = controls.togglePivot();
        boolean flopToggle = controls.toggleFlop();

        boolean intakeCom = controls.getIntake();
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
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_UNJAM);
        }
        else if (intakeCom){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_INTAKE);
        }
        else if (exhaust){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_EXHAUST);
        }
        else if (pivotToggle && !prevPivotToggle){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }
        else if (flopToggle && !prevFlopToggle){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }


        //Carriage based systems
        if (scoreFront){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_FORWARD);
        }

        else if (scoreRear){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_BACKWARD);
        }
        else{
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SQUEEZE_HOLD);
        }


        //Elevator based systems
        if (manualRaise){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_RAISE_MANUAL);
        }
        else if (manualLower){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_LOWER_MANUAL);
        }
        else if (switchPos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_SWITCH);
        }

        else if (lowScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_LOW_SCALE);
        }

        else if (midScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_MID_SCALE);
        }

        else if (highScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_HIGH_SCALE);
        }
        else{
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SQUEEZE_HOLD);
        }

         prevFlopToggle = flopToggle;
         prevPivotToggle = pivotToggle;
    }
}
