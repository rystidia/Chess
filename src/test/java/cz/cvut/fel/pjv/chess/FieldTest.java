package cz.cvut.fel.pjv.chess;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class FieldTest {
    @Test(dataProvider = "algebraic-notation-valid")
    public void testToAlgebraicNotation(Field pos, String algNotation) {
        assertEquals(pos.toAlgebraicNotation(), algNotation);
    }

    @Test(dataProvider = "algebraic-notation-valid-and-invalid")
    public void testFromAlgebraicNotation(Field pos, String algNotation) {
        if (pos == null) {
            assertThrows(IllegalArgumentException.class, () -> Field.fromAlgebraicNotation(algNotation));
        } else {
            assertEquals(Field.fromAlgebraicNotation(algNotation), pos);
        }
    }

    @DataProvider(name = "algebraic-notation-valid")
    public Object[][] algNotationValidDataProvider() {
        // See http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#AEN229
        return new Object[][] {
            {new Field(7, 0), "a1"}, // white queen rook
            {new Field(7, 4), "e1"}, // white king
            {new Field(1, 1), "b7"}, // black queen knight pawn
            {new Field(0, 7), "h8"}, // black king rook
        };
    }

    @DataProvider(name = "algebraic-notation-valid-and-invalid")
    public Object[][] algNotationValidAndInvalidDataProvider() {
        Object[][] valid = algNotationValidDataProvider();
        Object[][] invalid = new Object[][] {
            {null, "h"},
            {null, "h23"},
            {null, "1a"},
            {null, "A1"},
            {null, "a0"},
            {null, "a9"},
            {null, "i1"},
        };
        // from https://www.programiz.com/java-programming/examples/concatenate-two-arrays
        Object[][] all = new Object[valid.length + invalid.length][];
        System.arraycopy(valid, 0, all, 0, valid.length);
        System.arraycopy(invalid, 0, all, valid.length, invalid.length);
        return all;
    }
}
