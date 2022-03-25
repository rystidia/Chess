package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Color;

/**
 * An AI player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Player for the descrition of all methods
 */
public class AIPlayer extends Player {
    /**
     * Initializes the player and sets the given color to him
     * <p>
     *
     * @param color a color
     */
    public AIPlayer(Color color) {
        super(color);
    }

    @Override
    public void makeMove() {
        throw new UnsupportedOperationException();
    }
}
