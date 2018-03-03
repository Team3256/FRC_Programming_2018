package frc.team3256.robot.auto.modes;

import frc.team3256.robot.auto.AutoModeBase;
import frc.team3256.robot.auto.AutoModeEndedException;
import frc.team3256.robot.gamedata.GameDataAccessor;

public class LeftScaleAuto extends AutoModeBase {
    @Override
    protected void routine() throws AutoModeEndedException {
        GameDataAccessor.Side scaleSide = GameDataAccessor.Side.ERR;
        if(!GameDataAccessor.dataFound()){
            scaleSide = GameDataAccessor.getScaleSide();
        }
        if(scaleSide == GameDataAccessor.Side.LEFT){

        }
        else if(scaleSide == GameDataAccessor.Side.RIGHT){

        }
        else{
            //Do Nothing
        }
    }
}
