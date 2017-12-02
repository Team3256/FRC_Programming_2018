package frc.team3256.robot.subsystems;

import com.ctre.CANTalon;
import frc.team3256.robot.Constants;

public class DriveTrain {

    private static DriveTrain instance;
    private CANTalon leftMaster, rightMaster;

    public static DriveTrain getInstance(){
        return instance == null ? instance = new DriveTrain() : instance;
    }

    private DriveTrain(){
        leftMaster = new CANTalon(Constants.kLeftDriveMaster);
        rightMaster = new CANTalon(Constants.kRightDriveMaster);

        leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        leftMaster.set(0);
        rightMaster.set(0);

        leftMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        if (leftMaster.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative) == CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent){
        }
        //rightMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
    }

    public double getLeftDistance(){
        return leftMaster.getPosition();
    }

    public double getRightDistance(){
        return rightMaster.getPosition();
    }

}
