package frc.team3256.lib.gamedata;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team3256.robot.Constants;

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
        return getSide(Constants.SWITCH_INDEX);
    }

    public static Side getScaleSide() {
        return getSide(Constants.SCALE_INDEX);
    }

    public static Side getOpponentSwitchSide() {
        return getSide(Constants.OPPONENT_SWITCH_INDEX);
    }

    public enum Side {
        LEFT,
        RIGHT,
        ERR;

        static Side fromCharacter(char c) {
            return c == 'L' ? LEFT : c == 'R' ? RIGHT : ERR;
        }
    }
}
