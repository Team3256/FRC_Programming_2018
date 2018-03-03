package frc.team3256.robot.gamedata;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.robot.Constants;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.modes.CenterLeftSwitchAuto;
import frc.team3256.robot.auto.modes.CenterRightSwitchAuto;
import frc.team3256.robot.auto.modes.CrossBaselineAuto;
import frc.team3256.robot.auto.modes.DoNothingAuto;

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
        /*else if (wantedAuto == "LEFT SCALE"){
            if(getSwitchSide() == Side.ERR){
                return new CrossBaselineAuto();
            }
            else if (getSwitchSide() == Side.LEFT){
                return new CenterLeftSwitchAuto();
            }
            else if (getSwitchSide() == Side.RIGHT){
                return new CenterRightSwitchAuto();
            }
        }*/

        if (getScaleSide() == Side.ERR || getSwitchSide() == Side.ERR) {
            //just cross the baseline...
        }

        if(getScaleSide() == Side.LEFT) {
            if(getSwitchSide() == Side.LEFT) {

            }
            else if (getSwitchSide() == Side.RIGHT) {

            }
        }

        else if (getScaleSide() == Side.RIGHT) {
            if(getSwitchSide() == Side.LEFT) {

            }
            else if (getSwitchSide() == Side.RIGHT) {

            }
        }
        return new DoNothingAuto();
    }
}

