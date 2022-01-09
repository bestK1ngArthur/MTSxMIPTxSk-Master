package ru.bestk1ng.master.game.errors;

public class GameBusyCellException extends GameErrorException {
    public GameBusyCellException() {
        super("busy cell");
    }
}
