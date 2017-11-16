package server;


import com.sun.tools.internal.xjc.BadCommandLineException;
import shared.GameSnapshot;
import server.Hangman.Hangman;

import java.util.ArrayList;

class Controller {
    private Hangman hangman;

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
            case "stop":
                return this.hangman.endGame(false);
            case "guess":
                return this.hangman.guess(command[1]);
            default:
                throw new ErroneousInputException("Unrecognized command");
        }
    }

}
