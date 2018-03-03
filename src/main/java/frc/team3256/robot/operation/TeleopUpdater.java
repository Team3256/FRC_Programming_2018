package frc.team3256.robot.operation;

import frc.team3256.lib.DrivePower;
import frc.team3256.lib.control.TeleopDriveController;
import frc.team3256.robot.Constants;
import frc.team3256.robot.subsystems.Carriage;
import frc.team3256.robot.subsystems.DriveTrain;
import frc.team3256.robot.subsystems.Elevator;
import frc.team3256.robot.subsystems.Intake;

public class TeleopUpdater {

    private ControlsInterface controls = new LogitechButtonBoardConfig();

    private DriveTrain m_drive = DriveTrain.getInstance();
    private Intake m_intake = Intake.getInstance();
    private Elevator m_elevator = Elevator.getInstance();
    private Carriage m_carriage = Carriage.getInstance();

    private boolean prevPivotToggle = false;
    private boolean prevFlopToggle = false;
    private boolean isManualControl = false;

    public void update(){

        double throttle = controls.getThrottle();
        double turn = controls.getTurn();
        boolean quickTurn = controls.getQuickTurn();
        boolean shiftDown = controls.getLowGear();

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

        //Drivetrain subsystem
        DrivePower power = TeleopDriveController.curvatureDrive(throttle, turn, quickTurn, !shiftDown);
        m_drive.setOpenLoop(power);
        m_drive.setHighGear(power.getHighGear());

        //Intake subsystem
        //Unjamming is highest priority because it is both intake and exhaust buttons
        if (unjam){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_UNJAM);
        }
        else if (intake){
            if (m_elevator.getHeight() > Constants.kIntakePreset + 2.0) {
                m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_INTAKE_POS);
                m_intake.setWantedState(Intake.WantedState.IDLE);
            }
            else{
                m_intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
            }
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
        else {
            m_intake.setWantedState(Intake.WantedState.IDLE);
        }

        prevFlopToggle = flopToggle;
        prevPivotToggle = pivotToggle;

        //Carriage subsystem
        if (scoreFront){
            m_carriage.setWantedState(Carriage.WantedState.WANTS_TO_SCORE_FORWARD);
        }
        else if (scoreRear){
            m_carriage.setWantedState(Carriage.WantedState.WANTS_TO_SCORE_BACKWARD);
        }
        else{
            m_carriage.setWantedState(Carriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
        }

        //Elevator subsystem
        //If the elevator is not homed yet, home the elevator
        if (!m_elevator.isHomed()){
            System.out.println("TeleopUpdater: NOT HOMED!...AUTO HOMING");
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_HOME);
        }

        //Manual raising and lowering is highest priority for the elevator
        if (manualRaise){
            isManualControl = true;
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_MANUAL_UP);
        }
        else if (manualLower){
            isManualControl = true;
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_MANUAL_DOWN);
        }
        //For all the presets, make sure we are homed before running them
        else if (switchPos && m_elevator.isHomed()){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            isManualControl = false;
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_SWITCH_POS);
        }
        else if (lowScalePos && m_elevator.isHomed()){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            isManualControl = false;
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_LOW_SCALE_POS);
        }
        else if (midScalePos && m_elevator.isHomed()){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            isManualControl = false;
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_MID_SCALE_POS);
        }
        else if (highScalePos && m_elevator.isHomed()){
            m_intake.setWantedState(Intake.WantedState.WANTS_TO_DEPLOY);
            isManualControl = false;
            m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_HIGH_SCALE_POS);
        }
        else{
            //Only hold if we are in manual control
            //If we are not in manual control, don't hold, the elevator internally will stop at the correct preset height
            if (isManualControl){
                m_elevator.setWantedState(Elevator.WantedState.WANTS_TO_HOLD);
            }
        }
    }
}
