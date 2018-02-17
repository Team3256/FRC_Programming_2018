package frc.team3256.robot;
import java.util.Scanner;

public class LoopTest {

    public static class Print {

        int currState;
        int prevState;
        boolean stateChanged;
        boolean firstRun = true;

        public void init() {
            prevState = 0;
            stateChanged = true;
        }

        public void update() {
            if (firstRun) {
                stateChanged = false;
                firstRun = false;
            }
            else if (prevState != currState){
                System.out.println("Wants to change state");
                stateChanged = true;
                prevState = currState;
            }
            else stateChanged = false;

            if (stateChanged) return;

            switch (currState) {
                case 0:
                    System.out.println("0");
                    break;
                case 1:
                    System.out.println("1");
                    break;
                default:
                    System.out.println("other");
                    break;
            }

        }

        public void setState(int state) {
            this.currState = state;
        }
    }

    public static class Looper {

        Print printer = new Print();

        public void run() {
            printer.init();
            while(true) {
                Scanner scanner = new Scanner(System.in);
                set(scanner.nextInt());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                printer.update();
            }
        }

        public void set(int state) {
            printer.setState(state);
        }
    }

    public static void main(String [] args){
        Looper looper = new Looper();
        looper.run();
    }
}
