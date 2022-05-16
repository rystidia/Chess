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
    private boolean won = false;
    private boolean lost = false;
    private boolean draw = false;

    private String name;

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

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public boolean isLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    /**
     * @return true if player lost the game, false otherwise
     */


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
        return getTimeString(false);
    }

    public String getTimeString(boolean withHours) {
        final long secondsLeft = timeLeft / 1000;
        long minutes = secondsLeft / 60;
        final long seconds = secondsLeft % 60;
        if (withHours) {
            long hours = minutes / 60;
            minutes %= 60;
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    public void setTimeString(String timeString) {
        String[] parts = timeString.split(":", 3);
        try {
            timeLeft = ((Long.parseLong(parts[0]) * 60 + Long.parseLong(parts[1])) * 60 + Long.parseLong(parts[2])) * 1000;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            timeLeft = 25 * 60 * 1000;
        }
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

    /**
     * @return Player name, or null if the name is not available
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
