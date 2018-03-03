package frc.team3256.robot.gamedata;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.robot.Constants;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.modes.*;

public class GameDataAccessor {
    public static String getGameData() { return DriverStation.getInstance().getGameSpecificMessage(); }

    public static Side getSide(int index) {
        if (index < 0 || index > 2)
            return Side.ERR;
        String gameData = getGameData();
        if (gameData != null && gameData.length() == 3)
            return Side.fromCharacter(DriverStation.getInstance().getGameSpecificMessage().charAt(index));
        return Side.ERR;
    }

    public static Side getSwitchSide() { return getSide(Constants.kSwitchIndex); }

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

        /**
         * For the automodes that we want currently for San Diego ↓
         */
        if(wantedAuto == "ROBOT CENTER - SWITCH"){
            return new CenterSwitchAuto();
        }
        else if(wantedAuto == "ROBOT LEFT - SWITCH"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if(getSwitchSide() == Side.LEFT){
                //return new LeftRobotLeftSwitchAuto(); Not an automode yet
            }
            else if (getSwitchSide() == Side.RIGHT){
                return new CrossBaselineAuto();
            }
        }
        else if(wantedAuto == "ROBOT LEFT - SCALE"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getScaleSide() == Side.LEFT){
                //return new LeftRobotLeftScaleAuto(); Not an automode yet
            }
            else if (getScaleSide() == Side.RIGHT){
                //return new LeftRobotRightScaleAuto();  Not an automode yet
            }
        }
        /**
         * Will probably not be used/finished by the time we get to San Diego ↓
         */
        /*else if(wantedAuto == "ROBOT LEFT - SCALE SWITCH"){
            if(getScaleSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if(getScaleSide() == Side.LEFT){
                if(getSwitchSide() == Side.LEFT){
                    return new LeftRobotLeftScaleLeftSwitchThreeCubeAuto();
                }
                else if(getSwitchSide() == Side.RIGHT){
                    return new LeftRobotLeftScaleRightSwitchThreeCubeAuto();
                }
            }
            else if(getScaleSide() == Side.RIGHT){
                if(getSwitchSide() == Side.LEFT){
                    return new LeftRobotRightScaleLeftSwitchThreeCubeAuto();
                }
                else if(getSwitchSide() == Side.RIGHT){
                    return new LeftRobotRightScaleRightSwitchThreeCubeAuto();
                }
            }
        }*/


        /**
         * For when we have all of our automodes figured out ↓
         */
        /*if(wantedAuto == "ROBOT CENTER - SWITCH"){
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

        else if (wantedAuto == "ROBOT LEFT - SWITCH"){
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

        else if (wantedAuto == "ROBOT LEFT - SCALE"){
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

        else if (wantedAuto == "ROBOT LEFT - SCALE SWITCH"){
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
        else if (wantedAuto == "ROBOT RIGHT - SWITCH"){
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
        else if (wantedAuto == "ROBOT RIGHT - SCALE"){
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
        else if (wantedAuto == "ROBOT RIGHT - SCALE SWITCH"){
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
        }*/
        return new DoNothingAuto();
    }
}

