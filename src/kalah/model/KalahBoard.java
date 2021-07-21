package kalah.model;

import kalah.controller.exception.InvalidActionException;
import kalah.util.Constants;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class KalahBoard implements IKalahBoard {
    int MAX = 20;
    private Player p1;
    private Player p2;
    private Player currentPlayer;
    private PitCollection pits;
    private boolean isActive;
    private boolean gameCompleted;
    private int[][] p1House = new int[MAX][MAX];
    private int[][] p2House = new int[MAX][MAX];
    private int[] p1Stone = new int[MAX];
    private int[] p2Stone = new int[MAX];
    private Player lastPlayer;
    private int countMove = -1;

    // Constructor
    public KalahBoard(Player p1, Player p2, PitCollection pits) {
        this.p1 = p1;
        this.p2 = p2;
        this.pits = pits;

        this.currentPlayer = p1;
        this.isActive = true;
        this.gameCompleted = false;
    }

    @Override
    public Player getP1() {
        return p1;
    }

    @Override
    public Player getP2() {
        return p2;
    }

    @Override
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public Player getOpponentPlayer() {
        if (currentPlayer.equals(p1)) {
            return p2;
        } else {
            return p1;
        }
    }

    @Override
    public PitCollection getPits() {
        return pits;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void terminate() {
        isActive = false;
    }

    // Do a move and handle exception
    @Override
    public void executeMove(int position) throws InvalidActionException {

        //Save pits
        countMove++;
        for (int i = 0; i < 6; i++) {
            //p1 array
            p1House[countMove][i] = pits.getPlayerHouse(getP1(), i + 1).getSeeds();
            p2House[countMove][i] = pits.getPlayerHouse(getP2(), i + 1).getSeeds();
        }
        p1Stone[countMove] = pits.getPlayerStore(getP1()).getSeeds();
        p2Stone[countMove] = pits.getPlayerStore(getP2()).getSeeds();

        Pit house = pits.getPlayerHouse(currentPlayer, position);
        int seedCount = house.getSeeds();

        if (seedCount == 0) {
            throw new InvalidActionException("House is empty. Move again.");
        }

        if (lastPlayer == null)
            lastPlayer = currentPlayer;

        house.clearSeeds();
        Pit pit = null;
        pits.configureIterator(currentPlayer, getOpponentPlayer(), house);

        for (int i = 0; i < seedCount; i++) {
            pit = nextPit();
            pit.addSeed();
        }

        lastPlayer = currentPlayer;

        if (pit == null || isPlayersOwnStore(pit)) {
            // do nothing
        } else if (isOpponentsTurn(pit)) {
            switchPlayer();
        } else if (isCapture(pit)) {
            doCapture(pit);
        } else {
            switchPlayer();
        }
    }

    private void resetUndo() {
        p1House = new int[MAX][6];
        p2House = new int[MAX][6];
        p1Stone = new int[MAX];
        p2Stone = new int[MAX];
        countMove = -1;
    }

    private void switchPlayer() {
        if (currentPlayer.equals(p1)) {
            currentPlayer = p2;
        } else {
            currentPlayer = p1;
        }
        resetUndo();
    }

    @Override
    public boolean canContinue() {
        Iterator<Pit> playerPits = pits.getPlayerHouses(currentPlayer);

        while (playerPits.hasNext()) {
            if (playerPits.next().getSeeds() > 0) {
                return true;
            }
        }

        gameCompleted = true;
        return false;
    }

    @Override
    public boolean isGameCompleted() {
        return gameCompleted;
    }

    @Override
    public Map<Player, Integer> getFinalScores() {
        Map<Player, Integer> scores = new HashMap<>();
        scores.put(getOpponentPlayer(), getTotalSeeds() - pits.getPlayerStore(currentPlayer).getSeeds());
        scores.put(currentPlayer, pits.getPlayerStore(currentPlayer).getSeeds());
        return scores;
    }

    private Pit nextPit() {
        Pit nextPit = pits.getNextPit();

        while (nextPit.equals(pits.getPlayerStore(getOpponentPlayer()))) {
            nextPit = pits.getNextPit();
        }

        return nextPit;
    }

    /*
     *  Case 1: The last seed is sown in a house that is either not owned by the player,
     *  or the house already contains at least one seed. In which case it is the other player's turn.
     */
    private boolean isOpponentsTurn(Pit pit) {
        return pits.isPlayersHouse(getOpponentPlayer(), pit) || pit.getSeeds() > 1;
    }

    /*
     *  Case 2: The last seed is sown in the player's store. In which case the same player gets another move.
     */
    private boolean isPlayersOwnStore(Pit pit) {
        return pit.equals(pits.getPlayerStore(currentPlayer));
    }

    /*
     *  Case 3: The last seed is sown in one of the player's own houses, that house is empty,
     *  and the opposite house owned by the opponent has at least one seed in it.
     *  In which case the seed just sown and all the seeds in the opposite house are moved to the player's store.
     *  If the opposite house is empty, then no capture takes place.
     */
    private boolean isCapture(Pit pit) {
        return pit.getSeeds() == 1 && pits.isPlayersHouse(currentPlayer, pit)
                && pits.getOppositePit(getOpponentPlayer(), pit).getSeeds() > 0;
    }

    private void doCapture(Pit pit) {
        Pit oppositePit = pits.getOppositePit(getOpponentPlayer(), pit);
        pit.clearSeeds();

        // + 1 to account for the seed which caused the capture
        pits.getPlayerStore(currentPlayer).addSeeds(oppositePit.getSeeds() + 1);
        oppositePit.clearSeeds();

        switchPlayer();
    }

    private int getTotalSeeds() {
        return Constants.NUM_PLAYERS * Constants.NUM_PITS * Constants.NUM_SEEDS;
    }

    @Override
    public void executeUndoMove() throws InvalidActionException {

        if (countMove == -1) {
            System.out.println("Invalid command. Try again.");
        } else {
            //set seed undo
            for (int i = 0; i < 6; i++) {
                pits.getPlayerHouse(getP1(), i + 1).setSeed(p1House[countMove][i]);
                pits.getPlayerHouse(getP2(), i + 1).setSeed(p2House[countMove][i]);
            }

            //set seed player house
            pits.getPlayerStore(getP1()).setSeed(p1Stone[countMove]);
            pits.getPlayerStore(getP2()).setSeed(p2Stone[countMove]);

            countMove--;
        }
    }
}
