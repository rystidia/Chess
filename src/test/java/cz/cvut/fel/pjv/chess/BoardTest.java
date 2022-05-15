package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.*;

public class BoardTest {

    @Test
    public void testGetValidMoves_Castling() {
        // FIXME: mock Board
        Board board = new Board();

        Rook oppRook1 = new Rook(MyColor.BLACK, board);
        board.moveFigure(oppRook1, new Field(3, 3));

        Rook oppRook2 = new Rook(MyColor.BLACK, board);
        board.moveFigure(oppRook2, new Field(3, 6));

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
   row  3 - - - r - - r -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - - o o - -
        7 R - - - X o - R
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(6, 4));
            add(new Field(6, 5));
            add(new Field(7, 5));
        }};
        assertEquals(board.getValidMoves(figure), expected);
    }

    @Test
    public void testGetValidMoves_Figure() {
        // FIXME: mock Board
        Board board = new Board();
        Rook oppRook = new Rook(MyColor.BLACK, board);
        board.moveFigure(oppRook, new Field(1, 4));

        Bishop oppBishop = new Bishop(MyColor.BLACK, board);
        board.moveFigure(oppBishop, new Field(2, 1));

        Pawn myPawn = new Pawn(MyColor.WHITE, board);
        board.moveFigure(myPawn, new Field(4, 4));

        Bishop myBishop = new Bishop(MyColor.WHITE, board);
        board.moveFigure(myBishop, new Field(4, 3));

        King king = new King(MyColor.WHITE, board);
        board.moveFigure(king, new Field(5, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - r - - -
        2 - b - - - - - -
   row  3 - - o - - - - -
        4 - - - B P - - -
        5 - - - - X - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(2, 1));
            add(new Field(3, 2));
        }};
        assertEquals(board.getValidMoves(myBishop), expected);
    }

    @Test
    public void testIsInCheck_WhiteKing2() {
        // FIXME: mock Board
        Board board = new Board();
        Rook oppRook = new Rook(MyColor.BLACK, board);
        board.moveFigure(oppRook, new Field(1, 4));

        Bishop oppBishop = new Bishop(MyColor.BLACK, board);
        board.moveFigure(oppBishop, new Field(2, 1));

        Rook myRook = new Rook(MyColor.WHITE, board);
        board.moveFigure(myRook, new Field(4, 3));

        King figure = new King(MyColor.WHITE, board);
        board.moveFigure(figure, new Field(5, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - r - - -
        2 - b - - - - - -
   row  3 - - - - - - - -
        4 - - - P - - - -
        5 - - - - X - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        assertTrue(board.getValidMoves(myRook).isEmpty());
    }

    @Test
    public void testToFEN() {
        Board board = new Board();
        board.initialPosition();
        assertEquals(board.toFEN(), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }
}
