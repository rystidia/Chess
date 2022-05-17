package cz.cvut.fel.pjv.chess;

import cz.cvut.fel.pjv.chess.figures.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A board for a chess game
 * Board is nothing, but a matrix of the Fields
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 */
public class Board {
    public static final int MAX_ROW = 7;
    public static final int MAX_COL = 7;

    private final Figure[][] board;

    boolean gameMode = false;

    private Board initialBoard;

    private final MyColor startingColor = MyColor.WHITE;

    private final List<Move> history = new ArrayList<>();

    private Pawn enPassantPawn;

    /**
     * Initializes the board
     */
    public Board() {
        this.board = new Figure[MAX_ROW + 1][MAX_COL + 1];
    }

    /**
     * Copy constructor of the board. Copies board and all the figures on it.
     */
    public Board(Board srcBoard) {
        this();
        gameMode = srcBoard.gameMode;
        initialBoard = srcBoard.initialBoard;
        for (int r = 0; r <= MAX_ROW; r++) {
            for (int c = 0; c <= MAX_COL; c++) {
                Figure fig = srcBoard.board[r][c];
                if (fig != null) {
                    fig = fig.clone(this);
                }
                this.board[r][c] = fig;
            }
        }
    }

    public Figure getFigure(Field pos) {
        return board[pos.row][pos.column];
    }

    public void setFigure(Field pos, Figure figure) {
        board[pos.row][pos.column] = figure;
    }

    public void setEnPassantPawn(Pawn enPassantPawn) {
        this.enPassantPawn = enPassantPawn;
    }

    public Pawn getEnPassantPawn() {
        return enPassantPawn;
    }

    public void moveFigure(Figure figure, Field toPos) {
        moveFigure(figure, toPos, gameMode);
    }

    /**
     * Clears en passant, adds move to the history and executes the given move.
     */
    public void moveFigure(Figure figure, Field toPos, boolean writeToHistory) {
        if (enPassantPawn != null && figure.getColor() == enPassantPawn.getColor()){
            enPassantPawn.clearDoubleAdvance();
            enPassantPawn = null;
        }
        if (figure.getPosition() != null) {
            if (getFigure(figure.getPosition()) != figure) {
                throw new IllegalArgumentException("provided figure was not found where it should be");
            }
            setFigure(figure.getPosition(), null);
            if (writeToHistory) {
                history.add(new Move(figure.getPosition(), toPos));
            }
        } else {
            throw new IllegalStateException("placing new figure is not allowed with moveFigure()");
        }
        figure.move(toPos);

    }

    /**
     * Places the figure to the given field.
     */
    public void placeFigure(Figure figure, Field toPos) {
        if (figure.getPosition() != null) {
            if (getFigure(figure.getPosition()) != figure) {
                throw new IllegalArgumentException("provided figure was not found where it should be");
            }
            setFigure(figure.getPosition(), null);
        }
        figure.setPosition(toPos);
        setFigure(toPos, figure);
    }

    /**
     * Promotes the given pawn.
     */
    public void pawnPromotion(Pawn pawn, Figure promotionFig) {
        Move pawnMove = history.get(history.size() - 1);
        history.set(history.size() - 1, new Move(
            pawnMove.getFrom(),
            pawnMove.getTo(),
            promotionFig.getClass()
        ));
        // NB: calling moveFigure() instead would result in an IllegalStateException
        promotionFig.move(pawn.getPosition());
    }

    /**
     * Setup figures to initial chess position.
     */
    public void initialPosition() {
        for (MyColor color : Arrays.asList(MyColor.BLACK, MyColor.WHITE)) {
            int row = color == MyColor.WHITE ? 7 : 0;
            placeFigure(new Rook(color, this), new Field(row, 0));
            placeFigure(new Rook(color, this), new Field(row, 7));
            placeFigure(new Knight(color, this), new Field(row, 1));
            placeFigure(new Knight(color, this), new Field(row, 6));
            placeFigure(new Bishop(color, this), new Field(row, 2));
            placeFigure(new Bishop(color, this), new Field(row, 5));
            placeFigure(new Queen(color, this), new Field(row, 3));
            placeFigure(new King(color, this), new Field(row, 4));
        }
        for (int i = 0; i <= MAX_COL; i++) {
            placeFigure(new Pawn(MyColor.WHITE, this), new Field(6, i));
            placeFigure(new Pawn(MyColor.BLACK, this), new Field(1, i));
        }
    }

    /**
     * Returns all valid moves for given figure, checks if king is not in check after them
     */
    public Set<Field> getValidMoves(Figure figure) {
        Set<Field> validMoves = figure.getValidMoves();
        for (Iterator<Field> iterator = validMoves.iterator(); iterator.hasNext(); ) {
            Field pos = iterator.next();
            Board newBoard = simulateMove(figure, pos);
            if (newBoard.getKing(figure.getColor()).isInCheck()) {
                iterator.remove();
            }
        }
        return validMoves;
    }

    public Set<Figure> getFiguresByTypeAndColor(Class<? extends Figure> figClass, MyColor color) {
        Set<Figure> figures = new HashSet<>();
        for (Figure[] row : board) {
            for (Figure fig : row) {
                if (figClass.isInstance(fig) && fig.getColor() == color) {
                    figures.add(fig);
                }
            }
        }
        return figures;
    }

