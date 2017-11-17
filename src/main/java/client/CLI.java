package client;

import shared.GameSnapshot;
import shared.Response;
import shared.ResponseType;

import java.io.IOException;
import java.util.Scanner;


public class CLI implements Runnable {
    private Controller controller;
    private boolean connected = false;

    public void start () throws IOException, ClassNotFoundException {
        if (connected) {
            return;
        }
        controller = new Controller();
        this.connected = controller.isConnected();
        new Thread(this).start();
    }

    private static void showInputSign() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.print("> ");
    }

    @Override
    public void run () {
        while (connected) {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            showInputSign();
            try {
                this.controller.passCommand(command);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                showInputSign();
            }
        }
        System.out.println("Disconnected from server");
    }

    public static void handleResponse (Object obj) {
        Response response = (Response) obj;
        switch (response.getType()) {
            case CONNECTED:
                System.out.println("Welcome to HangMan! Enter 'start' to begin a new game, or 'quit' to exit.");
                break;
            case STARTED:
                System.out.println("Game started! Enter 'guess' followed by a letter or a word to start guessing");
                renderSnapshot((GameSnapshot) response.getData());
                break;
            case INFO:
                System.out.println(response.getMessage());
                break;
            case SNAPSHOT:
                GameSnapshot snapshot = (GameSnapshot) response.getData();
                renderSnapshot(snapshot);
                break;
            default:
                System.out.println("Unknown response from server");
        }
        showInputSign();
    }

    private static void renderSnapshot(GameSnapshot game) {
        String formatted;
        if (!game.isFinished()) {
            formatted = "Progress: " + game.getProgress() + " | Attempts remaining: " + (game.getMaxAttempts() - game.getAttempts() + " | Score: " + game.getScore());
        } else {
            formatted = "You've run out of attempts :( Enter 'start' to begin a new game, or 'quit' to exit";
            if (game.isWon())
                formatted = "Congratulations! You guessed it. Enter 'start' to begin a new game, or 'quit' to exit";
        }
        System.out.println(formatted);
    }
}
