package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Color;

/**
 * An abstract model of a player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Player {
    private final Color color;

    /**
     * Initializes the player and sets the given color to him
     * <p>
     *
     * @param color a color
     */
    public Player(Color color) {
        this.color = color;
    }

    /**
     * Executes the given move
     */
    abstract void makeMove();

    /**
     * @return true if player lost the game, false otherwise
     */
    public boolean lost() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if player hasValidMoves, false otherwise
     */
    public boolean hasValidMoves() {
        throw new UnsupportedOperationException();
    }
}
