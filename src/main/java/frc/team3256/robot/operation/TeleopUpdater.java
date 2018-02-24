package frc.team3256.robot.operation;

import frc.team3256.robot.subsystems.Intake;
import frc.team3256.robot.subsystems.Superstructure;

public class TeleopUpdater {

    ControlsInterface controls = new LogitechButtonBoardConfig();
    boolean prevPivotToggle, prevFlopToggle;
    boolean prevIntake, prevExhaust, prevUnjam;
    boolean prevScoreFront, prevScoreRear;
    boolean prevSwitchPos, prevLowScalePos, prevHighScalePos;
    boolean prevManualRaise, prevManualLower;
    boolean prevManualSqueeze;

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
        boolean highScalePos = controls.scalePresetHigh();

        boolean manualRaise = controls.manualElevatorUp() > 0.25;
        boolean manualLower = controls.manualElevatorDown() > 0.25;

        boolean manualSqueeze = controls.manualSqueezeCarriage();

        if (pivotToggle && !prevPivotToggle){
            Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
        }

        if (flopToggle && !prevFlopToggle){
            Intake.getInstance().setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
        }

        if (intake && !prevIntake){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_INTAKE);
        }

        if (exhaust && !prevExhaust){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_EXHAUST);
        }

        if (unjam && !prevUnjam){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_UNJAM);
        }

        if (scoreFront && !prevScoreFront){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_FORWARD);
        }

        if (scoreRear && !prevScoreRear){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_BACKWARD);
        }

        if (switchPos && !prevSwitchPos){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_SWITCH);
        }

        if (lowScalePos && !prevLowScalePos){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_LOW_SCALE);
        }

        if (highScalePos && !prevHighScalePos){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_HIGH_SCALE);
        }

        if (manualRaise && !prevManualRaise){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_RAISE_MANUAL);
        }

        if (manualLower && !prevLowScalePos){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_LOWER_MANUAL);
        }

        if (manualSqueeze && !prevManualSqueeze){
            Superstructure.getInstance().setWantedState(Superstructure.WantedState.WANTS_TO_SQUEEZE);
        }

         prevFlopToggle = flopToggle;
         prevPivotToggle = pivotToggle;

         prevIntake = intake;
         prevExhaust = exhaust;
         prevUnjam = unjam;

         prevScoreFront = scoreFront;
         prevScoreRear = scoreRear;

         prevSwitchPos = switchPos;
         prevLowScalePos = lowScalePos;
         prevHighScalePos = highScalePos;

         prevManualRaise = manualRaise;
         prevManualLower = manualLower;

         prevManualSqueeze = manualSqueeze;
    }
}
