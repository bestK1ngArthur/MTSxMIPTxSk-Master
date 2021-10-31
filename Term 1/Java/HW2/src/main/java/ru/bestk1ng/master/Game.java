package ru.bestk1ng.master;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class GameBusyCellException extends Exception {
    GameBusyCellException() {
        super("busy cell");
    }
}

class GameWhiteCellException extends Exception {
    GameWhiteCellException() {
        super("white cell");
    }
}

class GameInvalidMoveException extends Exception {
    GameInvalidMoveException() {
        super("invalid move");
    }
}

class GameErrorException extends Exception {
    GameErrorException() {
        super("error");
    }
}

public final class Game {
    private Board board;

    private static final String COORDINATE_REGEX = "([a-z])\\d";
    private static final String COORDINATE_KING_REGEX = "([A-Z])\\d";
    private static final String COORDINATE_DELIMITER = " ";

    public Game(String whiteCoordinates, String blackCoordinates) throws Exception {
        String[] white = whiteCoordinates.split(COORDINATE_DELIMITER);
        String[] black = blackCoordinates.split(COORDINATE_DELIMITER);

        ArrayList<Checker> checkersList = new ArrayList<>();
        checkersList.addAll(List.of(parseCheckers(white, Checker.Color.WHITE)));
        checkersList.addAll(List.of(parseCheckers(black, Checker.Color.BLACK)));
        Checker[] checkers = checkersList.toArray(new Checker[checkersList.size()]);

        ArrayList<Board.Position> positionsList = new ArrayList<>();
        positionsList.addAll(List.of(parsePositions(white)));
        positionsList.addAll(List.of(parsePositions(black)));
        Board.Position[] positions = positionsList.toArray(new Board.Position[positionsList.size()]);

        this.board = new Board(checkers, positions);
    }

    public void makeMove(String move) throws Exception {
        String[] movesArray = move.split(COORDINATE_DELIMITER);

        if (movesArray.length != 2) {
            throw new GameErrorException();
        }

        moveChecker(movesArray[0]);
        moveChecker(movesArray[1]);
    }

    public String getWhiteCoordinates() throws Exception {
        return getCoordinates(Checker.Color.WHITE);
    }

    public String getBlackCoordinates() throws Exception {
        return getCoordinates(Checker.Color.BLACK);
    }

    public String getCoordinates(Checker.Color color) throws Exception {
        Checker[] checkers = board.getCheckers(color);
        Board.Position[] positions = board.getPositions(color);

        if (checkers.length != positions.length) {
            throw new GameErrorException();
        }

        String[] coordinates = new String[checkers.length];

        for (int i = 0; i < checkers.length; i++) {
            coordinates[i] = getCoordinate(checkers[i].getKing(), positions[i]);
        }

        coordinates = Arrays.stream(coordinates)
                .sorted()
                .toArray(String[]::new);

        return String.join(COORDINATE_DELIMITER, coordinates);
    }

    private String getCoordinate(boolean isKing, Board.Position position) {
        String column;
        if (isKing) {
            column = String.valueOf(position.getColumn()).toUpperCase();
        } else {
            column = String.valueOf(position.getColumn());
        }

        return column + position.getRow();
    }

    private Checker[] parseCheckers(String[] coordinates, Checker.Color color) {
        List<Checker> checkers = Arrays.stream(coordinates)
                .map(coordinate -> this.parseChecker(coordinate, color))
                .collect(Collectors.toList());

        return checkers.toArray(new Checker[checkers.size()]);
    }

    private Checker parseChecker(String coordinate, Checker.Color color) {
        if (coordinate.matches(COORDINATE_KING_REGEX)) {
            return new Checker(true, color);
        } else if (coordinate.matches(COORDINATE_REGEX)) {
            return new Checker(false, color);
        }

        return null;
    }

    private Board.Position[] parsePositions(String[] coordinates) {
        List<Board.Position> positions = Arrays.stream(coordinates)
                .map(this::parsePosition)
                .collect(Collectors.toList());

        return positions.toArray(new Board.Position[positions.size()]);
    }

    private Board.Position parsePosition(String coordinate) {
        String lowerCasedCoordinate = coordinate.toLowerCase();

        if (!lowerCasedCoordinate.matches("(-|:)?(" + COORDINATE_REGEX + "|" + COORDINATE_KING_REGEX + ")")) {
            return null;
        }

        int row = Character.getNumericValue(lowerCasedCoordinate.charAt(1));
        char column = lowerCasedCoordinate.charAt(0);

        return new Board.Position(row, column);
    }

    private void moveChecker(String move) throws Exception {
        Pattern regex = Pattern.compile("(-|:)?(" + COORDINATE_REGEX + "|" + COORDINATE_KING_REGEX + ")");
        Matcher regexMatcher = regex.matcher(move);

        Board.Position oldPosition = null;
        while (regexMatcher.find()) {
            String string = regexMatcher.group();
            boolean isHit = false;

            if (string.matches("(-|:)(" + COORDINATE_REGEX + "|" + COORDINATE_KING_REGEX + ")")) {
                isHit = string.charAt(0) == ':';
                string = string.substring(1);
            }

            Board.Position position = parsePosition(string);

            if (oldPosition != null) {
                board.moveChecker(oldPosition, position, isHit);
            }

            oldPosition = position;
        }
    }
}
