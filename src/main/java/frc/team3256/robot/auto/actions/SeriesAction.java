package frc.team3256.robot.auto.actions;

import java.util.ArrayList;
import java.util.List;

/**
 * Executes actions in a series (one after another).
 */
public class SeriesAction implements Action {
    private Action currentAction = null;
    private final ArrayList<Action> remainingActions;

    public SeriesAction(List<Action> actions) {
        remainingActions = new ArrayList<>(actions.size());
        remainingActions.addAll(actions);
    }

    @Override
    public boolean isFinished() {
        return remainingActions.isEmpty() && currentAction == null;
    }

    @Override
    public void update() {
        if (currentAction == null) {
            if (remainingActions.isEmpty())
                return;
            currentAction = remainingActions.remove(0);
            currentAction.start();
        }

        currentAction.update();

        if (currentAction.isFinished()) {
            currentAction.done();
            currentAction = null;
        }
    }

    @Override
    public void done() {

    }

    @Override
    public void start() {

    }
}
