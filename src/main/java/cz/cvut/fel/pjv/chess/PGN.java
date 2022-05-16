package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.King;
import cz.cvut.fel.pjv.chess.figures.Pawn;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.*;

public class PGN {
    protected final Map<String, String> tags = new HashMap<>();

    private MyColor winnerColor;

    public Board load(Reader reader) throws IOException, ParseException {
        CharReader r = new CharReader(reader);
        tags.clear();
        {
            boolean isPresent;
            do {
                isPresent = parseTag(r);
            } while (isPresent);
        }

        Board board;

        if (Objects.equals(tags.get("SetUp"), "1")) {
            String fen = tags.get("FEN");
            if (fen == null) {
                throw new ParseException("the value of SetUp tag is \"1\", but no FEN tag present");
            }
            board = Board.fromFEN(fen);
        } else {
            board = new Board();
            board.initialPosition();
        }
        board.switchToGameMode();

        MyColor curColor = MyColor.WHITE;
        String terminationMarker;

        while (true) {
            skipComments(r);
            if (r.read() == '*') {
                terminationMarker = "*";
                break;
            } else {
                r.goBackOne();
            }
            String symbolToken = parseSymbolToken(r);
            if (Objects.equals(symbolToken, "1-0") || Objects.equals(symbolToken, "0-1") || Objects.equals(symbolToken, "1/2-1/2")) {
                terminationMarker = symbolToken;
                break;
            }
            Integer moveNumber = null;
            try {
                moveNumber = Integer.parseInt(symbolToken);
            } catch (NumberFormatException ignored) {
            }
            if (moveNumber != null) {
                skipWhitespace(r);
                skipChars(r, ".");
                skipWhitespace(r);
                symbolToken = parseSymbolToken(r);
            }
            skipWhitespace(r);
            SAN sanMove = new SAN(symbolToken);
            if (sanMove.getQueenSideCastling() != null) {
                King king = board.getKing(curColor);
                Field toPos = king.getPosition().plus(0, sanMove.getQueenSideCastling() ? -2 : 2);
                if (!king.getValidMoves().contains(toPos)) {
                    throw new ParseException((sanMove.getQueenSideCastling() ? "queen" : "king") + " side castling is not a valid move at this point");
                }
                board.moveFigure(king, toPos);
            } else {
                Set<Figure> candidateFigs = board.getFiguresByTypeAndColor(sanMove.getFigureType(), curColor);
                Set<Figure> possibleFigs = new HashSet<>();
                for (Figure fig : candidateFigs) {
                    if (
                        fig.getValidMoves().contains(sanMove.getDestPos())
                            && (sanMove.getOrigRow() == null || fig.getPosition().row == sanMove.getOrigRow())
                            && (sanMove.getOrigColumn() == null || fig.getPosition().column == sanMove.getOrigColumn())
                    ) {
                        possibleFigs.add(fig);
                    }
                }
                if (possibleFigs.size() != 1) {
                    throw new ParseException("expected SAN move identifier '" + symbolToken + "' to match exactly 1 figure, but " + possibleFigs.size() + " were matched");
                }
                Figure fig = possibleFigs.iterator().next();
                board.moveFigure(fig, sanMove.getDestPos());
                if (sanMove.getPromotionFigure() != null) {
                    ((Pawn) fig).promotion(sanMove.getPromotionFigure());
                }
            }
            curColor = MyColor.getOppositeColor(curColor);
        }
        if (tags.containsKey("Result") && !terminationMarker.equals(tags.get("Result"))) {
            throw new ParseException("game termination marker is '" + terminationMarker + "', " +
                "but the Result tag value is '" + tags.get("Result") + "'");
        }
        switch (terminationMarker) {
            case "1-0":
                winnerColor = MyColor.WHITE;
                break;
            case "0-1":
                winnerColor = MyColor.BLACK;
                break;
            case "1/2-1/2":
                winnerColor = null;
                break;
        }
        return board;
    }

