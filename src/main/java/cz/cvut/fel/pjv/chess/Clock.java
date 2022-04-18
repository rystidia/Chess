package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.Player;

/**
 * A clock for a chess game
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Clock implements Runnable {
    private Player white;
    private Player black;

    public Clock(Player white, Player black) {
        this.white = white;
        this.black = black;
    }

    public Player getWhite() {
        return white;
    }

    public Player getBlack() {
        return black;
    }

    public void setWhite(Player white) {
        this.white = white;
    }

    public void setBlack(Player black) {
        this.black = black;
    }

    /**
     * Calls runnable object
     */
    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {

            synchronized (this) {
                if (white.isCurrentPlayer()) {
                    white.setTimeLeft(white.getTimeLeft() - 1);
                } else {
                    black.setTimeLeft(black.getTimeLeft() - 1);
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (this.white.getTimeLeft() == 0){
                this.white.lose();
                stop = true;
            }
            if (this.black.getTimeLeft() == 0) {
                this.black.lose();
                stop = true;
            }
        }
    }

    public void switchPlayers() {
        if (white.isCurrentPlayer()) {
            white.setCurrentPlayer(false);
            black.setCurrentPlayer(true);
        } else {
            black.setCurrentPlayer(false);
            white.setCurrentPlayer(true);
        }
    }
}

