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
    public void makeMove() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return True if player lost the game, false otherwise
     */
    public boolean lost() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return True if player hasValidMoves, false otherwise
     */
    public boolean hasValidMoves() {
        throw new UnsupportedOperationException();
    }
}