    public void save(PrintWriter writer, Board board) {
        Board initialBoard = board.getInitialBoard() != null ? board.getInitialBoard() : board;
        String fen = initialBoard.toFEN();
        if (Objects.equals(fen, "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")) {
            tags.remove("SetUp");
            tags.remove("FEN");
        } else {
            tags.put("SetUp", "1");
            tags.put("FEN", fen);
        }
        if (!tags.containsKey("Result")) {
            tags.put("Result", "*");
        }

        final List<String> sevenTags = Arrays.asList("Event", "Site", "Date", "Round", "White", "Black", "Result");
        for (String key : sevenTags) {
            if (!tags.containsKey(key)) continue;
            writeTag(writer, key);
        }
        List<String> restOfTags = new ArrayList<>(tags.keySet());
        restOfTags.removeAll(sevenTags);
        restOfTags.sort(String::compareTo);
        for (String key : restOfTags) {
            writeTag(writer, key);
        }
        writer.print('\n'); // intentionally use line feed (LF, 0x0a) per the PGN specification
        List<String> tokens = new ArrayList<>();
        Board simBoard = new Board(initialBoard);
        simBoard.switchToGameMode();
        int halfMoveNum = 0;
        for (Move move : board.getHistory()) {
            if (halfMoveNum % 2 == 0) {
                tokens.add(((halfMoveNum / 2) + 1) + ".");
            }
            String moveStr = "";
            Figure fig = simBoard.getFigure(move.getFrom());
            if (fig instanceof King && Math.abs(move.getTo().column - move.getFrom().column) > 1) {
                moveStr = "O-O" + (move.getTo().column - move.getFrom().column < 0 ? "-O" : "");
            } else {
                if (!(fig instanceof Pawn)) {
                    moveStr += Figure.getCharacterByFigureClass(fig.getClass());
                }
                Set<Figure> possibleFigs = new HashSet<>();
                {
                    Set<Figure> candidateFigs = simBoard.getFiguresByTypeAndColor(fig.getClass(), fig.getColor());
                    for (Figure candFig : candidateFigs) {
                        if (candFig.getValidMoves().contains(move.getTo())) {
                            possibleFigs.add(candFig);
                        }
                    }
                }
                boolean ambiguous = (possibleFigs.size() != 1);
                if (ambiguous || (fig instanceof Pawn && (simBoard.getFigure(move.getTo()) != null))) {
                    Set<Figure> fileDisambig = new HashSet<>(possibleFigs);
                    fileDisambig.removeIf(f -> f.getPosition().column != fig.getPosition().column);
                    if (fileDisambig.size() == 1) {
                        moveStr += fig.getPosition().toAlgebraicNotation().charAt(0);
                        ambiguous = false;
                    }
                }
                if (ambiguous) {
                    Set<Figure> rankDisambig = new HashSet<>(possibleFigs);
                    rankDisambig.removeIf(f -> f.getPosition().row != fig.getPosition().row);
                    if (rankDisambig.size() == 1) {
                        moveStr += fig.getPosition().toAlgebraicNotation().charAt(1);
                        ambiguous = false;
                    }
                }
                if (ambiguous) {
                    moveStr += fig.getPosition().toAlgebraicNotation();
                }
                if (simBoard.getFigure(move.getTo()) != null) {
                    moveStr += 'x';
                }
                moveStr += move.getTo().toAlgebraicNotation();
                if (move.getPromotionFigure() != null) {
                    moveStr += "=" + Figure.getCharacterByFigureClass(move.getPromotionFigure());
                }
            }
            simBoard.moveFigure(fig, move.getTo());
            if (move.getPromotionFigure() != null) {
                ((Pawn) fig).promotion(move.getPromotionFigure());
            }
            if (simBoard.getKing(MyColor.getOppositeColor(fig.getColor())).isInCheck()) {
                // FIXME: '#' for checkmate
                moveStr += '+';
            }
            tokens.add(moveStr);
            halfMoveNum++;
        }
        tokens.add(tags.get("Result"));
        StringBuilder line = new StringBuilder();
        for (String token : tokens) {
            if (line.length() + (line.isEmpty() ? 0 : 1) + token.length() >= 80) {
                writer.print(line);
                writer.print('\n');
                line.setLength(0);
            }
            if (!line.isEmpty()) line.append(' ');
            line.append(token);
        }
        writer.print(line);
        writer.print('\n');
    }

