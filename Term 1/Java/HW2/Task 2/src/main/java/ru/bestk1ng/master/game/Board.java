package ru.bestk1ng.master.game;

import ru.bestk1ng.master.game.errors.GameBusyCellException;
import ru.bestk1ng.master.game.errors.GameErrorException;
import ru.bestk1ng.master.game.errors.GameInvalidMoveException;
import ru.bestk1ng.master.game.errors.GameWhiteCellException;

import java.util.ArrayList;
import java.util.List;

class Board {
    private static final int SIZE = 8;
    private static final char[] COLUMN_CHARS = "abcdefgh".toCharArray();

    private List<Checker>[][] board = new List[SIZE][SIZE];

    Board(List<Checker>[] checkers, Position[] positions) throws GameErrorException {
        if (checkers.length != positions.length) {
            throw new GameErrorException();
        }

        for (int i = 0; i < checkers.length; i++) {
            setChecker(checkers[i], positions[i]);
        }
    }

    void moveChecker(Position from, Position to) throws GameErrorException {
        if (canHit(from)) {
            throw new GameInvalidMoveException();
        }

        moveChecker(from, to, false);
    }

    void hitChecker(Position from, Position to) throws GameErrorException {
        moveChecker(from, to, true);
    }

    Position[] getPositions(Checker.Color color) {
        ArrayList<Position> positions = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            List<Checker>[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                List<Checker> checkerColumn = board[i][j];

                if (checkerColumn == null || checkerColumn.size() == 0) {
                    continue;
                }

                if (checkerColumn.get(0).getColor().equals(color)) {
                    positions.add(getPosition(i, j));
                }
            }
        }

