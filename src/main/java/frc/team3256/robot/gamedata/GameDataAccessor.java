package frc.team3256.robot.gamedata;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.robot.Constants;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.modes.*;

public class GameDataAccessor {
    public static String getGameData() {
        return DriverStation.getInstance().getGameSpecificMessage();
    }

    public static Side getSide(int index) {
        if (index < 0 || index > 2)
            return Side.ERR;
        String gameData = getGameData();
        if (gameData != null && gameData.length() == 3)
            return Side.fromCharacter(DriverStation.getInstance().getGameSpecificMessage().charAt(index));
        return Side.ERR;
    }

    public static Side getSwitchSide() {
        return getSide(Constants.kSwitchIndex);
    }

    public static Side getScaleSide() {
        return getSide(Constants.kScaleIndex);
    }

    public static Side getOpponentSwitchSide() {
        return getSide(Constants.kOpponentSwitchIndex);
    }

    public enum Side {
        LEFT,
        RIGHT,
        ERR;

        static Side fromCharacter(char c) {
            return c == 'L' ? LEFT : c == 'R' ? RIGHT : ERR;
        }
    }

    public static boolean dataFound(){
        return getGameData().length() > 0;
    }

    public static AutoModeBase getAutoMode(String wantedAuto) {

        if(wantedAuto == "CENTER SWITCH"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getSwitchSide() == Side.LEFT){
                return new CenterLeftSwitchAuto();
            }
            else if (getSwitchSide() == Side.RIGHT){
                return new CenterRightSwitchAuto();
            }
        }

        else if (wantedAuto == "LEFT SWITCH"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getSwitchSide() == Side.LEFT){
                //Return specified automode
            }
            else if (getSwitchSide() == Side.RIGHT){
                //Return specified automode
            }
        }

        else if (wantedAuto == "LEFT SCALE"){
            if(getScaleSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getScaleSide() == Side.LEFT){
                //Return specified automode
            }
            else if (getScaleSide() == Side.RIGHT){
                //Return specified automode
            }
        }

        else if (wantedAuto == "LEFT SCALE SWITCH"){
            if(getScaleSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if(getScaleSide() == Side.LEFT){
                if(getSwitchSide() == Side.ERR){
                    return new CrossBaselineAuto();
                }
                else if(getSwitchSide() == Side.LEFT){
                    return new LeftRobotLeftScaleLeftSwitchThreeCubeAuto();
                }
                else if(getSwitchSide() == Side.RIGHT){
                    return new LeftRobotLeftScaleRightSwitchThreeCubeAuto();
                }
            }
            else if(getScaleSide() == Side.RIGHT){
                if(getSwitchSide() == Side.ERR){
                    return new CrossBaselineAuto();
                }
                else if(getSwitchSide() == Side.LEFT){
                    return new LeftRobotRightScaleLeftSwitchThreeCubeAuto();
                }
                else if (getSwitchSide() == Side.RIGHT){
                    return new LeftRobotRightScaleRightSwitchThreeCubeAuto();
                }
            }
        }
        else if (wantedAuto == "RIGHT SWITCH"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getSwitchSide() == Side.LEFT){
                //Return specified automode
            }
            else if (getSwitchSide() == Side.RIGHT){
                //Return specified automode
            }
        }
        else if (wantedAuto == "RIGHT SCALE"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getSwitchSide() == Side.LEFT){
                //Return specified automode
            }
            else if (getSwitchSide() == Side.RIGHT){
                //Return specified automode
            }
        }
        else if (wantedAuto == "RIGHT SCALE SWITCH"){
            if(getScaleSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getScaleSide() == Side.LEFT){
                if(getSwitchSide() == Side.ERR){
                    return new CrossBaselineAuto();
                }
                else if(getSwitchSide() == Side.LEFT){
                    return new RightRobotLeftScaleLeftSwitchThreeCubeAuto();
                }
                else if(getSwitchSide() == Side.RIGHT){
                    return new RightRobotLeftScaleRightSwitchThreeCubeAuto();
                }
            }
            else if (getScaleSide() == Side.RIGHT){
                if(getSwitchSide() == Side.ERR){
                    return new CrossBaselineAuto();
                }
                else if(getSwitchSide() == Side.LEFT){
                    return new RightRobotRightScaleLeftSwitchThreeCubeAuto();
                }
                else if(getSwitchSide() == Side.RIGHT){
                    return new RightRobotRightScaleRightSwitchThreeCubeAuto();
                }
            }
        }
        return new DoNothingAuto();
    }
}

