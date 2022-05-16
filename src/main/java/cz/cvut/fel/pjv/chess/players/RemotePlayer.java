package cz.cvut.fel.pjv.chess.players;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.chess.Board;
import cz.cvut.fel.pjv.chess.Field;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.figures.Figure;
import cz.cvut.fel.pjv.chess.figures.Pawn;
import cz.cvut.fel.pjv.chess.server.Packet;
import cz.cvut.fel.pjv.chess.server.Protocol;
import cz.cvut.fel.pjv.chess.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.chess.server.Protocol.*;

/**
 * A remote player
 *
 * @author pucilpet@fel.cvut.cz
 * @author rystidia@fel.cvut.cz
 * @version 1.0
 * @see #Player for the description of all methods
 */
public class RemotePlayer extends Player {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private static final int port = 5556;
    private static final String host = "localhost";

    private PrintWriter out;
    private Runnable moveCallback;
    private Runnable alertCallback;
    private Runnable opponentSurrenderCallback;
    private Runnable drawOfferDialogCallback;
    private Runnable startGameCallback;
    private Runnable gameDrawCallback;

    private Board board;

    public RemotePlayer() {
        start();
    }

    /**
     * Initializes the player and sets the given color to him
     * <p>
     *
     * @param color a color
     */
    public RemotePlayer(MyColor color) {
        super(color);
        start();
    }

    private void start() {
        new Thread(() -> {
        try (
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out = new PrintWriter(socket.getOutputStream(), true);
                boolean running = true;
                while (running) {
                    String msg = null;
                    try {
                        msg = in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (msg != null) {
                        processIncomingMessage(msg);
                    } else {
                        running = false;
                    }
                }

        } catch (ConnectException ex) {
            LOGGER.log(Level.SEVERE, "Server is not running. {0}", ex.getMessage());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Client can't connect. {0}", ex.getMessage());
        }
        }).start();
    }

    public void makeMove(Board board) {
        this.board = board;
    }

    private void processIncomingMessage(String msg) {
        ObjectMapper objectMapper = new ObjectMapper();
        Packet packet;
        try {
            packet = objectMapper.readValue(msg, Packet.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (Protocol.valueOf(packet.getType())) {
            case GAME_START:
                setColor(MyColor.getOppositeColor(packet.getColor()));
                setName(packet.getOpponentName());
                startGameCallback.run();
                break;
            case MOVE:
                Figure fig = board.getFigure(Field.fromAlgebraicNotation(packet.getFrom()));
                board.moveFigure(fig, Field.fromAlgebraicNotation(packet.getTo()));
                if (packet.getPromotionFigure() != null) {
                    ((Pawn) fig).promotion(Figure.getFigureClassByCharacter(packet.getPromotionFigure()));
                }
                moveCallback.run();
                break;
            case DRAW_OFFER:
                drawOfferDialogCallback.run();
                break;
            case RESPONSE_TO_OFFER:
                if (packet.getDrawAccepted().equals("Yes")) {
                    gameDrawCallback.run();
                    gameEnd(null);
                }
                break;
            case SURRENDER:
                opponentSurrenderCallback.run();
                break;
            case REJECTED:
                alertCallback.run();
                break;
        }
    }

    public void sendToServer(Packet packet) {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg;
        try {
            msg = objectMapper.writeValueAsString(packet);
            System.out.println(msg);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        out.println(msg);
    }

    private void gameEnd(MyColor winnerColor) {
        Packet p = new Packet(GAME_END.name());
        p.setWinnerColor(winnerColor);
        sendToServer(p);
    }

    public void setGameDrawCallback(Runnable gameDrawCallback) {
        this.gameDrawCallback = gameDrawCallback;
    }

    public void setMoveCallback(Runnable moveCallback) {
        this.moveCallback = moveCallback;
    }

    public void setAlertCallback(Runnable alertCallback) {
        this.alertCallback = alertCallback;
    }

    public void setOpponentSurrenderCallback(Runnable opponentSurrenderCallback) {
        this.opponentSurrenderCallback = opponentSurrenderCallback;
    }

    public void setDrawOfferDialogCallback(Runnable drawOfferDialogCallback) {
        this.drawOfferDialogCallback = drawOfferDialogCallback;
    }

    public void setStartGameCallback(Runnable startGameCallback) {
        this.startGameCallback = startGameCallback;
    }

    public void sendDrawResponse(String drawResponse) {
        Packet response = new Packet(RESPONSE_TO_OFFER.name());
        response.setDrawAccepted(drawResponse);
        sendToServer(response);
    }

    public void sendDrawOffer(){
        Packet drawOffer = new Packet(DRAW_OFFER.name());
        sendToServer(drawOffer);
    }

    public void sendMove(Field from, Field toPos, Class<? extends Figure> promClass) {
        Packet move = new Packet(MOVE.name());
        move.setFrom(from.toAlgebraicNotation());
        move.setTo(toPos.toAlgebraicNotation());
        if (promClass != null) {
            move.setPromotionFigure(Figure.getCharacterByFigureClass(promClass));
        } else {
            move.setPromotionFigure(null);
        }
        sendToServer(move);
    }

    public void sendMMRequest(String name) {
        Packet mmReq = new Packet(MATCHMAKE_REQUEST.name());
        mmReq.setName(name);
        sendToServer(mmReq);
    }

    public void sendSurrender(){
        Packet surrender = new Packet(SURRENDER.name());
        sendToServer(surrender);
    }
}