        return positions.toArray(new Position[positions.size()]);
    }

    List<Checker>[] getCheckers(Checker.Color color) {
        List<List<Checker>> checkers = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            List<Checker>[] row = board[i];

            for (int j = 0; j < row.length; j++) {
                List<Checker> checkerColumn = board[i][j];

                if (checkerColumn == null || checkerColumn.size() == 0) {
                    continue;
                }

                if (checkerColumn.get(0).getColor().equals(color)) {
                    checkers.add(checkerColumn);
                }
            }
        }

        return checkers.toArray(new List[checkers.size()]);
    }

    void print() {
        System.out.print("  | ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(getColumn(i) + " | ");
        }
        System.out.println();

        for (int i = 0; i < board.length; i++) {
            List<Checker>[] row = board[i];

            System.out.print(getRow(i) + " | ");
            for (int j = 0; j < row.length; j++) {
                List<Checker> checkers = board[i][j];

                String symbol = "";
                if (checkers == null || checkers.size() == 0)  {
                    symbol = "  ";
                } else if (checkers.get(0).isBlack()) {
                    symbol = "●" + checkers.size();
                } else {
                    symbol = "○" + checkers.size();
                }

                System.out.print(symbol + "| ");
            }

            System.out.println();
        }
    }

    // PRIVATE

    private List<Checker> getChecker(Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        if (!isPositionOnBoard(rowIndex, columnIndex)) {
            return null;
        }

        return board[rowIndex][columnIndex];
    }

    private void setChecker(List<Checker> checker, Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        board[rowIndex][columnIndex] = checker;
    }

    private void moveChecker(Position from, Position to, Boolean isHit) throws GameErrorException {
        List<Checker> checker = getChecker(from);

        if (!isBlackField(to)) {
            throw new GameWhiteCellException();
        }

        if (getChecker(to) != null) {
            throw new GameBusyCellException();
        }

        if (checker == null) {
            throw new GameInvalidMoveException();
        }

        if (isHit) {
            hit(checker, from, to);
        } else {
            move(checker, from, to);
        }

        makeKingIfNeeded(to);
    }

    // ---------------------

    void makeKingIfNeeded(Position position) throws GameErrorException {
        List<Checker> checkerColumn = getChecker(position);
        Checker checker = checkerColumn.get(0);

        if (checker == null) {
            throw new GameErrorException();
        }

        int rowIndex = getRowIndex(position.row);

        if (checker.getColor() == Checker.Color.WHITE && rowIndex != (SIZE - 1)) {
            return;
        }

        if (checker.getColor() == Checker.Color.BLACK && rowIndex != 0) {
            return;
        }

        checker.setKing(true);
        setChecker(checkerColumn, position);
    }

    private void move(List<Checker> checker, Position from, Position to) throws GameInvalidMoveException {
        Position[] availableHitPositions = availableHitPositions(checker, from);
        if (availableHitPositions.length > 0 && !List.of(availableHitPositions).contains(to)) {
            throw new GameInvalidMoveException();
        }

        if (hasHitColleagues(from)) {
            throw new GameInvalidMoveException();
        }

        setChecker(checker, to);
        setChecker(null, from);
    }

    private void hit(List<Checker> checker, Position from, Position to) {
        Position[] hitPositions = hitPositions(from, to);

        setChecker(checker, to);
        setChecker(null, from);

        for (Position position : hitPositions) {
            List<Checker> hittedColumn = getChecker(position);

            if (hittedColumn != null && hittedColumn.size() > 0) {
                checker.add(hittedColumn.get(0));
            }

            if (hittedColumn != null && hittedColumn.size() > 1) {
                hittedColumn.remove(0);
            } else {
                setChecker(null, position);
            }
        }
    }

    private Integer getRowIndex(Integer row) {
        return row - 1;
    }

    private Integer getRow(Integer rowIndex) {
        return rowIndex + 1;
    }

    private Integer getColumnIndex(Character column) {
        Integer columnIndex = -1;
        for (int index = 0; index < COLUMN_CHARS.length; index++) {
            if (COLUMN_CHARS[index] == column) {
                columnIndex = index;
            }
        }

        return columnIndex;
    }

    private Character getColumn(Integer columnIndex) {
        return COLUMN_CHARS[columnIndex];
    }

    private Position getPosition(Integer rowIndex, Integer columnIndex) {
        Integer row = getRow(rowIndex);
        Character column = getColumn(columnIndex);
        return new Position(row, column);
    }

    private Boolean isBlackField(Position position) {
        int rowIndex = getRowIndex(position.row);
        int columnIndex = getColumnIndex(position.column);

        return (rowIndex + columnIndex) % 2 == 0;
    }

    private Boolean isPositionOnBoard(Integer rowIndex, Integer columnIndex) {
        return rowIndex >= 0
                && rowIndex < SIZE
                && columnIndex >= 0
                && columnIndex < SIZE;
    }

    private Position[] availableHitPositions(List<Checker> checkerColumn, Position position) {
        Checker checker = checkerColumn.get(0);

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

    private Position[] hitPositions(Position from, Position to)  {
        int fromRowIndex = getRowIndex(from.row);
        int fromColumnIndex = getColumnIndex(from.column);
        int toRowIndex = getRowIndex(to.row);
        int toColumnIndex = getColumnIndex(to.column);

        ArrayList<Position> positions = new ArrayList<>();

        if (toRowIndex > fromRowIndex && toColumnIndex > fromColumnIndex) {
            for (int rowIndex = fromRowIndex + 1; rowIndex < toRowIndex; rowIndex++) {
                for (int columnIndex = fromColumnIndex + 1; columnIndex < toColumnIndex; columnIndex++) {
                    positions.add(getPosition(rowIndex, columnIndex));
                }
            }
        } else if (toRowIndex > fromRowIndex && toColumnIndex < fromColumnIndex) {
            for (int rowIndex = fromRowIndex + 1; rowIndex < toRowIndex; rowIndex++) {
                for (int columnIndex = toColumnIndex + 1; columnIndex < fromColumnIndex; columnIndex++) {
                    positions.add(getPosition(rowIndex, columnIndex));
                }
            }
        } else if (toRowIndex < fromRowIndex && toColumnIndex > fromColumnIndex) {
            for (int rowIndex = toRowIndex + 1; rowIndex < fromRowIndex; rowIndex++) {
                for (int columnIndex = fromColumnIndex + 1; columnIndex < toColumnIndex; columnIndex++) {
                    positions.add(getPosition(rowIndex, columnIndex));
                }
            }
        } else if (toRowIndex < fromRowIndex && toColumnIndex < fromColumnIndex) {
            for (int rowIndex = toRowIndex + 1; rowIndex < fromRowIndex; rowIndex++) {
                for (int columnIndex = toColumnIndex + 1; columnIndex < fromColumnIndex; columnIndex++) {
                    positions.add(getPosition(rowIndex, columnIndex));
                }
            }
        }

        return positions.toArray(new Position[positions.size()]);
    }

    private boolean hasHitColleagues(Position position) {
        Checker.Color color = getChecker(position).get(0).getColor();
        List<Position> hitColleagues = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            List<Checker>[] row = board[i];
            for (int j = 0; j < row.length; j++) {
                List<Checker> checkerColumn = board[i][j];

                if (checkerColumn == null) {
                    continue;
                }

                Checker checker = checkerColumn.get(0);

                if (checker == null || !checker.getColor().equals(color)) {
                    continue;
                }

                Position colleaguePosition = getPosition(i, j);

                if (position.row == colleaguePosition.row && position.column == colleaguePosition.column) {
                    continue;
                }

                if (canHit(colleaguePosition)) {
                    hitColleagues.add(colleaguePosition);
                }
            }
        }

        return hitColleagues.size() > 0;
    }

    private Boolean canHit(Position position) {
        Checker.Color color = getChecker(position).get(0).getColor();

        Integer rowIndex = getRowIndex(position.row);
        Integer columnIndex = getColumnIndex(position.column);

        ArrayList<Position> positions = new ArrayList<>();

        // ↗
        if (isPositionOnBoard(rowIndex + 1, columnIndex + 1)
                && isPositionOnBoard(rowIndex + 2, columnIndex + 2)) {
            List<Checker> checker = getChecker(getPosition(rowIndex + 1, columnIndex + 1));
            List<Checker> nextChecker = getChecker(getPosition(rowIndex + 2, columnIndex + 2));

            if (checker != null && !checker.get(0).getColor().equals(color) && nextChecker == null) {
                return true;
            }
        }

        // ↘
        if (isPositionOnBoard(rowIndex - 1, columnIndex + 1)
                && isPositionOnBoard(rowIndex - 2, columnIndex + 2)) {
            List<Checker> checker = getChecker(getPosition(rowIndex - 1, columnIndex + 1));
            List<Checker> nextChecker = getChecker(getPosition(rowIndex - 2, columnIndex + 2));

            if (checker != null && !checker.get(0).getColor().equals(color) && nextChecker == null) {
                return true;
            }
        }

        // ↙
        if (isPositionOnBoard(rowIndex - 1, columnIndex - 1)
                && isPositionOnBoard(rowIndex - 2, columnIndex - 2)) {
            List<Checker> checker = getChecker(getPosition(rowIndex - 1, columnIndex - 1));
            List<Checker> nextChecker = getChecker(getPosition(rowIndex - 2, columnIndex - 2));

            if (checker != null && !checker.get(0).getColor().equals(color) && nextChecker == null) {
                return true;
            }
        }

        // ↖
        if (isPositionOnBoard(rowIndex + 1, columnIndex - 1)
                && isPositionOnBoard(rowIndex + 2, columnIndex - 2)) {
            List<Checker> checker = getChecker(getPosition(rowIndex + 1, columnIndex - 1));
            List<Checker> nextChecker = getChecker(getPosition(rowIndex + 2, columnIndex - 2));

            if (checker != null && !checker.get(0).getColor().equals(color) && nextChecker == null) {
                return true;
            }
        }

        return false;
    }

    static class Position {
        private Integer row;
        private Character column;

        Position(Integer row, Character column) {
            this.row = row;
            this.column = column;
        }

        Integer getRow() {
            return row;
        }

        Character getColumn() {
            return column;
        }
    }
}
