package kalah.view;

import com.qualitascorpus.testsupport.IO;
import kalah.controller.IPlayerAction;
import kalah.controller.GameEndAction;
import kalah.controller.PlayerAction;
import kalah.controller.UndoAction;
import kalah.model.*;
import kalah.util.*;

import java.util.Iterator;
import java.util.Map;

public class BoardRender implements IBoardRender {
    // Create constants to render the board
    private static final int MINIMUM_DOUBLE_DIGITS = 10;
    private static final int BASE = 0;

    private static final String BOARD_BORDER_SIDES = "+----+";
    private static final String BOARD_HOUSE = "-------";
    private static final String BOARD_DIVIDER_SIDES = "|    |";
    private static final String BOARD_SPECIAL = "+";
    private static final String BOARD_PLAYER = "|%s%s%s|";
    private static final String BOARD_PIT_SINGLE = "| %d[ %d] ";
    private static final String BOARD_PIT_DOUBLE = "| %d[%d] ";
    private static final String BOARD_NAME = " P%s ";
    private static final String BOARD_STORE_SINGLE = "  %d ";
    private static final String BOARD_STORE_DOUBLE = " %d ";
    private static final String BOARD_VERTICAL_DIVIDER = "|";

    // This comment is used for run given test
    // private static final String BOARD_INPUT = "Player P%d's turn - Specify house number or 'q' to quit: ";

    // This is used for play with undo feature
    private static final String BOARD_INPUT = "Player P%d's turn - Specify house number, 'u' to undo or 'q' to quit: ";

    private IO io;
    private String border;
    private String divider;

    // Constructor
    public BoardRender(IO io) {
        this.io = io;
        this.border = renderLine(BOARD_BORDER_SIDES);
        this.divider = renderLine(BOARD_DIVIDER_SIDES);
    }

    @Override
    public void renderBoard(IKalahBoard kalahBoard) {
        Player p1 = kalahBoard.getP1();
        Player p2 = kalahBoard.getP2();
        PitCollection pits = kalahBoard.getPits();

        io.println(border);
        io.println(String.format(BOARD_PLAYER, renderPlayerName(p2), renderPlayerHouses(pits, p2), renderPlayerStore(pits, p1)));
        io.println(divider);
        io.println(String.format(BOARD_PLAYER, renderPlayerStore(pits, p2), renderPlayerHouses(pits, p1), renderPlayerName(p1)));
        io.println(border);
    }

    @Override
    public IPlayerAction requestPlayerAction(IKalahBoard kalahBoard) {
        if (!kalahBoard.canContinue()) {
            return new GameEndAction(kalahBoard);
        }

        String response = io.readFromKeyboard(String.format(BOARD_INPUT, kalahBoard.getCurrentPlayer().getId()));

        if (isValid(response)) {
            if (response.equals("q") || response.equals("Q")) {
                System.out.println("Press Q");
                return new GameEndAction(kalahBoard);
            } else if (response.equals("u") || response.equals("U")) {
                //Undo
                return new UndoAction(kalahBoard);
            } else {
                try {
                    return new PlayerAction(kalahBoard, Integer.parseInt(response));
                } catch (NumberFormatException e) {
                    // do nothing as it will fall through to return
                    System.out.println("NumberFormatEx");
                }
            }
        }
        return requestPlayerAction(kalahBoard);
    }

    @Override
    public void renderTermination() {
        io.println("Game over");
    }

    @Override
    public void renderError(String errorMessage) {
        io.println(errorMessage);
    }

    // Display the winner
    @Override
    public void renderScores(IKalahBoard kalahBoard) {
        Map<Player, Integer> scores = kalahBoard.getFinalScores();
        io.println(String.format("\tplayer %d:%d", kalahBoard.getP1().getId(), scores.get(kalahBoard.getP1())));
        io.println(String.format("\tplayer %d:%d", kalahBoard.getP2().getId(), scores.get(kalahBoard.getP2())));

        if (scores.get(kalahBoard.getP1()) > scores.get(kalahBoard.getP2())) {
            io.println(String.format("Player %d wins!", kalahBoard.getP1().getId()));
        } else if (scores.get(kalahBoard.getP1()) < scores.get(kalahBoard.getP2())) {
            io.println(String.format("Player %d wins!", kalahBoard.getP2().getId()));
        } else {
            io.println("A tie!");
        }
    }

    private String renderPlayerHouses(PitCollection pits, Player player) {
        StringBuilder output = new StringBuilder();
        Iterator<Pit> playerHouses = pits.getPlayerHouses(player);

        int index = 1;
        if (player.getDirection().equals(PlayerDirection.RIGHT)) {
            while (playerHouses.hasNext()) {
                output.append(getFormattedHouse(playerHouses.next(), index));
                index++;
            }
        } else {
            while (playerHouses.hasNext()) {
                output.insert(BASE, getFormattedHouse(playerHouses.next(), index));
                index++;
            }
        }

        output.append(BOARD_VERTICAL_DIVIDER);
        return output.toString();
    }

    private String renderPlayerName(Player player) {
        return String.format(BOARD_NAME, player.getId());
    }

    private String renderPlayerStore(PitCollection pits, Player player) {
        int seeds = pits.getPlayerStore(player).getSeeds();

        if (seeds < MINIMUM_DOUBLE_DIGITS) {
            return String.format(BOARD_STORE_SINGLE, seeds);
        } else {
            return String.format(BOARD_STORE_DOUBLE, seeds);
        }
    }

    private String renderLine(String sides) {
        StringBuilder line = new StringBuilder(sides);
        for (int i = 0; i < Constants.NUM_PITS; i++) {
            line.append(BOARD_HOUSE);
            if (i != Constants.NUM_PITS - 1) {
                line.append(BOARD_SPECIAL);
            }
        }
        line.append(sides);
        return line.toString();
    }

    // Check whether input is valid
    private boolean isValid(String response) {
        if (response == null || response.isEmpty()) {
            return false;
        }

        if (response.equals("q") || response.equals("Q")) {
            return true;
        } else if (response.equals("u") || response.equals("U")) {
            return true;
        } else {
            try {
                Integer.parseInt(response);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    // Get formatted house when seeds > 10
    private String getFormattedHouse(Pit house, Integer index) {
        int seeds = house.getSeeds();
        String fmt = seeds < MINIMUM_DOUBLE_DIGITS ? BOARD_PIT_SINGLE : BOARD_PIT_DOUBLE;
        return String.format(fmt, index, seeds);
    }
}