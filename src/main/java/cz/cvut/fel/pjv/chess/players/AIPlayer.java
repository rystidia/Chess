package cz.cvut.fel.pjv.chess.players;

import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Field;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.Pawn;
import cz.cvut.fel.pjv.chess.figures.Queen;
import javafx.util.Pair;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An AI player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Player for the description of all methods
 */
public class AIPlayer extends Player {
    private final int maxDepth;
    private final Random rand;

    /**
     * Initializes the player and sets the given color to him
     * <p>
     *
     * @param color a color
     */
    public AIPlayer(MyColor color) {
        super(color);
        this.maxDepth = 2;
        this.rand = new Random();
    }

    /**
     * Finds the optimal move using alpha-beta algorithm. Then makes the found move on the given board.
     * // Originally from https://www.chessprogramming.org/Alpha-Beta, adapted
     * <p>
     *
     * @param board a board on which the move will be done
     */
    @Override
    public void makeMove(Board board) {
        Pair<Figure, Field> decision = decision(board);
        Figure figure = decision.getKey();
        Field toPos = decision.getValue();
        board.moveFigure(figure, toPos);
        if (figure instanceof Pawn) {
            if ((figure.getColor() == MyColor.WHITE && toPos.row == 0) || (figure.getColor() == MyColor.BLACK && toPos.row == 7)) {
                ((Pawn) figure).promotion(Queen.class);
            }
        }
    }

    @Override
    public String getName() {
        return super.getName() != null ? super.getName() : "Computer";
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

    private List<Pair<Figure, Field>> getAllMoves(Board board, MyColor color){
        List<Pair<Figure, Field>> moves = new ArrayList<>();
        for (int r = 0; r <= Board.MAX_ROW; r++) {
            for (int c = 0; c <= Board.MAX_COL; c++) {
                Field field = new Field(r, c);
                Figure fig = board.getFigure(field);
                if (fig != null && fig.getColor() == color && fig.hasValidMoves()) {
                    Set<Field> validMoves = board.getValidMoves(fig);
                    for (Field toPos: validMoves) {
                        moves.add(new Pair<>(fig, toPos));
                    }
                }
            }
        }
        return moves;
    }

    private Board simulateAllMoves(List<Pair<Figure, Field>> moves, Board board) {
        Board newBoard = new Board(board);
        for (Pair<Figure, Field> move: moves) {
            Figure newFig = newBoard.getFigure(move.getKey().getPosition());
            newBoard.moveFigure(newFig, move.getValue());
        }
        return newBoard;
    }

    private float maxValue(Board b, ArrayList<Pair<Figure, Field>> state, float alpha, float beta, int depth) {
        if(depth > maxDepth)
            return eval1(b, state, getColor());

        Board newBoard = simulateAllMoves(state, b);
        List<Pair<Figure, Field>> moves = getAllMoves(newBoard, getColor());
        if(moves.size() == 0)
            return Float.NEGATIVE_INFINITY;

        for (Pair<Figure, Field> move : moves) {
            state.add(move);
            float tmp = minValue(b, state, alpha, beta, depth + 1);
            state.remove(state.lastIndexOf(move));
            if (tmp > alpha) {
                alpha = tmp;
            }

            if (beta <= alpha)
                break;
        }
        return alpha;
    }

    private float minValue(Board b, ArrayList<Pair<Figure, Field>> state, float alpha, float beta, int depth) {
        if(depth > maxDepth)
            return eval1(b, state, MyColor.getOppositeColor(getColor()));

        Board newBoard = simulateAllMoves(state, b);
        List<Pair<Figure, Field>> moves = getAllMoves(newBoard, MyColor.getOppositeColor(getColor()));

        if(moves.size() == 0)
            return Float.POSITIVE_INFINITY;

        for (Pair<Figure, Field> move : moves) {
            state.add(move);
            float tmp = maxValue(b, state, alpha, beta, depth + 1);
            state.remove(state.lastIndexOf(move));
            if (tmp < beta) {
                beta = tmp;
            }

            if (beta <= alpha)
                break;
        }
        return beta;
    }

    private Pair<Figure, Field> decision(final Board b) {
        // return optimal move

        final List<Pair<Figure, Field>> moves = getAllMoves(b, getColor());
        if(moves.size() == 0)
            return null;

        Vector<Future<Float>> costs = new Vector<>(moves.size());
        costs.setSize(moves.size());

        ExecutorService exec = Executors.newFixedThreadPool(moves.size());
        try {
            for (int i = 0; i < moves.size(); i++) {
                final Pair<Figure, Field> move = moves.get(i);
                Future<Float> result = exec.submit(() -> {
                    ArrayList<Pair<Figure, Field>> state = new ArrayList<>();
                    state.add(move);
                    return minValue(b, state, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 1);
                });
                costs.set(i, result);
            }
        } finally {
            exec.shutdown();
        }

        // find move that has the maximum cost
        int maxi = -1;
        float max = Float.NEGATIVE_INFINITY;
        for(int i = 0; i < moves.size(); i++) {
            float cost;
            try {
                cost = costs.get(i).get();
            } catch (Exception e) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ignored) {
                }
                continue;
            }
            if(cost >= max) {
                if(Math.abs(cost-max) < 0.1) // sometimes will act randomly
                    if(rand.nextBoolean())
                        continue;

                max = cost;
                maxi = i;
            }
        }
        return moves.get(maxi);
    }

    private float eval1(Board b, ArrayList<Pair<Figure, Field>> moves, MyColor currentColor) {
        Board newBoard = simulateAllMoves(moves, b);

        if(getMovableFigures(newBoard).size() == 0) {
            if(newBoard.getKing(currentColor).isInCheck())
                return (currentColor == getColor()) ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
            else
                return Float.NEGATIVE_INFINITY; // don't look for a draw
        }

        int whiteScore = 0;
        int blackScore = 0;

        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++) {
                Field pos = new Field(i, j);
                Figure fig = newBoard.getFigure(pos);
                if(fig != null)
                    if(fig.getColor() == MyColor.WHITE)
                        whiteScore += getValueOfFig(fig);
                    else
                        blackScore += getValueOfFig(fig);
            }

        if(getColor() == MyColor.WHITE)
            return whiteScore - blackScore;
        else
            return blackScore - whiteScore;
    }

    private int getValueOfFig(Figure fig){
        String name = fig.getClass().getSimpleName();
        return switch (name) { // costs of each figure
            case "Pawn" -> 10;
            case "Knight", "Bishop" -> 30;
            case "Rook" -> 50;
            case "Queen" -> 90;
            case "King" -> 900;
            default -> 0;
        };
    }
}
