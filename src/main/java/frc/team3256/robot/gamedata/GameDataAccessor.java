package frc.team3256.robot.gamedata;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.robot.Constants;
import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.modes.Final.CenterSwitchAuto;
import frc.team3256.robot.auto.modes.Final.CrossBaselineForwardAuto;
import frc.team3256.robot.auto.modes.Final.DoNothingAuto;

public class GameDataAccessor {

    private static String data = "";

    public static String getGameData() { return data = DriverStation.getInstance().getGameSpecificMessage(); }

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
        return data.length() > 0;
    }

    public static String getRawData(){
        return data;
    }

}

