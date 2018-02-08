package frc.team3256.lib.hardware;

import edu.wpi.first.wpilibj.AnalogInput;

public class PressureSensor {

    private AnalogInput analogInput;

    public PressureSensor(int port){
        analogInput = new AnalogInput(port);
    }

    private double getAverageVoltage(){
        return analogInput.getAverageVoltage();
    }

    private double getPressure(){
        return 250.0 * (getAverageVoltage() / 5.0) - 25.0;
    }
}