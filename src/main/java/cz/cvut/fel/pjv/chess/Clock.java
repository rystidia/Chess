package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;
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
    private final BorderPane timeWhite;
    private final BorderPane timeBlack;
    private volatile boolean shutdown = false;

    public Clock(Player white, Player black, BorderPane timeWhite, BorderPane timeBlack) {
        this.white = white;
        this.black = black;
        this.timeWhite = timeWhite;
        this.timeBlack = timeBlack;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * Starts the timer.
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

            if (white.getTimeLeft() <= 0) {
                white.setLost(true);
                black.setWon(true);
                gameFinished = true;
            }
            if (black.getTimeLeft() <= 0) {
                black.setLost(true);
                black.setWon(true);
                gameFinished = true;
            }
        }
    }

    /**
     * Shutdowns the clock.
     */
    public void shutdown() {
        shutdown = true;
    }

    /**
     * Redraws the clock of the given player.
     */
    public void redrawClockBox(Player player) {
        BorderPane timeBox = player.getColor() == MyColor.WHITE ? timeWhite : timeBlack;
        if (player.isWon()) {
            if (player instanceof RemotePlayer){
                ((RemotePlayer) player).gameEnd(player.getColor());
            }
            timeBox.setStyle("-fx-background-color: #87e199");
            shutdown();
        } else if (player.isLost()) {
            timeBox.setStyle("-fx-background-color: #ec8780");
            shutdown();
        } else if (player.isDraw()) {
            if (player instanceof RemotePlayer && player.getColor() == MyColor.WHITE){
                ((RemotePlayer) player).gameEnd(null);
            }
            timeBox.setStyle("-fx-background-color: #edb13d");
            shutdown();
        }
        Label playerTime = new Label(player.getTimeString());
        playerTime.setFont(new Font("Segoe UI", 30));
        timeBox.setCenter(playerTime);
    }
}

