package cz.cvut.fel.pjv.chess.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChessServerThread extends Thread{
    private final Socket socket;

    public ChessServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("SERVER: Connected.");
        try (
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String line;
            while (!(line = br.readLine()).equals("BYE")) {
                System.out.println("Client sent: " + line);
            }
            System.out.println("SERVER: Finished.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
