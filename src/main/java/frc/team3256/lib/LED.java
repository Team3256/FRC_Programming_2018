package frc.team3256.lib;

import edu.wpi.first.wpilibj.Solenoid;

public class LED {

    private Solenoid red;
    private Solenoid green;
    private Solenoid blue;

    private int pcmID = 1;
    private int rPort = 5;
    private int gPort = 6;
    private int bPort = 4;

    private static LED instance;

    public static LED getInstance(){
        return instance == null ? instance = new LED() : instance;
    }

    private LED(){
        red = new Solenoid(pcmID, rPort);
        green = new Solenoid(pcmID, gPort);
        blue = new Solenoid(pcmID, bPort);
    }

    public void red(){
        red.set(true);
        green.set(false);
        blue.set(false);
    }

    public void green(){
        red.set(false);
        green.set(true);
        blue.set(false);
    }

    public void blue(){
        red.set(false);
        green.set(false);
        blue.set(true);
    }

    public void turnOff(){
        red.set(false);
        green.set(false);
        blue.set(false);
    }

    public void set(boolean r, boolean g, boolean b){
        red.set(r);
        green.set(g);
        blue.set(b);
    }

}
