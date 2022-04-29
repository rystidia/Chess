package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.Player;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

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
        boolean gameFinished = false;
        while (!gameFinished) {
            synchronized (this) {
                if (white.isCurrentPlayer()) {
                    white.setTimeLeft(white.getTimeLeft() - 1);
                } else {
                    black.setTimeLeft(black.getTimeLeft() - 1);
                }

                Platform.runLater(() -> {
                    redrawClockBox(white);
                    redrawClockBox(black);
                });
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (this.white.getTimeLeft() == 0) {
                this.white.lose();
                gameFinished = true;
            }
            if (this.black.getTimeLeft() == 0) {
                this.black.lose();
                gameFinished = true;
            }
        }
    }

    public void redrawClockBox(Player player){
        BorderPane timeBox = player.getColor() == MyColor.WHITE ? timeWhite : timeBlack;
        Label playerTime = new Label(player.getTimeString());
        playerTime.setFont(new Font("Segoe UI", 30));
        timeBox.setCenter(playerTime);
    }
}

