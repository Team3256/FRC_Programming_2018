package frc.team3256.robot.subsystems;

import java.util.EventListener;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ElevatorTest{
    Scanner s = new Scanner(System.in);
    String input;
    Timer timer;
    Elevator elevator = Elevator.getInstance();

    public ElevatorTest() {
        timer = new Timer(true);
    }

    //-----------------Read comments in Elevator class in under simulation-----------------
    TimerTask loop = new TimerTask() {
        @Override
        public void run() {
            System.out.println("ONE INPUT ALLOWED");
            input = s.nextLine();
            switch(input) {
                case "hs":
                    elevator.setWantedState(Elevator.WantedState.HIGH_SCALE);
                    break;
                case "ls":
                    elevator.setWantedState(Elevator.WantedState.LOW_SCALE);
                    break;
                case "ho":
                    elevator.setWantedState(Elevator.WantedState.HOLD);
                    break;
                case "sw":
                    elevator.setWantedState(Elevator.WantedState.SWITCH);
                    break;
                case "mu":
                    elevator.setWantedState(Elevator.WantedState.MANUAL_UP);
                    break;
                case "md":
                    elevator.setWantedState(Elevator.WantedState.MANUAL_DOWN);
                    break;
                case "ip":
                    elevator.setWantedState(Elevator.WantedState.INTAKE_POS);
                    break;
                default:
                    elevator.setWantedState(Elevator.WantedState.HOLD);
                    break;
            }
            elevator.update(System.currentTimeMillis());
            System.out.println(elevator.getCurrentState());
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
        ElevatorTest main = new ElevatorTest();
        main.elevator.init(0);
        main.timer.scheduleAtFixedRate(main.loop, 0, 500);
        Thread.sleep(1000*1000);
        main.timer.cancel();
        System.out.println("timer ended");
    }
}