package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.Player;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.util.Date;

/**
 * A clock for a chess game
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Clock implements Runnable {
    private final Player white;
    private final Player black;
    private volatile boolean shutdown = false;

    private final BorderPane timeWhite;
    private final BorderPane timeBlack;

    public Clock(Player white, Player black, BorderPane timeWhite, BorderPane timeBlack) {
        this.white = white;
        this.black = black;
        this.timeWhite = timeWhite;
        this.timeBlack = timeBlack;
    }

    /**
     * Calls runnable object
     */
    @Override
    public void run() {
        long timeElapsed = 0;
        boolean gameFinished = false;

        while (!gameFinished && !shutdown) {

            synchronized (this) {
                if (white.isCurrentPlayer()) {
                    white.setTimeLeft(white.getTimeLeft() - timeElapsed);
                } else {
                    black.setTimeLeft(black.getTimeLeft() - timeElapsed);
                }

                Platform.runLater(() -> {
                    redrawClockBox(white);
                    redrawClockBox(black);
                });
            }

            long startTimeStamp = new Date().getTime();
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long actualTimeStamp = new Date().getTime();
            timeElapsed = actualTimeStamp - startTimeStamp;

            if (this.white.getTimeLeft() <= 0) {
                this.white.lose();
                gameFinished = true;
            }
            if (this.black.getTimeLeft() <= 0) {
                this.black.lose();
                gameFinished = true;
            }
        }
    }

    public void shutdown() {
        shutdown = true;
    }

    public void redrawClockBox(Player player){
        BorderPane timeBox = player.getColor() == MyColor.WHITE ? timeWhite : timeBlack;
        Label playerTime = new Label(player.getTimeString());
        playerTime.setFont(new Font("Segoe UI", 30));
        timeBox.setCenter(playerTime);
    }
}

