package ru.bestk1ng.master.game.errors;

public class GameErrorException extends Exception {
    public GameErrorException() {
        new GameErrorException("error");
    }

    GameErrorException(String message) {
        super(message);
    }
}
