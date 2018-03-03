package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.gamedata.GameDataAccessor;

public class CenterSwitchAuto extends AutoModeBase{

    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side switchSide = GameDataAccessor.Side.ERR;
        if (!GameDataAccessor.dataFound()){
            switchSide = GameDataAccessor.getSwitchSide();
        }
        if (switchSide == GameDataAccessor.Side.LEFT){
            //new CenterLeftSwitchAuto().routine();
        }
        else if (switchSide == GameDataAccessor.Side.RIGHT){
            //new CenterRightSwitchAuto().routine();
        }
        else{
            //new DoNothingAuto().routine();
            //Do nothing
        }
    }
}
