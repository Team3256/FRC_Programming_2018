package frc.team3256.lib.control;

public class PIDController {
    private double kP, kI, kD;
    private double error, changeError, sumError, prevError, output;
    private double targetPos, tolerance, minPower, maxPower;


    /**
     * Sets tolerance to 0, minimum to 0, and maximum power to 1
     * @param kP - The P gain for the PIDController
     * @param kI - The I gain for the PIDController
     * @param kD - The D gain for the PIDController
     */
    public void setGains(double kP, double kI, double kD){
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        tolerance = 0;
        maxPower = 1;
        minPower = 0;
        reset();
    }

    /**
     * @param minPower - The minimum power output
     * @param maxPower - The maximum power output
     */
    public void setMinMaxOutput(double minPower, double maxPower){
        this.minPower = minPower;
        this.maxPower = maxPower;
    }

    /**
     * Resets the PIDController
     */
    public void reset(){
        error = 0;
        changeError = 0;
        sumError = 0;
        prevError = 0;
        output = 0;
        targetPos = 0;
    }

    /**
     * @param targetPosition - The target position of the robot to attempt to go to
     */
    public void setTargetPosition(double targetPosition){
        targetPos = targetPosition;
    }

    /**
     * @param currPosition - The current position
     * @return The calculated output
     */
    public double update(double currPosition){
        //Calculates the error
        error = targetPos - currPosition;

        //If the output is within the max power, accumulate the integral
        if(kP*Math.abs(error) <= maxPower){
            sumError += error;
        }
        //Calculate the derivative of the error
        changeError = error - prevError;

        //Update the previous error
        prevError = error;

        //Calculate the motor output
        output = kP*error + kI*sumError + kD*changeError;

        //Caps the motor's minimum and maximum output
        if(output >= 0){
            output = Math.min(maxPower, Math.max(output, minPower));
        }
        else if (output < 0){
            output = Math.max(minPower, Math.min(output, maxPower));
        }

        //Returns the output
        return output;
    }

    public double getError(){
        return error;
    }

    /**
     * @param tolerance - How far the robot can be from target position
     */
    public void setTolerance(double tolerance){
        this.tolerance = tolerance;
    }

    /**
     * @return - Return true if error is within the tolerance
     */
    public boolean isFinished(){
        return(Math.abs(error) <= tolerance);
    }

}