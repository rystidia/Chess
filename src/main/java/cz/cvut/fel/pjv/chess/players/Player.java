package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.MyColor;

/**
 * An abstract model of a player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Player {
    private final MyColor color;
    private int timeLeft;
    private boolean isCurrentPlayer;

    /**
     * Initializes the player and sets the given color to him
     * <p>
     *
     * @param color a color
     */
    public Player(MyColor color) {
        this.color = color;
    }

    /**
     * Executes the given move
     */
    public abstract void makeMove(Board board);

    /**
     * @return true if player lost the game, false otherwise
     */
    public boolean lost() {
        throw new UnsupportedOperationException();
    }

    public void lose() {
        throw new UnsupportedOperationException();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
    }

    /**
     * @return true if player hasValidMoves, false otherwise
     */
    public boolean hasValidMoves() {
        throw new UnsupportedOperationException();
    }
}
