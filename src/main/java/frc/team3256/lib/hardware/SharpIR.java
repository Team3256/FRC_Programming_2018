package frc.team3256.lib.hardware;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import frc.team3256.robot.Constants;

public class SharpIR {

    private AnalogInput analogInput;
    double minVoltage = Constants.kIntakeSharpIRMinVoltage;
    double maxVoltage = Constants.kIntakeSharpIRMaxVoltage;

    public SharpIR(int port){
        analogInput = new AnalogInput(port);
    }

    public double getAvgVoltage(){
        return analogInput.getAverageVoltage();
    }

    public boolean isTriggered(){
        return getAvgVoltage() > minVoltage && getAvgVoltage() < maxVoltage;
    }

    public double getDistance(){
        double dist_mm = 10.0 * Math.pow((147737.0/(getAvgVoltage()*1000.0 * 10.0)), 1.2134);
        double dist_in = dist_mm/25.4;
        return dist_in;
    }
}

