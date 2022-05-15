package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.Pawn;
import org.testng.annotations.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class PGNTest {
    @Test
    public void testLoadSpec() throws IOException, PGN.ParseException {
        Reader rdr = new InputStreamReader(getSpecResourceStream());
        PGN pgn = new PGN();
        Board board = pgn.load(rdr);
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

        assertBoard(board, expected);
        assertNull(pgn.getWinnerColor());
    }

    private static void assertBoard(Board board, char[][] expected) {
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

    @Test
    public void testLoadSetUp() throws IOException, PGN.ParseException {
        Reader rdr = new InputStreamReader(getSetUpResourceStream());
        PGN pgn = new PGN();
        Board board = pgn.load(rdr);
        assertEquals(pgn.getTagValue("Event"), "ICC 90 0");
        assertEquals(pgn.getTagValue("Site"), "Internet Chess Club");
        assertEquals(pgn.getTagValue("Date"), "2003.02.25");
        assertEquals(pgn.getTagValue("White"), "ERIQ");
        assertEquals(pgn.getTagValue("Black"), "DaveTheRook");
        assertEquals(pgn.getTagValue("Result"), "0-1");
        assertEquals(pgn.getTagValue("WhiteElo"), "2069");
        assertEquals(pgn.getTagValue("BlackElo"), "1606");
        assertEquals(pgn.getTagValue("ECO"), "C77");
//        assertEquals(pgn.getTagValue("SetUp"), "1");
//        assertEquals(pgn.getTagValue("FEN"), "r5k1/1b1p1ppp/p7/1p1Q4/2p1r3/PP4Pq/BBP2b1P/R4R1K w - - 0 20");

        // According to http://www.ee.unb.ca/cgi-bin/tervo/fen.pl
        char[][] initialBoardExpected = {
            {'r','-','-','-','-','-','k','-'},
            {'-','b','-','p','-','p','p','p'},
            {'p','-','-','-','-','-','-','-'},
            {'-','p','-','Q','-','-','-','-'},
            {'-','-','p','-','r','-','-','-'},
            {'P','P','-','-','-','-','P','q'},
            {'B','B','P','-','-','b','-','P'},
            {'R','-','-','-','-','R','-','K'},
        };
        assertBoard(board.getInitialBoard(), initialBoardExpected);

        // According to https://chesstempo.com/pgn-viewer/
        char[][] expected = {
            {'-','-','-','-','-','-','-','-'},
            {'-','-','Q','-','-','-','p','p'},
            {'p','-','-','-','r','p','k','-'},
            {'-','p','-','-','-','-','-','-'},
            {'-','-','p','-','-','-','P','-'},
            {'P','P','-','-','-','q','-','-'},
            {'B','B','P','-','r','-','-','P'},
            {'R','-','-','-','b','-','R','K'},
        };
        assertBoard(board, expected);
        assertEquals(pgn.getWinnerColor(), MyColor.BLACK);
    }

    @Test
    public void testLoadPromotion() throws IOException, PGN.ParseException {
        Reader rdr = new InputStreamReader(getPromotionResourceStream());
        PGN pgn = new PGN();
        Board board = pgn.load(rdr);

        char[][] expected = {
            {'r','r','-','-','N','-','-','-'},
            {'-','-','-','n','-','-','k','p'},
            {'p','-','-','-','-','-','p','-'},
            {'-','p','-','q','-','-','-','-'},
            {'P','-','p','-','N','-','-','-'},
            {'-','-','-','-','Q','N','-','P'},
            {'-','P','-','-','-','-','-','b'},
            {'-','-','-','-','R','R','-','K'},
        };
        assertBoard(board, expected);
    }

    private InputStream getSpecResourceStream() {
        return Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("spec.pgn"));
    }

    private InputStream getSetUpResourceStream() {
        return Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("set-up.pgn"));
    }

    private InputStream getPromotionResourceStream() {
        return Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("promotion.pgn"));
    }

    @Test
    public void testSaveSpec() throws IOException {
        PGN pgn = new PGN();
        pgn.setTagValue("White", "Fischer, Robert J."); // 5.
        pgn.setTagValue("Result", "1/2-1/2"); // 7.
        pgn.setTagValue("Date", "1992.11.04"); // 3.
        pgn.setTagValue("Site", "Belgrade, Serbia JUG"); // 2.
        pgn.setTagValue("Round", "29"); // 4.
        pgn.setTagValue("Event", "F/S Return Match"); // 1.
        pgn.setTagValue("Black", "Spassky, Boris V."); // 6.

        // According to https://chesstempo.com/pgn-viewer/
        String[][] moves = {
            {"e2", "e4"},     {"e7", "e5"},      // 1.
            {"g1", "Nf3"},    {"b8", "Nc6"},     // 2.
            {"f1", "Bb5"},    {"a7", "a6"},      // 3.
            {"b5", "Ba4"},    {"g8", "Nf6"},     // 4.
            {"e1", "g1"/*"O-O"*/},    {"f8", "Be7"},     // 5.
            {"f1", "Re1"},    {"b7", "b5"},      // 6.
            {"a4", "Bb3"},    {"d7", "d6"},      // 7.
            {"c2", "c3"},     {"e8", "g8"/*"O-O"*/},     // 8.
            {"h2", "h3"},     {"c6", "Nb8"},     // 9.
            {"d2", "d4"},     {"b8", "Nbd7"},    // 10.
            {"c3", "c4"},     {"c7", "c6"},      // 11.
            {"c4", "cxb5"},   {"a6", "axb5"},    // 12.
            {"b1", "Nc3"},    {"c8", "Bb7"},     // 13.
            {"c1", "Bg5"},    {"b5", "b4"},      // 14.
            {"c3", "Nb1"},    {"h7", "h6"},      // 15.
            {"g5", "Bh4"},    {"c6", "c5"},      // 16.
            {"d4", "dxe5"},   {"f6", "Nxe4"},    // 17.
            {"h4", "Bxe7"},   {"d8", "Qxe7"},    // 18.
            {"e5", "exd6"},   {"e7", "Qf6"},     // 19.
            {"b1", "Nbd2"},   {"e4", "Nxd6"},    // 20.
            {"d2", "Nc4"},    {"d6", "Nxc4"},    // 21.
            {"b3", "Bxc4"},   {"d7", "Nb6"},     // 22.
            {"f3", "Ne5"},    {"a8", "Rae8"},    // 23.
            {"c4", "Bxf7"/*+*/},  {"f8", "Rxf7"},     // 24.
            {"e5", "Nxf7"},   {"e8", "Rxe1"/*+*/},    // 25.
            {"d1", "Qxe1"},   {"g8", "Kxf7"},    // 26.
            {"e1", "Qe3"},    {"f6", "Qg5"},     // 27.
            {"e3", "Qxg5"},   {"h6", "hxg5"},    // 28.
            {"b2", "b3"},     {"f7", "Ke6"},     // 29.
            {"a2", "a3"},     {"e6", "Kd6"},     // 30.
            {"a3", "axb4"},   {"c5", "cxb4"},    // 31.
            {"a1", "Ra5"},    {"b6", "Nd5"},     // 32.
            {"f2", "f3"},     {"b7", "Bc8"},     // 33.
            {"g1", "Kf2"},    {"c8", "Bf5"},     // 34.
            {"a5", "Ra7"},    {"g7", "g6"},      // 35.
            {"a7", "Ra6"/*+*/},   {"d6", "Kc5"},      // 36.
            {"f2", "Ke1"},    {"d5", "Nf4"},     // 37.
            {"g2", "g3"},     {"f4", "Nxh3"},    // 38.
            {"e1", "Kd2"},    {"c5", "Kb5"},     // 39.
            {"a6", "Rd6"},    {"b5", "Kc5"},     // 40.
            {"d6", "Ra6"},    {"h3", "Nf2"},     // 41.
            {"g3", "g4"},     {"f5", "Bd3"},     // 42.
            {"a6", "Re6"}, // 1/2-1/2        // 43.
        };
        Board board = new Board();
        board.initialPosition();
        board.switchToGameMode();

        for (String[] fullMove : moves) {
            Field from = Field.fromAlgebraicNotation(fullMove[0]);
            Field to = Field.fromAlgebraicNotation(fullMove[1].substring(fullMove[1].length() - 2));
            Figure fig = board.getFigure(from);
            board.moveFigure(fig, to);
        }

        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        pgn.save(pw, board);
        String actual = out.toString();
        String expected =
            new BufferedReader(new InputStreamReader(getSpecResourceStream(), StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n")) + '\n';
        assertEquals(actual, expected);
    }


    @Test
    public void testSaveSetUp() throws IOException {
        PGN pgn = new PGN();
        pgn.setTagValue("Event", "ICC 90 0");
        pgn.setTagValue("Site", "Internet Chess Club");
        pgn.setTagValue("Date", "2003.02.25");
        pgn.setTagValue("White", "ERIQ");
        pgn.setTagValue("Black", "DaveTheRook");
        pgn.setTagValue("Result", "0-1");
        pgn.setTagValue("WhiteElo", "2069");
        pgn.setTagValue("BlackElo", "1606");
        pgn.setTagValue("ECO", "C77");

        // According to https://chesstempo.com/pgn-viewer/
        String[][] moves = {
            {"d5", "Qxb7"},       {"a8", "Rae8"},      // 1.
            {"b7", "Qd5"},        {"e8", "R8e6"},      // 2.
            {"d5", "Qxd7"},       {"f7", "f6"},        // 3.
            {"d7", "Qc8"/*+*/},   {"g8", "Kf7"},       // 4.
            {"c8", "Qd7"/*+*/},   {"f7", "Kg6"},       // 5.
            {"d7", "Qc7"},        {"f2", "Be1"},       // 6.
            {"f1", "Rg1"},        {"e4", "Re2"},       // 7.
            {"g3", "g4"},         {"h3", "Qf3"/*+*/},  // 8.
            // 0-1
        };
        Board board = Board.fromFEN("r5k1/1b1p1ppp/p7/1p1Q4/2p1r3/PP4Pq/BBP2b1P/R4R1K w - - 0 1");
        board.switchToGameMode();

        for (String[] fullMove : moves) {
            Field from = Field.fromAlgebraicNotation(fullMove[0]);
            Field to = Field.fromAlgebraicNotation(fullMove[1].substring(fullMove[1].length() - 2));
            Figure fig = board.getFigure(from);
            board.moveFigure(fig, to);
        }

        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        pgn.save(pw, board);
        String actual = out.toString();
        String expected =
            new BufferedReader(new InputStreamReader(getSetUpResourceStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n")) + '\n';
        assertEquals(actual, expected);
    }

    @Test
    public void testSavePromotion() {
        PGN pgn = new PGN();
        pgn.setTagValue("Event", "URS-FL");
        pgn.setTagValue("Site", "Lvov");
        pgn.setTagValue("Date", "1987.??.??");
        pgn.setTagValue("Round", "?");
        pgn.setTagValue("White", "Novikov, Igor A");
        pgn.setTagValue("Black", "Dreev, Alexey");
//        pgn.setTagValue("Result", "1-0");
        pgn.setTagValue("WhiteElo", "2455");
        pgn.setTagValue("BlackElo", "2475");
        pgn.setTagValue("ECO", "E12");

        // According to https://chesstempo.com/pgn-viewer/
        String[][] moves = {
            {"c3", "Ne4"},         {"d6", "Qxd5"},      // 1.
            {"f7", "fxe8=N"/*+*/},
//            {"b8", "Rxe8"},      // 2.
//            {"h1", "Kxh2"},        {"d7", "Ne5"},       // 3.
//            {"e1", "Re2"},         {"d5", "Qd3"},       // 4.
//            {"f3", "Nfd2"},        {"d3", "Qxe3"},      // 5.
//            {"e2", "Rxe3"},        {"a8", "Rad8"},      // 6.
//            {"a4", "axb5"},        {"a6", "axb5"},      // 7.
//            {"e3", "Ra3"},         {"e8", "Re7"},       // 8.
//            {"a3", "Ra5"},         {"b5", "b4"},        // 9.
//            {"f1", "Rc1"},         {"d8", "Rd4"},       // 10.
//            {"a5", "Rc5"},         {"d4", "Rxd2"/*+*/}, // 11.
//            {"e4", "Nxd2"},        {"e5", "Nd3"},       // 12.
//            {"c1", "R1xc4"},       {"d3", "Nxc5"},      // 13.
//            {"h2", "Kg3"} // 14.
            // 1-0
        };
        Board board = Board.fromFEN("rr2n3/3n1Pkp/p2q2p1/1p1P4/P1p5/2N1QN1P/1P5b/4RR1K w - - 0 1");
        board.switchToGameMode();

        for (String[] fullMove : moves) {
            Field from = Field.fromAlgebraicNotation(fullMove[0]);
            String toStr = fullMove[1];
            Class<? extends Figure> promoteToFigClass = null;
            if (toStr.charAt(toStr.length() - 2) == '=') {
                promoteToFigClass = Figure.getFigureClassByCharacter(toStr.charAt(toStr.length() - 1));
                toStr = toStr.substring(0, toStr.length() - 2);
            }
            Field to = Field.fromAlgebraicNotation(toStr.substring(toStr.length() - 2));
            Figure fig = board.getFigure(from);
            board.moveFigure(fig, to);
            if (promoteToFigClass != null) {
                ((Pawn) fig).promotion(promoteToFigClass);
            }
        }

        StringWriter out = new StringWriter();
        PrintWriter pw = new PrintWriter(out);
        pgn.save(pw, board);
        String actual = out.toString();
        String expected =
            new BufferedReader(new InputStreamReader(getPromotionResourceStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n")) + '\n';
        assertEquals(actual, expected);
    }
}
