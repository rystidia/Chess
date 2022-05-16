package cz.cvut.fel.pjv.chess.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private final int PORT_NUMBER;
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    private final Map<Connection, Connection> opponents;

    private ServerSocket serverSocket;
    private Socket socket;
    private Connection waitingConnection;

    public Server(int PORT_NUMBER) {
        this.PORT_NUMBER = PORT_NUMBER;
        opponents = new HashMap<>();
    }

    public static void main(String[] args) {
        Server server = new Server(5556);
        server.run();
    }

    @Override
    public void run() {
        try {
            // start server...
            serverSocket = new ServerSocket(PORT_NUMBER);
            LOGGER.info("The server is running.");
            while (true) {
                // listening for clients...
                socket = serverSocket.accept();
                LOGGER.info("The server is accepted connection.");
                // ...open new connection with client
                Connection connection = new Connection(this, socket);
                new Thread(connection).start();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "The server is not running. {0}", ex.getMessage());
        } finally {
            stop();
        }
    }

    public Connection getWaitingConnection() {
        return waitingConnection;
    }

    public void setWaitingConnection(Connection waitingConnection) {
        this.waitingConnection = waitingConnection;
    }

    public Connection getOpponent(Connection a){
        return opponents.get(a);
    }

    public void addConnection(Connection a, Connection b) {
        // add only connection with name not yet existing
        synchronized(opponents) {
            opponents.put(a, b);
            opponents.put(b, a);
            LOGGER.info("Adding connection");
        }
    }

    public boolean isNameUnique(String name){
        for (Connection connection: opponents.values()) {
            if (connection.getName().equals(name)) {
                System.out.println("reject");
                return false;
            }
        }
        if (getWaitingConnection() != null){
            return !getWaitingConnection().getName().equals(name);
        }
        return true;
    }

    public Map<Connection, Connection> getOpponents() {
        return opponents;
    }

    public void removeConnection(Connection connectionToRemove) {
        synchronized(opponents) {
            Connection opponent = opponents.get(connectionToRemove);
            opponents.remove(connectionToRemove);
            opponents.remove(opponent);
        }
    }

    public void stop() {
        LOGGER.info("Stopping server.");
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "The server is failed to stop properly. {0}", ex.getMessage());
        }
    }
}
