package frc.team3256.robot.subsystems;

import java.util.EventListener;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main{
    Scanner s = new Scanner(System.in);
    String input;
    Timer timer;
    Superstructure superstructure = Superstructure.getInstance();

    public Main() {
        timer = new Timer(true);
    }

    TimerTask loop = new TimerTask() {
        @Override
        public void run() {
            System.out.println("ONE INPUT ALLOWED");
            input = s.nextLine();
            switch(input) {
                /*case "pi":
                    superstructure.setWantedState(Superstructure.WantedState.);
                    break;
                case "fl":
                    superstructure.setWantedState(Intake.WantedState.WANTS_TO_TOGGLE_FLOP);
                    break;*/
                case "in":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_INTAKE);
                    break;
                case "ex":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_EXHAUST);
                    break;
                case "ss":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_SWITCH);
                    break;
                case "sh":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_HIGH_SCALE);
                    break;
                case "sl":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_LOW_SCALE);
                    break;
                case "sf":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_FORWARD);
                    break;
                case "sb":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SCORE_BACKWARD);
                    break;
                case "sq":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_SQUEEZE);
                    break;
                case "rm":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_RAISE_MANUAL);
                    break;
                case "lm":
                    superstructure.setWantedState(Superstructure.WantedState.WANTS_TO_LOWER_MANUAL);
                    break;
                default:
                    break;
            }
            superstructure.update(System.currentTimeMillis());
            System.out.println(superstructure.getCurrentState());
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
        Main main = new Main();
        main.superstructure.init(0);
        main.timer.scheduleAtFixedRate(main.loop, 0, 500);
        Thread.sleep(1000*1000);
        main.timer.cancel();
        System.out.println("timer ended");
    }
}