    public King getKing(MyColor color) {
        return (King) getFiguresByTypeAndColor(King.class, color).iterator().next();
    }

    /**
     * Creates new simulation board, executes the given move on it and returns the board.
     */
    public Board simulateMove(Figure figure, Field toPos) {
        Board newBoard = new Board(this);
        Figure newFig = newBoard.getFigure(figure.getPosition());
        newBoard.moveFigure(newFig, toPos);
        return newBoard;
    }

    /**
     * Returns the number of figures of the given color on the board.
     */
    public int getNumOfFigs(MyColor color) {
        int ret = 0;
        for (Figure[] row : board) {
            for (Figure fig : row) {
                if (fig != null && fig.getColor() == color)
                    ret++;
            }
        }
        return ret;
    }

    /**
     * Returns the number of figures of the given color and the given figure type on the board.
     */
    public int getNumOfSameFigs(Class<? extends Figure> figClass, MyColor color) {
        return getFiguresByTypeAndColor(figClass, color).size();
    }

    /**
     * Returns true if the number of missing pawns is greater than or equal to the number of additional pieces, false otherwise.
     */
    public boolean canPlaceFigure(Class<? extends Figure> figClass, MyColor color) {
        int extraKnights = Math.max(getNumOfSameFigs(Knight.class, color) + (figClass == Knight.class ? 1 : 0) - 2, 0);
        int extraBishops = Math.max(getNumOfSameFigs(Bishop.class, color) + (figClass == Bishop.class ? 1 : 0) - 2, 0);
        int extraQueens = Math.max(getNumOfSameFigs(Queen.class, color) + (figClass == Queen.class ? 1 : 0) - 1, 0);
        int extraRooks = Math.max(getNumOfSameFigs(Rook.class, color) + (figClass == Rook.class ? 1 : 0) - 2, 0);
        int extraFigs = extraRooks + extraQueens + extraBishops + extraKnights;
        int missingPawns = 8 - (getNumOfSameFigs(Pawn.class, color) + (figClass == Pawn.class ? 1 : 0));
        return extraFigs <= missingPawns;
    }

    public List<Move> getHistory() {
        return Collections.unmodifiableList(history);
    }

    public Board getInitialBoard() {
        return initialBoard;
    }

    /**
     * Saves the current board as initial and switches to game mode.
     */
    public void switchToGameMode() {
        if (gameMode) {
            return;
        }
        initialBoard = new Board(this);
        gameMode = true;
    }

    /**
     * Converts the board to FEN string.
     *
     * <p>
     * @see <a href="http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1">http://www.saremba.de/chessgml/standards/pgn/pgn-complete.htm#c16.1</a>
     */
    public String toFEN() {
        if (gameMode) {
            throw new UnsupportedOperationException("generating FEN for a board in game mode is not supported");
        }
        List<String> fields = new ArrayList<>();
        String boardField;
        {
            StringBuilder s = new StringBuilder();
            for (Figure[] row : board) {
                if (!s.isEmpty()) {
                    s.append('/');
                }
                int numEmpty = 0;
                for (Figure fig : row) {
                    if (fig == null) {
                        numEmpty++;
                        continue;
                    } else {
                        if (numEmpty != 0) {
                            s.append(numEmpty);
                        }
                        numEmpty = 0;
                    }
                    char ch = Figure.getCharacterByFigureClass(fig.getClass());
                    if (fig.getColor() == MyColor.BLACK) {
                        ch = Character.toLowerCase(ch);
                    }
                    s.append(ch);
                }
                if (numEmpty != 0) {
                    s.append(numEmpty);
                }
            }
            boardField = s.toString();
            fields.add(boardField);
        }
        fields.add(((history.size() + (startingColor == MyColor.BLACK ? 1 : 0)) & 1) == 0 ? "w" : "b");
        fields.add(boardField.equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR") ? "KQkq" : "-"); // castling availability
        fields.add("-"); // en passant target square
        fields.add("0"); // halfmove clock
        fields.add("1"); // fullmove number
        return String.join(" ", fields);
    }

    /**
     * Parses the FEN string and returns the board.
     */
    public static Board fromFEN(String fen) {
        String[] fields = fen.split(" ");
        Board brd = new Board();
        int row = 0;
        for (String rowStr : fields[0].split("/")) {
            int column = 0;
            for (char ch : rowStr.toCharArray()) {
                if (ch >= '0' && ch <= '9') {
                    column += (ch - '0');
                    continue;
                }
                Class<? extends Figure> figClass = Figure.getFigureClassByCharacter(Character.toUpperCase(ch));
                try {
                    brd.placeFigure(figClass
                        .getConstructor(MyColor.class, Board.class)
                        .newInstance(Character.isLowerCase(ch) ? MyColor.BLACK : MyColor.WHITE, brd), new Field(row, column));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                column++;
            }
            row++;
        }
        // FIXME
        if (!Objects.equals(fields[0], "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR")) {
            brd.getKing(MyColor.WHITE).setKingSideCastlingNotAvailable();
            brd.getKing(MyColor.WHITE).setQueenSideCastlingNotAvailable();
            brd.getKing(MyColor.BLACK).setKingSideCastlingNotAvailable();
            brd.getKing(MyColor.BLACK).setQueenSideCastlingNotAvailable();
        }
        // FIXME: ignored active color
        return brd;
    }
}
