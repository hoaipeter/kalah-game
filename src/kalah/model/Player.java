package kalah.model;

import kalah.util.PlayerDirection;

public class Player {
    private int id;
    private PlayerDirection direction;

    // Constructor
    public Player(int id, PlayerDirection playerDirection) {
        this.id = id;
        this.direction = playerDirection;
    }

    public int getId() {
        return id;
    }

    public PlayerDirection getDirection() {
        return direction;
    }
}
