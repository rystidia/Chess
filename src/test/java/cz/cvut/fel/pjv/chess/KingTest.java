package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.King;
import cz.cvut.fel.pjv.chess.figures.Knight;
import cz.cvut.fel.pjv.chess.figures.Rook;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class KingTest {
    @Test
    public void testGetValidMoves_OutOfRange() {
        // FIXME: mock Board
        Board board = new Board();
        King figure = new King(Color.WHITE, board);
        board.moveFigure(figure, new Field(7, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - o o o - -
        7 - - - o X o - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(7, 3));
            add(new Field(6, 3));
            add(new Field(6, 4));
            add(new Field(6, 5));
            add(new Field(7, 5));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_BlockingFigures() {
        // FIXME: mock Board
        Board board = new Board();
        Figure blockingMy1 = new Figure(Color.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy1, new Field(2, 4));

        Figure blockingOpp1 = new Figure(Color.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(1, 4));

        Figure blockingMy2 = new Figure(Color.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy2, new Field(3, 4));

        Figure blockingOpp2 = new Figure(Color.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(3, 2));

        King figure = new King(Color.WHITE, board);
        board.moveFigure(figure, new Field(2, 3));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - o o p - - -
        2 - - o X m - - -
   row  3 - - p o m - - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(1, 4)); // can be captured
            // (1, -1)
            add(new Field(1, 3));
            add(new Field(3, 2)); // can be captured
            // (1, 1)
            add(new Field(2, 2));
            add(new Field(1, 2));
            add(new Field(3, 3));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_Castling() {
        // FIXME: mock Board
        Board board = new Board();
        Rook myRook1 = new Rook(Color.WHITE, board);
        board.moveFigure(myRook1, new Field(7, 0));

        Rook myRook2 = new Rook(Color.WHITE, board);
        board.moveFigure(myRook2, new Field(7, 7));

        King figure = new King(Color.WHITE, board);
        board.moveFigure(figure, new Field(7, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - o o o - -
        7 R - o o X o o R
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(7, 3));
            add(new Field(6, 3));
            add(new Field(6, 4));
            add(new Field(6, 5));
            add(new Field(7, 5));
            add(new Field(7, 2));
            add(new Field(7, 6));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_CastlingBlockingFigs() {
        // FIXME: mock Board
        Board board = new Board();
        Rook myRook1 = new Rook(Color.BLACK, board);
        board.moveFigure(myRook1, new Field(0, 0));

        Rook myRook2 = new Rook(Color.BLACK, board);
        board.moveFigure(myRook2, new Field(0, 7));

        Figure myFigure = new Figure(Color.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(myFigure, new Field(0, 1));

        King figure = new King(Color.BLACK, board);
        board.moveFigure(figure, new Field(0, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 R m - o X o o R
        1 - - - o o o - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(0, 3));
            add(new Field(1, 3));
            add(new Field(1, 4));
            add(new Field(1, 5));
            add(new Field(0, 5));
            add(new Field(0, 6));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }
}
