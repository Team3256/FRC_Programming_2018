package frc.team3256.robot.subsystems;

import com.ctre.CANTalon;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.lib.hardware.TalonGenerator;
import frc.team3256.robot.Constants;

public class DriveTrain {

    private static DriveTrain instance;
    private CANTalon leftMaster, rightMaster, leftSlave, rightSlave;

    public static DriveTrain getInstance() {
        return instance == null ? instance = new DriveTrain() : instance;
    }

    private DriveTrain() {
        // create talon objects
        // master talons are set to a 5 period control frame rate
        leftMaster = TalonGenerator.generateMasterTalon(Constants.kLeftDriveMaster);
        leftSlave = TalonGenerator.generateSlaveTalon(Constants.kLeftDriveSlave, Constants.kLeftDriveMaster);
        rightMaster = TalonGenerator.generateMasterTalon(Constants.kRightDriveMaster);
        rightSlave = TalonGenerator.generateSlaveTalon(Constants.kRightDriveSlave, Constants.kRightDriveMaster);

        // setup encoders
        leftMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        if (leftMaster.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative)
                != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
            DriverStation.reportError("Mag Encoder on Left Master not detected!!!", false);
        }
        rightMaster.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
        if (rightMaster.isSensorPresent(CANTalon.FeedbackDevice.CtreMagEncoder_Relative)
                != CANTalon.FeedbackDeviceStatus.FeedbackStatusPresent) {
            DriverStation.reportError("Mag Encoder on Right Master not detected!!!", false);
        }

        /*
        // set feedback status frame rates
        // we want master Talons to give us fast feedback (our encoders are plugged into them)
        leftMaster.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 5);
        rightMaster.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 5);
        // set feedback periods on slave talons to super slow since they are in follower mode
        // this reduces CAN bus utilization percentage
        leftSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 1000);
        leftSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 1000);
        leftSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.General, 1000);
        leftSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.AnalogTempVbat, 1000);
        leftSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.PulseWidth, 1000);
        rightSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.Feedback, 1000);
        rightSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.QuadEncoder, 1000);
        rightSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.General, 1000);
        rightSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.AnalogTempVbat, 1000);
        rightSlave.setStatusFrameRateMs(CANTalon.StatusFrameRate.PulseWidth, 1000);
        */

        //leftMaster.reverseOutput(true);
        //leftSlave.reverseOutput(true);
        //rightMaster.reverseOutput(true);
        //rightSlave.reverseOutput(true);


    }

    public double getLeftDistance() {
        return leftMaster.getPosition();
    }

    public double getRightDistance() {
        return rightMaster.getPosition();
    }

    public void setPower(double leftPower, double rightPower) {
        if(leftMaster.getControlMode() != CANTalon.TalonControlMode.PercentVbus) {
            leftMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        }
        if(rightMaster.getControlMode() != CANTalon.TalonControlMode.PercentVbus) {
            rightMaster.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        }
        leftMaster.set(leftPower);
        rightMaster.set(rightPower);
    }

}
