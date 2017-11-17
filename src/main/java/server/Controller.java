package server;


import com.sun.tools.internal.xjc.BadCommandLineException;
import shared.GameSnapshot;
import server.Hangman.Hangman;

import java.util.ArrayList;
import java.util.Arrays;

class Controller {
    private Hangman hangman;
    private String[] commands = new String[]{"start","guess","quit"};

    Controller () {
        try {
            this.hangman = new Hangman();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    GameSnapshot parseCommand (String cmd) throws ErroneousInputException,InternalError {
        String[] command = cmd.split(" ");
        switch (command[0]) {
            case "start":
                return this.hangman.start();
            case "quit":
                return this.hangman.endGame(false);
            case "guess":
                if (command.length < 2)
                    throw new ErroneousInputException("Guesses must be in the format 'guess <letter>' or 'guess <word>'.\nFor example: 'guess r' or 'guess pirate'");
                return this.hangman.guess(command[1]);
            default:
                throw new ErroneousInputException("'" + command[0] + "' is not a recognized command. Available commands: " + Arrays.toString(commands));
        }
    }

    public String[] getCommands() {
        return commands;
    }

}
