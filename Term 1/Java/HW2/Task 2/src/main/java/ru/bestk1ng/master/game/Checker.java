package ru.bestk1ng.master.game;

/**
 * A checker is a unique unit participating in the game.
 */
class Checker {
    private Boolean king;
    private Color color;

    Checker(Boolean isKing, Color color) {
        this.king = isKing;
        this.color = color;
    }

    Boolean isKing() {
        return king;
    }

    public void setKing(Boolean isKing) {
        this.king = isKing;
    }

    Color getColor() {
        return color;
    }

    Boolean isBlack() {
        return color == Color.BLACK;
    }

    enum Color { BLACK, WHITE }
}
