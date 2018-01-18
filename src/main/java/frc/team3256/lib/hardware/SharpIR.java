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
}
