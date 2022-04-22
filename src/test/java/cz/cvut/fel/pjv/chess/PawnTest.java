package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.Pawn;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class PawnTest {

    @Test
    public void testGetValidMoves_WhiteFirstMove() {
        // FIXME: mock Board
        Board board = new Board();
        Pawn figure = new Pawn(MyColor.WHITE, board);
        board.moveFigure(figure, new Field(6, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 - - - - o - - -
        5 - - - - o - - -
        6 - - - - X - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(5, 4));
            add(new Field(4, 4));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_White() {
        // FIXME: mock Board
        Board board = new Board();
        Pawn figure = new Pawn(MyColor.WHITE, board) {
            @Override
            public boolean isFirstMove() {
                return false;
            }
        };
        board.moveFigure(figure, new Field(5, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 - - - - o - - -
        5 - - - - X - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(4, 4));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_BlackFirstMove() {
        // FIXME: mock Board
        Board board = new Board();
        Pawn figure = new Pawn(MyColor.BLACK, board);
        board.moveFigure(figure, new Field(1, 4));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - X - - -
        2 - - - - o - - -
   row  3 - - - - o - - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(2, 4));
            add(new Field(3, 4));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_BlockingFiguresWhite() {
        // FIXME: mock Board
        Board board = new Board();

        Figure blockingOpp1 = new Figure(MyColor.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(3, 2));

        Figure blockingOpp2 = new Figure(MyColor.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(3, 4));

        Pawn figure = new Pawn(MyColor.WHITE, board) {
            @Override
            public boolean isFirstMove() {
                return false;
            }
        };
        board.moveFigure(figure, new Field(4, 3));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - p o p - - -
        4 - - - X - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(3, 2)); // can be captured
            add(new Field(3, 3));
            add(new Field(3, 4)); // can be captured
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_BlockingFiguresBlack() {
        // FIXME: mock Board
        Board board = new Board();

        Figure blockingOpp1 = new Figure(MyColor.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(4, 1));

        Figure blockingMy1 = new Figure(MyColor.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy1, new Field(4, 0));

        Pawn figure = new Pawn(MyColor.BLACK, board) {
            @Override
            public boolean isFirstMove() {
                return false;
            }
        };
        board.moveFigure(figure, new Field(3, 0));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 X - - - - - - -
        4 m p - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(4, 1)); // can be captured
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_BlockingFigures2Black() {
        // FIXME: mock Board
        Board board = new Board();

        Figure blockingOpp1 = new Figure(MyColor.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(4, 1));

        Figure blockingOpp2 = new Figure(MyColor.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(4, 0));

        Pawn figure = new Pawn(MyColor.BLACK, board) {
            @Override
            public boolean isFirstMove() {
                return false;
            }
        };
        board.moveFigure(figure, new Field(3, 0));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 X - - - - - - -
        4 p p - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(4, 1)); // can be captured
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_EnPassantBlack() {
        // FIXME: mock Board
        Board board = new Board();

        Pawn oppPawn = new Pawn(MyColor.WHITE, board) {
            @Override
            public boolean doubleAdvance() {
                return true;
            }
        };
        board.moveFigure(oppPawn, new Field(4, 1));

        Pawn figure = new Pawn(MyColor.BLACK, board) {
            @Override
            public boolean isFirstMove() {
                return false;
            }
        };
        board.moveFigure(figure, new Field(4, 0));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 X P - - - - - -
        5 o o - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(5, 0));
            add(new Field(5, 1)); // can be captured
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetValidMoves_EnPassantWhite() {
        // FIXME: mock Board
        Board board = new Board();

        Pawn oppPawn1 = new Pawn(MyColor.BLACK, board) {
            @Override
            public boolean doubleAdvance() {
                return true;
            }
        };
        board.moveFigure(oppPawn1, new Field(3, 3));

        Pawn oppPawn2 = new Pawn(MyColor.BLACK, board) {
            @Override
            public boolean doubleAdvance() {
                return true;
            }
        };
        board.moveFigure(oppPawn2, new Field(3, 5));

        Pawn figure = new Pawn(MyColor.WHITE, board) {
            @Override
            public boolean isFirstMove() {
                return false;
            }
        };
        board.moveFigure(figure, new Field(3, 4));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - o o o - -
   row  3 - - - P X P - -
        4 - - - - - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(2, 4));
            add(new Field(2, 3)); // can be captured
            add(new Field(2, 5)); // can be captured
        }};
        assertEquals(figure.getValidMoves(), expected);
    }
}
