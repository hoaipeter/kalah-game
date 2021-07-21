package kalah.engine;

import kalah.controller.exception.InvalidActionException;
import kalah.view.IBoardRender;
import kalah.model.*;
import kalah.util.*;

public class KalahEngine {
    private static final int PLAYER1 = 1;
    private static final int PLAYER2 = 2;

    private IKalahBoard kalahBoard;
    private IBoardRender boardRender;

    // Constructor
    public KalahEngine(IBoardRender boardRender) {
        this.boardRender = boardRender;
        initialise();
    }

    public void play() {
        while (kalahBoard.isActive()) {
            boardRender.renderBoard(kalahBoard);
            try {
                boardRender.requestPlayerAction(kalahBoard).execute();
            } catch (InvalidActionException e) {
                boardRender.renderError(e.getMessage());
            }
        }
        boardRender.renderTermination();
        boardRender.renderBoard(kalahBoard);

        if (kalahBoard.isGameCompleted()) {
            boardRender.renderScores(kalahBoard);
        }
    }

    private void initialise() {
        // Initialise Players
        Player p1 = new Player(PLAYER1, PlayerDirection.RIGHT);
        Player p2 = new Player(PLAYER2, PlayerDirection.LEFT);

        // Initialise Board
        kalahBoard = new KalahBoard(p1, p2, new PitCollection(p1, p2));
    }
}
