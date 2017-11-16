package server;

import shared.Response;
import shared.GameSnapshot;
import shared.ResponseType;

import java.io.*;
import java.net.Socket;

import static shared.ResponseType.*;

/**
 * Handles all communication with one particular chat client.
 */
class ClientHandler implements Runnable {
    private final Server server;
    private final Socket clientSocket;
    private final Controller controller;
    private ObjectOutputStream toClient;
    private boolean connected;

    ClientHandler(Server server, Socket clientSocket, String initialResponse) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.controller = new Controller();
        this.connected = true;
    }

    public void run() {
        BufferedReader fromClient;
        try {
            fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            toClient = new ObjectOutputStream(clientSocket.getOutputStream());
            sendResponse(new Response(null, CONNECTED, "Connected to server."));
        } catch (IOException ioex) {
            throw new UncheckedIOException(ioex);
        }
        while (connected) {
            try {
                String line = fromClient.readLine();
                if (line == null) {
                    this.disconnectClient();
                } else {
                    try {
                        ResponseType type = SNAPSHOT;
                        GameSnapshot snapshot = controller.parseCommand(line);
                        if (snapshot.isFirstRun())
                           type = STARTED;
                        sendResponse(new Response(snapshot, type, null));
                    } catch (IOException ioex) {
                        ioex.printStackTrace();
                    }
                }
            } catch (ErroneousInputException e) {
                try {
                    sendResponse(new Response(null, INFO, e.getMessage()));
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                    this.disconnectClient();
                }
            } catch (InternalError internalError) {
                try {
                    sendResponse(new Response(null, INFO, "Something went wrong on our end. Please try again later"));
                    this.disconnectClient();
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                    this.disconnectClient();
                }
            } catch (IOException ioex) {
                ioex.printStackTrace();
                this.disconnectClient();
            }
        }
    }

    private void sendResponse(Response response) throws IOException {
        toClient.writeObject(response);
        toClient.flush();
        toClient.reset();
    }

    private void disconnectClient() {
        try {
            clientSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        connected = false;
        server.removeHandler(this);
    }
}