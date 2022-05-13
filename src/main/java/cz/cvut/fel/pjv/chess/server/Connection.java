package cz.cvut.fel.pjv.chess.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.pjv.chess.MyColor;
import cz.cvut.fel.pjv.chess.figures.Figure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cz.cvut.fel.pjv.chess.server.Protocol.*;

public class Connection implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final Server server;
    private final Socket socket;
    private PrintWriter out;
    private String name;

    public Connection(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // setup connection
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            boolean running = true;
            while (running) {
                // read message from client
                String msg = in.readLine();
                LOGGER.log(Level.INFO, "Server received: >>>{0}<<<", msg);
                if (msg != null) {
                    running = processIncomingMessage(msg);
                } else {
                    // end communication if received null
                    running = false;
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error communicating with client {0}", ex.getMessage());
        } finally {
            quit();
        }
    }

    private boolean processIncomingMessage(String msg){
        ObjectMapper objectMapper = new ObjectMapper();
        Packet packet;
        try {
            packet = objectMapper.readValue(msg, Packet.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (Protocol.valueOf(packet.getType())) {
            // incoming message arrived, decide what to do
            case MATCHMAKE_REQUEST -> {
                name = packet.getName();
                handleMMRequest();
            }
            case MOVE, DRAW_OFFER, RESPONSE_TO_OFFER, SURRENDER -> sendToOpponent(packet);
            case GAME_END -> {
                server.removeConnection(this);
                return false;
            }
        }
        return true;
    }

    private synchronized void handleMMRequest(){
        Connection opponent = server.getWaitingConnection();
        if (!server.isNameUnique(name)) {
            sendPacket(new Packet(REJECTED.name()));
        } else {
            if (opponent == null) {
                server.setWaitingConnection(this);
                while(server.getWaitingConnection() != null) {
                    try {
                        wait();
                        sendGameStart(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                server.addConnection(this, opponent);
                server.setWaitingConnection(null);
                opponent.notify();
                sendGameStart(false);
            }
        }
    }

    public void sendPacket(Packet packet) {
        ObjectMapper objectMapper = new ObjectMapper();
        String msg;
        try {
            msg = objectMapper.writeValueAsString(packet);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        LOGGER.log(Level.INFO, "Sending >>>{0}<<< to {1}", new Object[]{msg, name});
        out.println(msg);
    }

    public boolean sendToOpponent(Packet packet) {
        Connection opponent = server.getOpponent(this);
        if (opponent != null) {
            opponent.sendPacket(packet);
            return true;
        } else {
            return false;
        }
    }

    private void sendGameStart(boolean color) {
        MyColor playerColor = color ? MyColor.WHITE : MyColor.BLACK;
        Packet gs = new Packet(GAME_START.name());
        gs.setOpponentName(name);
        gs.setColor(playerColor);
        sendPacket(gs);
    }

    private void quit() {
        LOGGER.info("Quitting connection.");
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    public String getName() {
        return name;
    }
}

