package client;

import java.io.IOException;
import java.util.Arrays;

public class Controller {
    Net net;
    private String[] commands = new String[]{"start","guess","quit"};

    public Controller () throws IOException {
        this.net = new Net();
    }

    public boolean isConnected() {
        return this.net.isConnected();
    }

    public void parseCommand(String cmd) throws Exception {
        String[] command = cmd.split(" ");
        Object response;

        switch (command[0]) {
            case "start":
                this.startGame();
                break;
            case "guess":
                if (command.length < 2)
                    throw new Exception("Guesses must be in the format 'guess <letter>' or 'guess <word>'.\nFor example: 'guess r' or 'guess pirate'");
                this.guess(command[1]);
                break;
            case "quit":
                this.quit();
                break;
            default:
                throw new Exception("'" + command[0] + "' is not a recognized command. Available commands: " + Arrays.toString(commands));
        }
    }

    public String[] getCommands() {
        return commands;
    }

    private void startGame() throws IOException, ClassNotFoundException {
        this.net.sendCommand("start");
    }

    private void guess(String characters) throws IOException, ClassNotFoundException {
        this.net.sendCommand("guess " + characters);
    }

    public void quit() {
        this.net.disconnect();
        System.exit(0);
    }
}
