package ru.bestk1ng.master.game.errors;

public class GameInvalidMoveException extends GameErrorException {
    public GameInvalidMoveException() {
        super("invalid move");
    }
}
