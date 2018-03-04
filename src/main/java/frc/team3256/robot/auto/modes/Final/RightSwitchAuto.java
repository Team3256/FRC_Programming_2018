package frc.team3256.robot.auto.modes.Final;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.gamedata.GameDataAccessor;

public class RightSwitchAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side switchSide = GameDataAccessor.Side.ERR;
        if(!GameDataAccessor.dataFound()){
            switchSide = GameDataAccessor.getSwitchSide();
        }
        if(switchSide == GameDataAccessor.Side.LEFT){

        }
        else if(switchSide == GameDataAccessor.Side.RIGHT){

        }
        else{
            //Do Nothing
        }
    }
}
