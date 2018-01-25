package frc.team3256.robot.subsystems;

import java.util.ArrayList;
import java.util.List;

public class SubsystemManager {

    private List<SubsystemBase> subsystems;

    public SubsystemManager(){
        subsystems = new ArrayList<>();
    }

    public void addSubsystems(SubsystemBase ... subsystems){
        for(SubsystemBase s : subsystems){
            this.subsystems.add(s);
        }
    }

    public void zeroAllSensors(){
        for(SubsystemBase s : subsystems){
            s.zeroSensors();
        }
    }

    public void outputToDashboard(){
        for(SubsystemBase s : subsystems){
            s.outputToDashboard();
        }
    }

}
