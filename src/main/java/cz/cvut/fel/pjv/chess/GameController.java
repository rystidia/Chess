package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.AIPlayer;
import cz.cvut.fel.pjv.chess.players.Player;
import cz.cvut.fel.pjv.chess.players.RemotePlayer;

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

    public void start(){
        if (getCurPlayer() instanceof AIPlayer){
            game.AIPlayerMove(board);
        }
        if (getCurPlayer() instanceof RemotePlayer){
            getCurPlayer().makeMove(board);
        }
    }

    public Player getCurPlayer() {
        return white.isCurrentPlayer() ? white : black;
    }

    public void switchCurPlayer() {
        if (white.isCurrentPlayer()) {
            white.setCurrentPlayer(false);
            black.setCurrentPlayer(true);
        } else {
            black.setCurrentPlayer(false);
            white.setCurrentPlayer(true);
        }
        if (getCurPlayer() instanceof RemotePlayer){
            getCurPlayer().makeMove(board);
        }
        if (isCheckMate(board)){
            getOpponent(getCurPlayer()).setWon(true);
            getCurPlayer().setLost(true);
        }
        if (isStaleMate(board)){
            getOpponent(getCurPlayer()).setDraw(true);
            getCurPlayer().setDraw(true);
        }
    }

    public boolean isCurrentColor(MyColor color) {
        return white.isCurrentPlayer() ? color == MyColor.WHITE : color == MyColor.BLACK;
    }

    public Player getOpponent(Player player){
        return player.getColor() == MyColor.WHITE ? black : white;
    }

    public boolean isStaleMate(Board board){
        Player curPlayer = getCurPlayer();
        return curPlayer.hasNoValidMoves(board) && !board.getKing(curPlayer.getColor()).isInCheck();
    }

    public boolean isCheckMate(Board board){
        Player curPlayer = getCurPlayer();
        return curPlayer.hasNoValidMoves(board) && board.getKing(curPlayer.getColor()).isInCheck();
    }
}

