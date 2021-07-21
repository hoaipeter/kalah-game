package kalah.controller;

import kalah.controller.exception.InvalidActionException;
import kalah.model.IKalahBoard;

public class PlayerAction implements IPlayerAction {
    private IKalahBoard kalahBoard;
    private int position;

    // Constructor
    public PlayerAction(IKalahBoard gameBoard, int position) {
        this.kalahBoard = gameBoard;
        this.position = position;
    }

    @Override
    public void execute() throws InvalidActionException {
        kalahBoard.executeMove(position);
    }
}
