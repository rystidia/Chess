package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Color;

/**
 * A local player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Player for the descrition of all methods
 */
public class LocalPlayer extends Player {
    public LocalPlayer(Color color) {
        super(color);
    }

    public void makeMove() {
        throw new UnsupportedOperationException();
    }
}
