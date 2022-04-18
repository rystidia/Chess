package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.players.Player;

/**
 * A main class of the program
 * Runs chess game
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Game {
    private final Player white;
    private final Player black;
    Parser parser = new Parser();
    private Board board;
//    private Clock clock = new Clock();

    /**
     * Initializes new game
     */
    public Game(Player white, Player black) {
        this.white = white;
        this.black = black;
        this.board = new Board();
    }

    /**
     * Initializes a saved game so that players can continue it
     */
    public Game(Player white, Player black, String PGN) {
        this.white = white;
        this.black = black;
        this.board = parser.parsePGN(PGN);
    }

    public static void main(String[] args) {

    }

    /**
     * Runs the game
     * Tracks the players' moves
     * Checks if one of the players won
     * Controls time of each move
     */
    public void runGame() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return true if there is a draw, false otherwise
     */
    public boolean draw(Board board) {
        throw new UnsupportedOperationException();
    }
}
