package frc.team3256.robot.operation;

import frc.team3256.robot.subsystems.Intake;
import frc.team3256.robot.subsystems.Superstructure;

public class TeleopUpdater {

    ControlsInterface controls = new LogitechButtonBoardConfig();
    Superstructure superstructure = Superstructure.getInstance();;
    Intake intake = Intake.getInstance();;
    boolean prevPivotToggle, prevFlopToggle;
    boolean prevIntake, prevExhaust, prevUnjam;
    boolean prevScoreFront, prevScoreRear;
    boolean prevSwitchPos, prevLowScalePos, prevHighScalePos, prevMidScalePos;
    boolean prevManualRaise, prevManualLower;
    boolean prevManualSqueeze;

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

        boolean manualSqueeze = controls.manualSqueezeCarriage();

        if (pivotToggle && !prevPivotToggle){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }

        if (flopToggle && !prevFlopToggle){
            intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }

        if (intakeCom && !prevIntake){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_INTAKE);
        }

        if (exhaust && !prevExhaust){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_EXHAUST);
        }

        if (unjam && !prevUnjam){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_UNJAM);
        }

        if (scoreFront && !prevScoreFront){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_FORWARD);
        }

        if (scoreRear && !prevScoreRear){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_BACKWARD);
        }

        if (switchPos && !prevSwitchPos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_SWITCH);
        }

        if (lowScalePos && !prevLowScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_LOW_SCALE);
        }

        if (midScalePos && !prevMidScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_MID_SCALE);
        }

        if (highScalePos && !prevHighScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_HIGH_SCALE);
        }

        if (manualRaise && !prevManualRaise){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_RAISE_MANUAL);
        }

        if (manualLower && !prevLowScalePos){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_LOWER_MANUAL);
        }

        if (manualSqueeze && !prevManualSqueeze){
            superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SQUEEZE);
        }

         prevFlopToggle = flopToggle;
         prevPivotToggle = pivotToggle;

         prevIntake = intakeCom;
         prevExhaust = exhaust;
         prevUnjam = unjam;

         prevScoreFront = scoreFront;
         prevScoreRear = scoreRear;

         prevSwitchPos = switchPos;
         prevLowScalePos = lowScalePos;
         prevHighScalePos = highScalePos;
         prevMidScalePos = midScalePos;

         prevManualRaise = manualRaise;
         prevManualLower = manualLower;

         prevManualSqueeze = manualSqueeze;
    }
}
