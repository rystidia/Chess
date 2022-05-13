package cz.cvut.fel.pjv.chess;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class SANTest {
    @Test
    public void testPromotion() throws PGN.ParseException {
        SAN san = new SAN("fxg1=Q+");
        assertNull(san.getQueenSideCastling());
        assertEquals(san.getFigureType(), 'P');
        assertNull(san.getOrigRow());
        assertEquals(san.getOrigColumn(), 'f' - 'a');
        assertEquals(san.getDestPos(), Field.fromAlgebraicNotation("g1"));
        assertEquals(san.getPromotionFigure(), 'Q');
    }

    @Test
    public void testKingSideCastling() throws PGN.ParseException {
        SAN san = new SAN("O-O");
        assertFalse(san.getQueenSideCastling());
        assertNull(san.getFigureType());
        assertNull(san.getOrigRow());
        assertNull(san.getOrigColumn());
        assertNull(san.getDestPos());
        assertNull(san.getPromotionFigure());
    }

    @Test
    public void testQueenSideCastling() throws PGN.ParseException {
        SAN san = new SAN("O-O-O");
        assertTrue(san.getQueenSideCastling());
        assertNull(san.getFigureType());
        assertNull(san.getOrigRow());
        assertNull(san.getOrigColumn());
        assertNull(san.getDestPos());
        assertNull(san.getPromotionFigure());
    }

    @Test
    public void testPawnMove() throws PGN.ParseException {
        SAN san = new SAN("d4");
        assertNull(san.getQueenSideCastling());
        assertEquals(san.getFigureType(), 'P');
        assertNull(san.getOrigRow());
        assertNull(san.getOrigColumn());
        assertEquals(san.getDestPos(), Field.fromAlgebraicNotation("d4"));
        assertNull(san.getPromotionFigure());
    }

    @Test
    public void testPawnCapture() throws PGN.ParseException {
        SAN san = new SAN("cxb4");
        assertNull(san.getQueenSideCastling());
        assertEquals(san.getFigureType(), 'P');
        assertNull(san.getOrigRow());
        assertEquals(san.getOrigColumn(), 'c' - 'a');
        assertEquals(san.getDestPos(), Field.fromAlgebraicNotation("b4"));
        assertNull(san.getPromotionFigure());
    }

    @Test
    public void testStandardMove() throws PGN.ParseException {
        SAN san = new SAN("Qa6xb7#");
        assertNull(san.getQueenSideCastling());
        assertEquals(san.getFigureType(), 'Q');
        assertEquals(new Field(san.getOrigRow(), san.getOrigColumn()), Field.fromAlgebraicNotation("a6"));
        assertEquals(san.getDestPos(), Field.fromAlgebraicNotation("b7"));
        assertNull(san.getPromotionFigure());
    }
}
