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

        boolean manualRaise = controls.manualElevatorUp() > 0.25;
        boolean manualLower = controls.manualElevatorDown() > 0.25;


        if (pivotToggle && !prevPivotToggle){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }


        if (flopToggle && !prevFlopToggle){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }


        if (intakeCom){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_INTAKE);
        }
        else if (exhaust){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_EXHAUST);
        }
        else if (unjam){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_UNJAM);
        }


        if (scoreFront){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_FORWARD);
        }

        else if (scoreRear){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_BACKWARD);
        }


        if (switchPos){
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


        if (manualRaise){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_RAISE_MANUAL);
        }

        else if (manualLower){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_LOWER_MANUAL);
        }

         prevFlopToggle = flopToggle;
         prevPivotToggle = pivotToggle;
    }
}
