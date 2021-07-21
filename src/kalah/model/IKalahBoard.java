package kalah.model;

import kalah.controller.exception.InvalidActionException;

import java.util.Map;

public interface IKalahBoard {
    Player getP1();

    Player getP2();

    Player getCurrentPlayer();

    Player getOpponentPlayer();

    PitCollection getPits();

    boolean isActive();

    void terminate();

    void executeMove(int position) throws InvalidActionException;
    
    void executeUndoMove() throws InvalidActionException;

    boolean canContinue();

    boolean isGameCompleted();

    Map<Player, Integer> getFinalScores();
}
