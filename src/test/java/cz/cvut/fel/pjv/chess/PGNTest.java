package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class PGNTest {
    @Test
    public void testLoadSpec() throws IOException, PGN.ParseException {
        Reader rdr = new InputStreamReader(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("spec.pgn")));
        PGN pgn = new PGN();
        pgn.load(rdr);
        assertEquals(pgn.getTagValue("Event"), "F/S Return Match");
        assertEquals(pgn.getTagValue("Site"), "Belgrade, Serbia JUG");
        assertEquals(pgn.getTagValue("Date"), "1992.11.04");
        assertEquals(pgn.getTagValue("Round"), "29");
        assertEquals(pgn.getTagValue("White"), "Fischer, Robert J.");
        assertEquals(pgn.getTagValue("Black"), "Spassky, Boris V.");
        assertEquals(pgn.getTagValue("Result"), "1/2-1/2");

        // According to https://chesstempo.com/pgn-viewer/
        char[][] expected = {
            {'-','-','-','-','-','-','-','-'},
            {'-','-','-','-','-','-','-','-'},
            {'-','-','-','-','R','-','p','-'},
            {'-','-','k','-','-','-','p','-'},
            {'-','p','-','-','-','-','P','-'},
            {'-','P','-','b','-','P','-','-'},
            {'-','-','-','K','-','n','-','-'},
            {'-','-','-','-','-','-','-','-'},
        };

        Board board = pgn.getBoard();
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            for (int c = 0; c <= Board.MAX_COL; c++) {
                char ch = expected[r][c];
                Figure fig = board.getFigure(new Field(r, c));
                if (ch == '-') {
                    assertNull(fig);
                } else {
                    assertEquals(Figure.getCharacterByFigureClass(fig.getClass()), Character.toUpperCase(ch));
                    MyColor expectedColor = Character.isLowerCase(ch) ? MyColor.BLACK : MyColor.WHITE;
                    assertEquals(fig.getColor(), expectedColor);
                }
            }
        }
    }

}
