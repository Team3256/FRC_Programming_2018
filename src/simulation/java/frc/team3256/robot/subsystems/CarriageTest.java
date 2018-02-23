package frc.team3256.robot.subsystems;

import java.util.EventListener;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class CarriageTest{
    Scanner s = new Scanner(System.in);
    String input;
    Timer timer;
    ElevatorCarriage carriage = ElevatorCarriage.getInstance();

    public CarriageTest() {
        timer = new Timer(true);
    }

    TimerTask loop = new TimerTask() {
        @Override
        public void run() {
            System.out.println("ONE INPUT ALLOWED");
            input = s.nextLine();
            switch(input) {
                case "re":
                    carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_RECEIVE);
                    break;
                case "sf":
                    carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_FORWARD);
                    break;
                case "sb":
                    carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SCORE_BACKWARD);
                    break;
                case "si":
                    carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
                    break;
                case "o":
                    carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_OPEN);
                    break;
                default:
                    carriage.setWantedState(ElevatorCarriage.WantedState.WANTS_TO_SQUEEZE_IDLE);
                    break;
            }
            carriage.update(System.currentTimeMillis());
            System.out.println(carriage.getCurrentState());
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
        CarriageTest main = new CarriageTest();
        main.carriage.init(0);
        main.timer.scheduleAtFixedRate(main.loop, 0, 500);
        Thread.sleep(1000*1000);
        main.timer.cancel();
        System.out.println("timer ended");
    }
}