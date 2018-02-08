package frc.team3256.lib.hardware;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;

public class SharpIR {

    private AnalogInput analogInput;
    private AnalogTrigger analogTrigger;

    public SharpIR(int port, double lowerVoltageLimit, double upperVoltageLimit){
        analogInput = new AnalogInput(port);
        analogTrigger = new AnalogTrigger(analogInput);
        analogTrigger.setAveraged(true);
        analogTrigger.setLimitsVoltage(lowerVoltageLimit, upperVoltageLimit);
    }

    public double getAvgVoltage(){
        return analogInput.getAverageVoltage();
    }

    public boolean isTriggered(){
        return analogTrigger.getTriggerState();
    }

    public double getDistance(){
        double dist_mm = 10.0 * Math.pow((147737.0/(getAvgVoltage()*1000.0 * 10.0)), 1.2134);
        double dist_in = dist_mm/25.4;
        return dist_in;
    }
}