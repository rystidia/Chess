package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.Knight;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.testng.Assert.assertEquals;

public class KnightTest {
    @Test
    public void testGetValidMoves_OutOfRange() {
        // FIXME: mock Board
        Board board = new Board();
        Knight figure = new Knight(Color.WHITE, board);
        board.moveFigure(figure, new Field(6, 2));
        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - -
        2 - - - - - - - -
   row  3 - - - - - - - -
        4 - o - o - - - -
        5 o - - - o - - -
        6 - - X - - - - -
        7 o - - - o - - -
         */
        Set<Field> expected = new HashSet<>() {{
            add(new Field(7, 0));
            add(new Field(5, 0));
            add(new Field(4, 1));
            add(new Field(4, 3));
            add(new Field(5, 4));
            add(new Field(7, 4));
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
        board.moveFigure(blockingMy1, new Field(0, 2));

        Figure blockingOpp1 = new Figure(Color.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(0, 4));

        Figure blockingMy2 = new Figure(Color.WHITE, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy2, new Field(3, 5));

        Figure blockingOpp2 = new Figure(Color.BLACK, board) {
            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(3, 1));

        Knight figure = new Knight(Color.WHITE, board);
        board.moveFigure(figure, new Field(2, 3));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - m - p - - -
        1 - o - - - o - -
        2 - - - X - - - -
   row  3 - p - - - m - -
        4 - - o - o - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            // (-1, -1)
            // (-1, 1)
            add(new Field(0, 4)); // can be captured
            // (1, -1)
            add(new Field(1, 1));
            add(new Field(3, 1)); // can be captured
            // (1, 1)
            add(new Field(4, 2));
            add(new Field(1, 5));
            add(new Field(4, 4));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }
}
