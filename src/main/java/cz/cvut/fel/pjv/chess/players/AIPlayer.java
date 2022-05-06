package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Field;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.figures.Figure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * An AI player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Player for the description of all methods
 */
public class AIPlayer extends Player {
    /**
     * Initializes the player and sets the given color to him
     * <p>
     *
     * @param color a color
     */
    public AIPlayer(MyColor color) {
        super(color);
    }

    @Override
    public void makeMove(Board board) {
        List<Figure> figs = getMovableFigures(board);
        Random random = new Random();
        int randomFig = random.nextInt(figs.size());
        Figure fig = figs.get(randomFig);
        Set<Field> valMoves = board.getValidMoves(fig);
        Field[] valMovesArray = valMoves.toArray(new Field[0]);
        int rNumber = random.nextInt(valMoves.size());
        board.moveFigure(fig, valMovesArray[rNumber]);
    }

    private List<Figure> getMovableFigures(Board board) {
        List<Figure> figs = new ArrayList<>();
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            for (int c = 0; c <= Board.MAX_COL; c++) {
                Field field = new Field(r, c);
                Figure fig = board.getFigure(field);
                if (fig != null && fig.getColor() == getColor() && fig.hasValidMoves()) {
                    figs.add(fig);
                }
            }
        }
        return figs;
    }
}
