package ru.bestk1ng.master;

class Checker {
    enum Color { BLACK, WHITE }

    private boolean isKing;
    private Color color;

    boolean getKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    Color getColor() {
        return color;
    }

    Checker(boolean isKing, Color color) {
        this.isKing = isKing;
        this.color = color;
    }
}
