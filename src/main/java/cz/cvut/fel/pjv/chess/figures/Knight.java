package cz.cvut.fel.pjv.chess.figures;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Color;
import cz.cvut.fel.pjv.chess.Field;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A knight piece
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Figure for the description of all methods
 */
public class Knight extends Figure {
    public Knight(Color color, Board board) {
        super(color, board);
    }

    @Override
    public Set<Field> getValidMoves() {
        Set<Field> validMoves = new HashSet<>();
        for (int oneDiff : Arrays.asList(-1, 1)) {
            for (int twoDiff : Arrays.asList(-2, 2)) {
                for (int i = 0; i < 2; i++) {
                    int tmp = oneDiff;
                    oneDiff = twoDiff;
                    twoDiff = tmp;
                    try {
                        Field pos = getPosition().plus(oneDiff, twoDiff);
                        Figure blockingFig = getBoard().getFigure(pos);
                        if (blockingFig == null || blockingFig.getColor() != getColor()) {
                            validMoves.add(pos);
                        }
                    } catch (IllegalArgumentException ignored) {
                    }
                }
            }
        }
        return validMoves;
    }
}
