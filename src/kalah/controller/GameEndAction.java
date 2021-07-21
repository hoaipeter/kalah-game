package kalah.controller;

import kalah.model.IKalahBoard;

public class GameEndAction implements IPlayerAction {
    private IKalahBoard kalahBoard;

    // Constructor
    public GameEndAction(IKalahBoard gameBoard) {
        this.kalahBoard = gameBoard;
    }

    @Override
    public void execute() {
        kalahBoard.terminate();
    }
}
