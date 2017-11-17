package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

class Server {
    private static final int LINGER_TIME = 5000;
    private static final int TIMEOUT_HALF_HOUR = 1800000;
    private final Controller controller = new Controller();
    private final List<ClientHandler> clients = new ArrayList<>();

    public static void main (String[] args) {
        Server server = new Server();
        server.serve(1337);
    }

    private void serve (int port) {
        try {
            ServerSocket listeningSocket = new ServerSocket(port);
            while (true) {
                Socket clientSocket = listeningSocket.accept();
                startHandler(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Server failure.");
            e.printStackTrace();
        }
    }

    private void startHandler (Socket clientSocket) throws SocketException {
        clientSocket.setSoLinger(true, LINGER_TIME);
        clientSocket.setSoTimeout(TIMEOUT_HALF_HOUR);
        ClientHandler client = new ClientHandler(this, clientSocket, "");
        synchronized (this.clients) {
            this.clients.add(client);
            printClientStatus();
        }
        Thread handlerThread = new Thread(client);
        handlerThread.setPriority(Thread.MAX_PRIORITY);
        handlerThread.start();
    }

    void removeHandler (ClientHandler handler) {
        synchronized (this.clients) {
            this.clients.remove(handler);
            printClientStatus();
        }
    }

    private void printClientStatus() {
        System.out.println(clients.size() + " connected");
    }
}