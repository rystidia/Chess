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

    }

    public MyColor getColor() {
        return color;
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

    public String getTimeString(){
        String minutes = String.valueOf(this.timeLeft / 60);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        String seconds = String.valueOf(this.timeLeft % 60);
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }

    /**
     * @return true if player hasValidMoves, false otherwise
     */
    public boolean hasValidMoves() {
        throw new UnsupportedOperationException();
    }
}
