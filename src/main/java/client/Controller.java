package client;

import java.io.IOException;
import java.util.Arrays;

public class Controller {
    Net net;

    public Controller () throws IOException {
        this.net = new Net();
    }

    public boolean isConnected() {
        return this.net.isConnected();
    }

    void passCommand(String cmd) throws IOException, ClassNotFoundException {
        this.net.sendCommand(cmd);
        if (cmd.equals("quit"))
            this.quit();
    }


    public void quit() {
        this.net.disconnect();
        System.exit(0);
    }
}
