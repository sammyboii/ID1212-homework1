package client;

import java.io.*;
import java.net.Socket;

public class Net {
    private PrintWriter toServer;
    private boolean connected = false;

    private Socket socket;

    public Net () {
        try {
            this.socket = new Socket("localhost", 1337);
            this.toServer = new PrintWriter(socket.getOutputStream(), true);
            Thread listener = new Thread(new Listener(this, socket));
            listener.start();
            this.connected = true;
        } catch (IOException ex) {
            System.err.println("Could not connect to server");
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void disconnect() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.connected = false;
    }

    public void sendCommand(String command) throws IOException, ClassNotFoundException {
        this.toServer.println(command);
    }

    class Listener implements Runnable {
        ObjectInputStream fromServer;
        Net net;
        boolean connected = false;

        Listener(Net net, Socket socket) {
            try {
                this.net = net;
                this.fromServer = new ObjectInputStream(socket.getInputStream());
                this.connected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (connected) {
                try {
                    Object response = fromServer.readObject();
                    CLI.handleResponse(response);
                } catch (Exception ex ) {
                    net.disconnect();
                }
            }
        }
    }
}
