package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SAN {
    // Originally from https://metacpan.org/release/OLOF/Regexp-Common-Chess-0.1/source/lib/Regexp/Common/Chess.pm, adapted
    private static final String check = "[+#]";
    private static final String rank = "[1-8]";
    private static final String file = "[a-h]";
    private static final String piece = "[NBRQK]";
    private static final String promSuffix = "(?:=(?<promotionPiece>(?!K)" + piece + "))?";
    private static final String stdMove = "(?<stdPiece>" + piece + "?)(?<stdOrigFile>" + file + "?)(?<stdOrigRank>" + rank + "?)(?<stdCapture>x?)(?<stdDest>" + file + rank + ")" + promSuffix;
    private static final String castling = "(?<castling>O-O(?:-O)?)";

    private static final String regex = "^(?:" + castling + "|" + stdMove + ")" + check + "?$";
    private static final Pattern pattern = Pattern.compile(regex);

    private Boolean queenSideCastling;

    private Class<? extends Figure> figureType;

    private Integer origRow;
    private Integer origColumn;

    private Field destPos;

    private Class<? extends Figure> promotionFigure;

    /**
     * Parses the given SAN move.
     */
    public SAN(String move) throws PGN.ParseException {
        Matcher m = pattern.matcher(move);
        if (!m.matches()) {
            throw new PGN.ParseException("invalid SAN move '" + move + "'");
        }
        if (m.group("castling") != null) {
            queenSideCastling = Objects.equals(m.group("castling"), "O-O-O");
        } else if (m.group("stdDest") != null) {
            figureType = Figure.getFigureClassByCharacter(m.group("stdPiece").equals("") ? 'P' : m.group("stdPiece").charAt(0));
            if (!m.group("stdOrigRank").equals("")) {
                origRow = Field.fromAlgebraicNotation("a" + m.group("stdOrigRank")).row;
            }
            if (!m.group("stdOrigFile").equals("")) {
                origColumn = Field.fromAlgebraicNotation(m.group("stdOrigFile") + "1").column;
            }
            destPos = Field.fromAlgebraicNotation(m.group("stdDest"));
            // FIXME: maybe process "stdCapture"
            if (m.group("promotionPiece") != null) {
                promotionFigure = Figure.getFigureClassByCharacter(m.group("promotionPiece").charAt(0));
            }
        }
    }

    public Boolean getQueenSideCastling() {
        return queenSideCastling;
    }

    public Class<? extends Figure> getFigureType() {
        return figureType;
    }

    public Integer getOrigRow() {
        return origRow;
    }

    public Integer getOrigColumn() {
        return origColumn;
    }

    public Field getDestPos() {
        return destPos;
    }

    public Class<? extends Figure> getPromotionFigure() {
        return promotionFigure;
    }
}
