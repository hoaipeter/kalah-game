package kalah.view;

import kalah.controller.IPlayerAction;
import kalah.model.IKalahBoard;

public interface IBoardRender {
    void renderBoard(IKalahBoard kalahBoard);

    void renderTermination();

    void renderError(String errorMessage);

    void renderScores(IKalahBoard kalahBoard);

    IPlayerAction requestPlayerAction(IKalahBoard kalahBoard);
}