    protected boolean parseTag(CharReader r) throws IOException, ParseException {
        if (r.read() != '[') {
            r.goBackOne();
            return false;
        }
        skipWhitespace(r);
        String name = parseSymbolToken(r);
        skipWhitespace(r);
        String value = parseStringToken(r);
        skipWhitespace(r);
        expectChar(r, ']', "right bracket token ] of a tag pair");
        tags.put(name, value);
        skipWhitespace(r);
        return true;
    }

    protected void writeTag(PrintWriter w, String key) {
        w.format("[%s \"%s\"]\n", key, tags.get(key).replaceAll("([\"\\\\])", "\\\\$1"));
    }

    protected static String parseStringToken(Reader r) throws IOException, ParseException {
        expectChar(r, '"', "opening quote \" of a string token");
        StringBuilder s = new StringBuilder();
        boolean backslash = false;
        for (int ch; (ch = r.read()) != -1;) {
            if (backslash) {
                if (ch == '"' || ch == '\\') {
                    s.append((char) ch);
                } else {
                    throw new ParseException("expected \" or \\ after backslash, but got '" + (char)ch + "'");
                }
                backslash = false;
            } else {
                if (ch == '\\') {
                    backslash = true;
                } else if (ch == '"') {
                    return s.toString();
                } else {
                    s.append((char) ch);
                }
            }
        }
        throw new ParseException("expected closing quote \" of a string token, but reached end of file");
    }

    protected static String parseSymbolToken(CharReader r) throws IOException, ParseException {
        StringBuilder s = new StringBuilder();
        {
            int ch = r.read();
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                s.append((char) ch);
            } else {
                throw new ParseException("expected the symbol token to start with a letter or digit, but got '" + (char)ch + "'");
            }
        }
        for (int ch; (ch = r.read()) != -1;) {
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')
                || "_+#=:-/".indexOf(ch) != -1) {
                s.append((char) ch);
            } else {
                r.goBackOne();
                break;
            }
        }
        return s.toString();
    }

    protected static void expectChar(Reader r, char expected, String charDesc) throws IOException, ParseException {
        int ch;
        if ((ch = r.read()) != expected) {
            final String msgDetail = (ch == -1) ? "reached end of file" : "got '" + (char)ch + "'";
            throw new ParseException("expected " + charDesc + ", but " + msgDetail);
        }
    }


    protected static void skipChars(CharReader r, String chars) throws IOException {
        for (int ch; (ch = r.read()) != -1;) {
            if (chars.indexOf(ch) == -1) {
                r.goBackOne();
                break;
            }
        }
    }

    protected static void skipWhitespace(CharReader r) throws IOException {
        skipChars(r, " \r\n\t");
    }

    protected static void skipComments(CharReader r) throws IOException, ParseException {
        while (true) {
            int ch = r.read();
            if (ch == '{') {
                skipBraceComment(r);
            } else if (ch == ';') {
                skipRestOfLineComment(r);
            } else {
                r.goBackOne();
                skipWhitespace(r);
                break;
            }
            skipWhitespace(r);
        }
    }

    protected static void skipBraceComment(CharReader r) throws IOException, ParseException {
        for (int ch; (ch = r.read()) != -1;) {
            if (ch == '}') {
                return;
            }
        }
        throw new ParseException("expected right brace } to end the comment, but reached end of file");
    }

    protected static void skipRestOfLineComment(CharReader r) throws IOException {
        boolean skipLF = false;
        for (int ch; (ch = r.read()) != -1;) {
            if (ch == '\n') {
                break;
            } else if (skipLF) {
                r.goBackOne();
                break;
            }
            skipLF = (ch == '\r');
        }
    }

    public String getTagValue(String key) {
        return tags.get(key);
    }

    public void setTagValue(String key, String value) {
        tags.put(key, value);
    }

    public MyColor getWinnerColor() {
        return winnerColor;
    }

    public static class CharReader extends Reader {
        private final Reader reader;

        private int prevCh;

        private boolean usePrevCh;

        public CharReader(Reader reader) {
            this.reader = reader;
        }

        @Override
        public int read() throws IOException {
            if (usePrevCh) {
                usePrevCh = false;
            } else {
                prevCh = reader.read();
            }
            return prevCh;
        }

        public void goBackOne() {
            usePrevCh = true;
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            return reader.read(cbuf, off, len);
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }

}
