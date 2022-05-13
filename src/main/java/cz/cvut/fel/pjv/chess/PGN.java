package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.King;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.*;

public class PGN {
    protected final Map<String, String> tags = new HashMap<>();

    private Board board;

    private MyColor winnerColor;

    public void load(Reader reader) throws IOException, ParseException {
        CharReader r = new CharReader(reader);
        {
            boolean isPresent;
            do {
                isPresent = parseTag(r);
            } while (isPresent);
        }

        board = new Board();
        board.initialPosition();

        MyColor curColor = MyColor.WHITE;
        String terminationMarker;

        while (true) {
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
                Set<Figure> candidateFigs = board.getFiguresByColorAndType(curColor, sanMove.getFigureType());
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
                board.moveFigure(possibleFigs.iterator().next(), sanMove.getDestPos());
            }
            curColor = MyColor.getOppositeColor(curColor);
        }
        if (terminationMarker != null) {
            if (tags.containsKey("Result") && !terminationMarker.equals(tags.get("Result"))) {
                throw new ParseException("game termination marker is '" + terminationMarker + "', " +
                    "but the Result tag value is '" + tags.get("Result") + "'");
            }
        }
    }

    public static void save(Writer writer) {
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

    public String getTagValue(String key) {
        return tags.get(key);
    }

    public Board getBoard() {
        return board;
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
