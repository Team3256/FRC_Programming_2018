package frc.team3256.robot.subsystems;

import java.util.EventListener;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class IntakeTest{
    Scanner s = new Scanner(System.in);
    String input;
    Timer timer;
    Intake intake = Intake.getInstance();

    public IntakeTest() {
        timer = new Timer(true);
    }

    TimerTask loop = new TimerTask() {
        @Override
        public void run() {
            System.out.println("ONE INPUT ALLOWED");
            input = s.nextLine();
            switch(input) { /*TODO: Fix the toggle pivot and flop. Does not switch even after changing
                              TODO: to different button, only after switch a pivot/flop*/
                case "in":
                    intake.setWantedState(Intake.WantedState.WANTS_TO_INTAKE);
                    break;
                case "ex":
                    intake.setWantedState(Intake.WantedState.WANTS_TO_EXHAUST);
                    break;
                case "tf":
                    intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
                    break;
                case "tp":
                    intake.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_PIVOT);
                    break;
                default:
                    break;
            }
            intake.update(System.currentTimeMillis());
            System.out.println(intake.getCurrentState());
            completeTask();
        }
        public void completeTask(){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    };

    public static void main(String[] args) throws InterruptedException {
        IntakeTest main = new IntakeTest();
        main.intake.init(0);
        main.timer.scheduleAtFixedRate(main.loop, 0, 500);
        Thread.sleep(1000*1000);
        main.timer.cancel();
        System.out.println("timer ended");
    }
}