package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Field;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.figures.Figure;

/**
 * An abstract model of a player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public abstract class Player {
    private MyColor color;
    private long timeLeft;
    private boolean isCurrentPlayer;

    public Player() {
    }

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

    public void setColor(MyColor color) {
        if (this.color == null)
            this.color = color;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
    }

    public boolean isCurrentPlayer() {
        return isCurrentPlayer;
    }

    public void setCurrentPlayer(boolean isCurrentPlayer) {
        this.isCurrentPlayer = isCurrentPlayer;
    }

    public String getTimeString() {
        long secondsLeft = timeLeft / 1000;
        String minutes = String.valueOf(secondsLeft / 60);
        if (minutes.length() < 2) {
            minutes = "0" + minutes;
        }
        String seconds = String.valueOf(secondsLeft % 60);
        if (seconds.length() < 2) {
            seconds = "0" + seconds;
        }
        return minutes + ":" + seconds;
    }

    /**
     * @return true if player hasValidMoves, false otherwise
     */
    public boolean hasNoValidMoves(Board board) {
        boolean ret = false;
        for (int r = 0; r <= Board.MAX_ROW && !ret; r++) {
            for (int c = 0; c <= Board.MAX_COL && !ret; c++) {
                Field field = new Field(r, c);
                Figure fig = board.getFigure(field);
                if (fig != null && fig.getColor() == getColor() && fig.hasValidMoves()) {
                    ret = true;
                }
            }
        }
        return !ret;
    }
}
