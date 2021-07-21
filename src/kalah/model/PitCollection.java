package kalah.model;

import kalah.util.Constants;

import java.util.*;

public class PitCollection {
    private static final int STORE_ID = 0;

    private Player p1;
    private Player p2;
    private Map<Player, List<Pit>> boardHouses;
    private Map<Player, Pit> boardStores;
    private Iterator<Pit> boardIterator;


    // Constructor
    public PitCollection(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        boardHouses = new HashMap<>();
        boardStores = new HashMap<>();
        initialisePits();
    }

    public Iterator<Pit> getPlayerHouses(Player player) {
        return boardHouses.get(player).iterator();
    }

    public Pit getPlayerHouse(Player player, int houseNum) {
        return boardHouses.get(player).get(convertToIndex(houseNum));
    }

    public Pit getPlayerStore(Player player) {
        return boardStores.get(player);
    }

    public Pit getNextPit() {
        if (boardIterator == null || !boardIterator.hasNext()) {
            boardIterator = resetBoardIterator();
        }

        return boardIterator.next();
    }

    public boolean isPlayersHouse(Player player, Pit pit) {
        return boardHouses.get(player).contains(pit);
    }

    public Pit getOppositePit(Player oppositePlayer, Pit currentPit) {
        int position = Constants.NUM_PITS + 1 - currentPit.getId();
        return boardHouses.get(oppositePlayer).get(convertToIndex(position));
    }

    public void configureIterator(Player currentPlayer, Player opponentPlayer, Pit chosenPit) {
        boardIterator = generateBoardIterator(currentPlayer, opponentPlayer, chosenPit.getId());
    }

    private void initialisePits() {
        List<Pit> p1Houses = new ArrayList<>();
        List<Pit> p2Houses = new ArrayList<>();

        for (int index = 1; index <= Constants.NUM_PITS; index++) {
            p1Houses.add(new Pit(index, Constants.NUM_SEEDS));
            p2Houses.add(new Pit(index, Constants.NUM_SEEDS));
        }

        boardHouses.put(p1, p1Houses);
        boardHouses.put(p2, p2Houses);
        boardStores.put(p1, new Pit(STORE_ID, Constants.NUM_STORE_SEEDS));
        boardStores.put(p2, new Pit(STORE_ID, Constants.NUM_STORE_SEEDS));
    }

    private Iterator<Pit> resetBoardIterator() {
        return generateBoardIterator(p1, p2, 0);
    }

    private Iterator<Pit> generateBoardIterator(Player playerA, Player playerB, int startIndex) {
        List<Pit> board = new ArrayList<>(boardHouses.get(playerA).subList(startIndex, Constants.NUM_PITS));
        board.add(boardStores.get(playerA));

        if (playerB != p1) {
            board.addAll(boardHouses.get(playerB));
            board.add(boardStores.get(playerB));
        }

        return board.iterator();
    }

    private int convertToIndex(int houseNum) {
        return houseNum - 1;
    }
}
