package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

public class KingTest {
    @Test
    public void testGetValidMoves_OutOfRange() {
        // FIXME: mock Board
        Board board = new Board();
        King figure = new King(MyColor.WHITE, board);
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
        Figure blockingMy1 = new Figure(MyColor.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy1, new Field(2, 4));

        Figure blockingOpp1 = new Figure(MyColor.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(1, 4));

        Figure blockingMy2 = new Figure(MyColor.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy2, new Field(3, 4));

        Figure blockingOpp2 = new Figure(MyColor.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(3, 2));

        King figure = new King(MyColor.WHITE, board);
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
        Rook myRook1 = new Rook(MyColor.WHITE, board);
        board.moveFigure(myRook1, new Field(7, 0));

        Rook myRook2 = new Rook(MyColor.WHITE, board);
        board.moveFigure(myRook2, new Field(7, 7));

        King figure = new King(MyColor.WHITE, board);
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
        Rook myRook1 = new Rook(MyColor.BLACK, board);
        board.moveFigure(myRook1, new Field(0, 0));

        Rook myRook2 = new Rook(MyColor.BLACK, board);
        board.moveFigure(myRook2, new Field(0, 7));

        Figure myFigure = new Figure(MyColor.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(myFigure, new Field(0, 1));

        King figure = new King(MyColor.BLACK, board);
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

    @Test
    public void testIsInCheck_WhiteKing1() {
        // FIXME: mock Board
        Board board = new Board();
        Rook oppRook = new Rook(MyColor.BLACK, board);
        board.moveFigure(oppRook, new Field(1, 4));

        Bishop oppBishop = new Bishop(MyColor.BLACK, board);
        board.moveFigure(oppBishop, new Field(2, 1));

        Pawn myPawn = new Pawn(MyColor.WHITE, board);
        board.moveFigure(myPawn, new Field(4, 4));

        Bishop myBishop = new Bishop(MyColor.WHITE, board);
        board.moveFigure(myBishop, new Field(3, 2));

        King king = new King(MyColor.WHITE, board);
        board.moveFigure(king, new Field(5, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - r - - -
        2 - b - - - - - -
   row  3 - - B - - - - -
        4 - - - - P - - -
        5 - - - - X - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */

        assertFalse(king.isInCheck());
    }

    @Test
    public void testIsInCheck_WhiteKing2() {
        // FIXME: mock Board
        Board board = new Board();
        Rook oppRook = new Rook(MyColor.BLACK, board);
        board.moveFigure(oppRook, new Field(1, 4));

        Bishop oppBishop = new Bishop(MyColor.BLACK, board);
        board.moveFigure(oppBishop, new Field(2, 1));

        Pawn myPawn = new Pawn(MyColor.WHITE, board);
        board.moveFigure(myPawn, new Field(4, 4));

        King figure = new King(MyColor.WHITE, board);
        board.moveFigure(figure, new Field(5, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - r - - -
        2 - b - - - - - -
   row  3 - - - - - - - -
        4 - - - - P - - -
        5 - - - - X - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        assertTrue(figure.isInCheck());
    }

    @Test
    public void testIsInCheck_BlackKing() {
        // FIXME: mock Board
        Board board = new Board();
        Knight oppKnight = new Knight(MyColor.WHITE, board);
        board.moveFigure(oppKnight, new Field(2, 2));

        King figure = new King(MyColor.BLACK, board);
        board.moveFigure(figure, new Field(1, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - X - - -
        2 - - K - - - - -
   row  3 - - - - - - - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        assertTrue(figure.isInCheck());
    }

}
