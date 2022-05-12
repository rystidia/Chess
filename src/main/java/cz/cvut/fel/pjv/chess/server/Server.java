package cz.cvut.fel.pjv.chess.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final int PORT = 5556;

    public static void main(String[] args) throws IOException {
        ServerSocket sSocket = new ServerSocket(PORT);
        while (true) {
            System.out.println("SERVER: Waiting for connection.");
            Socket cSocket = sSocket.accept();
            new ChessServerThread(cSocket).start();
        }
    }
}
