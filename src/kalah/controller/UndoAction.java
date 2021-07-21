package kalah.controller;

import kalah.controller.exception.InvalidActionException;
import kalah.model.IKalahBoard;

public class UndoAction implements IPlayerAction {

    private IKalahBoard kalahBoard;

    // Constructor
    public UndoAction(IKalahBoard gameBoard) {
        this.kalahBoard = gameBoard;
    }

    @Override
    public void execute() throws InvalidActionException {
        kalahBoard.executeUndoMove();
    }
}
