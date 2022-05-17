package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.AIPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;

/**
 * Game controller of the chess game
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class GameController {

    private final Player white;
    private final Player black;

    private final Board board;
    private final GameScene game;

    public GameController(Player white, Player black, GameScene game, Board board) {
        this.white = white;
        this.black = black;
        this.game = game;
        this.board = board;
    }

    public void start() {
        if (getCurPlayer() instanceof AIPlayer) {
            game.AIPlayerMove(board);
        }
        if (getCurPlayer() instanceof RemotePlayer) {
            getCurPlayer().makeMove(board);
        }
    }

    public Player getCurPlayer() {
        return white.isCurrentPlayer() ? white : black;
    }


    /**
     * Switches the current player.
     */
    public void switchCurPlayer() {
        if (white.isCurrentPlayer()) {
            white.setCurrentPlayer(false);
            black.setCurrentPlayer(true);
        } else {
            black.setCurrentPlayer(false);
            white.setCurrentPlayer(true);
        }
        if (getCurPlayer() instanceof RemotePlayer) {
            getCurPlayer().makeMove(board);
        }
        if (isCheckMate(board)) {
            getOpponent(getCurPlayer()).setWon(true);
            getCurPlayer().setLost(true);
        }
        if (isStaleMate(board)) {
            getOpponent(getCurPlayer()).setDraw(true);
            getCurPlayer().setDraw(true);
        }
    }

    public boolean isCurrentColor(MyColor color) {
        return white.isCurrentPlayer() ? color == MyColor.WHITE : color == MyColor.BLACK;
    }

    /**
     * Returns the opponent of the given player.
     */
    public Player getOpponent(Player player) {
        return player.getColor() == MyColor.WHITE ? black : white;
    }

    /**
     * Returns true king of the current player is in stalemate on the given board.
     */
    public boolean isStaleMate(Board board) {
        return isStaleMate(board, getCurPlayer());
    }

    /**
     * Returns true king of the current player is in checkmate on the given board.
     */
    public boolean isCheckMate(Board board) {
        return isCheckMate(board, getCurPlayer());
    }

    /**
     * Returns true king of the given player is in stalemate on the given board.
     */
    public boolean isStaleMate(Board board, Player player) {
        return player.hasNoValidMoves(board) && !board.getKing(player.getColor()).isInCheck();
    }

    /**
     * Returns true king of the given player is in checkmate on the given board.
     */
    public boolean isCheckMate(Board board, Player player) {
        return player.hasNoValidMoves(board) && board.getKing(player.getColor()).isInCheck();
    }
}

