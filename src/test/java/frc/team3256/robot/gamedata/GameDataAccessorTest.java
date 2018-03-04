package frc.team3256.robot.gamedata;

import frc.team3256.robot.auto.modes.Center.CenterLeftSwitchAuto;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameDataAccessorTest {

    @Test
    public void test(){
        GameDataAccessor.Side side = GameDataAccessor.Side.LEFT;
        String auto = "Center Switch";
        assertEquals(GameDataAccessor.getAutoMode(auto), new CenterLeftSwitchAuto());
    }
}