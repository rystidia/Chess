package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.Figure;

public class Move {
    private final Field from;
    private final Field to;
    private final Class<? extends Figure> promotionFigure;

    public Move(Field from, Field to) {
        this(from, to, null);
    }

    public Move(Field from, Field to, Class<? extends Figure> promotionFigure) {
        this.from = from;
        this.to = to;
        this.promotionFigure = promotionFigure;
    }

    public Field getFrom() {
        return from;
    }

    public Field getTo() {
        return to;
    }

    public Class<? extends Figure> getPromotionFigure() {
        return promotionFigure;
    }
}
