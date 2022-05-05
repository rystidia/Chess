package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

import java.util.*;

public class FigureTest {
    @Test
    public void testGetDiagonalDirections_EmptyBoard() {
        // FIXME: mock Board
        Board board = new Board();
        Figure figure = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return getDiagonalDirections();
            }
        };
        board.moveFigure(figure, new Field(6, 2));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - - - - - o
        2 - - - - - - o -
   row  3 - - - - - o - -
        4 o - - - o - - -
        5 - o - o - - - -
        6 - - X - - - - -
        7 - o - o - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            // (-1, -1)
            add(new Field(5, 1));
            add(new Field(4, 0));
            // (-1, 1)
            add(new Field(5, 3));
            add(new Field(4, 4));
            add(new Field(3, 5));
            add(new Field(2, 6));
            add(new Field(1, 7));
            // (1, -1)
            add(new Field(7, 1));
            // (1, 1)
            add(new Field(7, 3));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetVertAndHorDirections_EmptyBoard() {
        // FIXME: mock Board
        Board board = new Board();
        Figure figure = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return getVertAndHorDirections();
            }
        };
        board.moveFigure(figure, new Field(6, 2));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - o - - - - -
        1 - - o - - - - -
        2 - - o - - - - -
   row  3 - - o - - - - -
        4 - - o - - - - -
        5 - - o - - - - -
        6 o o X o o o o o
        7 - - o - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            // (0, -1)
            add(new Field(6, 1));
            add(new Field(6, 0));
            // (-1, 0)
            add(new Field(5, 2));
            add(new Field(4, 2));
            add(new Field(3, 2));
            add(new Field(2, 2));
            add(new Field(1, 2));
            add(new Field(0, 2));
            // (0, 1)
            add(new Field(6, 3));
            add(new Field(6, 4));
            add(new Field(6, 5));
            add(new Field(6, 6));
            add(new Field(6, 7));
            // (1, 0)
            add(new Field(7, 2));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetDiagonalDirections_BlockingFigures() {
        // FIXME: mock Board
        Board board = new Board();
        Figure blockingMy1 = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy1, new Field(1, 2));

        Figure blockingOpp1 = new Figure(MyColor.BLACK, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(1, 4));

        Figure blockingMy2 = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy2, new Field(6, 7));

        Figure blockingOpp2 = new Figure(MyColor.BLACK, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(4, 1));

        Figure figure = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return getDiagonalDirections();
            }
        };
        board.moveFigure(figure, new Field(2, 3));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - m - p - - -
        2 - - - X - - - -
   row  3 - - o - o - - -
        4 - p - - - o - -
        5 - - - - - - o -
        6 - - - - - - - m
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            // (-1, -1)
            // (-1, 1)
            add(new Field(1, 4)); // can be captured
            // (1, -1)
            add(new Field(3, 2));
            add(new Field(4, 1)); // can be captured
            // (1, 1)
            add(new Field(3, 4));
            add(new Field(4, 5));
            add(new Field(5, 6));
        }};
        assertEquals(figure.getValidMoves(), expected);
    }

    @Test
    public void testGetVertAndHorDirections_BlockingFigures() {
        // FIXME: mock Board
        Board board = new Board();
        Figure blockingMy1 = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy1, new Field(2, 2));

        Figure blockingOpp1 = new Figure(MyColor.BLACK, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp1, new Field(1, 3));

        Figure blockingMy2 = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingMy2, new Field(2, 7));

        Figure blockingOpp2 = new Figure(MyColor.BLACK, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return null;
            }
        };
        board.moveFigure(blockingOpp2, new Field(4, 3));

        Figure figure = new Figure(MyColor.WHITE, board) {
            @Override
            public Figure clone(Board dstBoard) {
                return null;
            }

            @Override
            public Set<Field> getValidMoves() {
                return getVertAndHorDirections();
            }
        };
        board.moveFigure(figure, new Field(2, 3));

        /*
                col
          0 1 2 3 4 5 6 7
        0 - - - - - - - -
        1 - - - p - - - -
        2 - - m X o o o m
   row  3 - - - o - - - -
        4 - - - p - - - -
        5 - - - - - - - -
        6 - - - - - - - -
        7 - - - - - - - -
         */
        Set<Field> expected = new HashSet<>() {{
            // (0, -1)
            // (-1, 0)
            add(new Field(1, 3)); // can be captured
            // (0, 1)
            add(new Field(2, 4));
            add(new Field(2, 5));
            add(new Field(2, 6));
            // (1, 0)
            add(new Field(3, 3));
            add(new Field(4, 3)); // can be captured
        }};
        assertEquals(figure.getValidMoves(), expected);
    }
}
