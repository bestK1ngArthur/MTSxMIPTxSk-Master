package ru.bestk1ng.master;

import java.util.ArrayList;
import java.util.List;

class Board {
    private static final int SIZE = 8;
    private static final char[] COLUMN_CHARS = "abcdefgh".toCharArray();

    private Checker[][] board = new Checker[SIZE][SIZE];

    Board(Checker[] checkers, Position[] positions) throws Exception {
        if (checkers.length != positions.length) {
            throw new GameErrorException();
        }

        for (int i = 0; i < checkers.length; i++) {
            setChecker(checkers[i], positions[i]);
        }
    }

    void moveChecker(Position from, Position to, boolean isHit) throws Exception {
        Checker checker = getChecker(from);

        if (checker == null) {
            throw new GameErrorException();
        }

        if (!isBlack(to)) {
            throw new GameWhiteCellException();
        }

        if (getChecker(to) != null) {
            throw new GameBusyCellException();
        }

        if (isHit) {
            hit(checker, from, to);
        } else {
            move(checker, from, to);
        }

        makeKingIfNeeded(to);
    }

    void makeKingIfNeeded(Position position) throws Exception {
        Checker checker = getChecker(position);

        if (checker == null) {
            throw new GameErrorException();
        }

        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        if (checker.getColor() == Checker.Color.WHITE && rowIndex != (SIZE - 1)) {
            return;
        }

        if (checker.getColor() == Checker.Color.BLACK && rowIndex != 0) {
            return;
        }

        checker.setKing(true);
        setChecker(checker, position);
    }

    Position[] getPositions(Checker.Color color) {
        ArrayList<Position> positions = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            Checker[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                Checker checker = board[i][j];

                if (checker == null) {
                    continue;
                }

                if (checker.getColor().equals(color)) {
                    positions.add(getPosition(i, j));
                }
            }
        }

        return positions.toArray(new Position[positions.size()]);
    }

    Checker[] getCheckers(Checker.Color color) {
        ArrayList<Checker> checkers = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            Checker[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                Checker checker = board[i][j];

                if (checker == null) {
                    continue;
                }

                if (checker.getColor().equals(color)) {
                    checkers.add(checker);
                }
            }
        }

        return checkers.toArray(new Checker[checkers.size()]);
    }

    private void move(Checker checker, Position from, Position to) throws Exception {
        Position[] hitPositions = hitPositions(checker, from);
        if (hitPositions.length > 0 && !List.of(hitPositions).contains(to)) {
            throw new GameInvalidMoveException();
        }

        setChecker(checker, to);
        setChecker(null, from);
    }

    private void hit(Checker checker, Position from, Position to) throws Exception {
        Position hitPosition = hitPosition(from, to);

        setChecker(checker, to);
        setChecker(null, from);
        setChecker(null, hitPosition);
    }

    private Checker getChecker(Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        if (!isPositionOnBoard(rowIndex, columnIndex)) {
            return null;
        }

        return board[rowIndex][columnIndex];
    }

    private void setChecker(Checker checker, Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);
        board[rowIndex][columnIndex] = checker;
    }

    private int getRowIndex(int row) {
        return row - 1;
    }

    private int getRow(int rowIndex) {
        return rowIndex + 1;
    }

    private int getColumnIndex(char column) {
        int columnIndex = -1;
        for (int index = 0; index < COLUMN_CHARS.length; index++) {
            if (COLUMN_CHARS[index] == column) {
                columnIndex = index;
            }
        }

        return columnIndex;
    }

    private char getColumn(int columnIndex) {
        return COLUMN_CHARS[columnIndex];
    }

    private Position getPosition(int rowIndex, int columnIndex) {
        int row = getRow(rowIndex);
        char column = getColumn(columnIndex);
        return new Position(row, column);
    }

    private boolean isBlack(Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        return (rowIndex + columnIndex) % 2 == 0;
    }

    private boolean isPositionOnBoard(int rowIndex, int columnIndex) {
        return rowIndex >= 0 && rowIndex < SIZE && columnIndex >= 0 && columnIndex < SIZE;
    }

    private Position[] hitPositions(Checker checker, Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        ArrayList<Position> positions = new ArrayList<>();

        if (isPositionOnBoard(rowIndex + 1, columnIndex)) {
            positions.add(getPosition(rowIndex + 1, columnIndex));
        }

        if (isPositionOnBoard(rowIndex - 1, columnIndex)) {
            positions.add(getPosition(rowIndex - 1, columnIndex));
        }

        if (isPositionOnBoard(rowIndex, columnIndex + 1)) {
            positions.add(getPosition(rowIndex, columnIndex + 1));
        }

        if (isPositionOnBoard(rowIndex, columnIndex - 1)) {
            positions.add(getPosition(rowIndex, columnIndex - 1));
        }

        return positions.stream()
                .filter(p -> getChecker(p) != null)
                .toArray(Position[]::new);
    }

    private Position hitPosition(Position from, Position to) throws Exception  {
        int fromRowIndex = getRowIndex(from.row);
        int fromColumnIndex = getColumnIndex(from.column);
        int toRowIndex = getRowIndex(to.row);
        int toColumnIndex = getColumnIndex(to.column);

        if (toRowIndex > fromRowIndex && toColumnIndex > fromColumnIndex) {
            return getPosition(toRowIndex - 1, toColumnIndex - 1);
        } else if (toRowIndex > fromRowIndex && toColumnIndex < fromColumnIndex) {
            return getPosition(toRowIndex - 1, fromColumnIndex - 1);
        } else if (toRowIndex < fromRowIndex && toColumnIndex > fromColumnIndex) {
            return getPosition(fromRowIndex - 1, toColumnIndex - 1);
        } else if (toRowIndex < fromRowIndex && toColumnIndex < fromColumnIndex) {
            return getPosition(fromRowIndex - 1, fromColumnIndex - 1);
        }

        throw new GameErrorException();
    }

    static class Position {
        private int row;
        private  char column;

        int getRow() {
            return row;
        }

        char getColumn() {
            return column;
        }

        Position(int row, char column) {
            this.row = row;
            this.column = column;
        }
    }
}
