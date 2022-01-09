package ru.bestk1ng.master.game;

import ru.bestk1ng.master.game.errors.GameErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Game contains logic for playing Column Russian checkers
 * @author Artem Belkov
 */
public final class Game {
    private static final String COORDINATE_REGEX = "([a-z])\\d";
    private static final String COORDINATE_DELIMITER = " ";
    private static final String COLUMN_DELIMITER = "_";

    private static final Character WHITE_CHECKER = 'w';
    private static final Character BLACK_CHECKER = 'b';

    private Board board;
    private Boolean isLoggingEnabled;

    /**
     * Game constructor
     * @param whiteCoordinates the string with white chechkers coordinates
     * @param blackCoordinates the string with black chechkers coordinates
     * @param needLogging flag with logging enabled
     */
    public Game(String whiteCoordinates, String blackCoordinates, Boolean needLogging) throws GameErrorException {
        String[] white = whiteCoordinates.split(COORDINATE_DELIMITER);
        String[] black = blackCoordinates.split(COORDINATE_DELIMITER);

        List<List<Checker>> checkersList = new ArrayList<>();
        checkersList.addAll(List.of(parseCheckers(white)));
        checkersList.addAll(List.of(parseCheckers(black)));
        List<Checker>[] checkers = checkersList.toArray(new List[checkersList.size()]);

        ArrayList<Board.Position> positionsList = new ArrayList<>();
        positionsList.addAll(List.of(parsePositions(white)));
        positionsList.addAll(List.of(parsePositions(black)));
        Board.Position[] positions = positionsList.toArray(new Board.Position[positionsList.size()]);

        this.board = new Board(checkers, positions);
        this.isLoggingEnabled = needLogging;

        if (isLoggingEnabled) {
            board.print();
        }
    }

    /**
     * Make game move
     * @param move the string with white and black move
     */
    public void makeMove(String move) throws GameErrorException {
        String[] movesArray = move.split(COORDINATE_DELIMITER);

        if (movesArray.length != 2) {
            throw new GameErrorException();
        }

        if (isLoggingEnabled) {
            printMove(movesArray[0]);
        }

        moveChecker(movesArray[0]);

        if (isLoggingEnabled) {
            board.print();
        }

        if (isLoggingEnabled) {
            printMove(movesArray[1]);
        }

        moveChecker(movesArray[1]);

        if (isLoggingEnabled) {
            board.print();
        }
    }

    /**
     * Make game move
     * @return the string with coordinates of white checkers
     */
    public String getWhiteCoordinates() throws GameErrorException {
        return getCoordinates(Checker.Color.WHITE);
    }

    /**
     * Make game move
     * @return the string with coordinates of black checkers
     */
    public String getBlackCoordinates() throws GameErrorException {
        return getCoordinates(Checker.Color.BLACK);
    }

    private String getCoordinates(Checker.Color color) throws GameErrorException {
        List<Checker>[] checkers = board.getCheckers(color);
        Board.Position[] positions = board.getPositions(color);

        if (checkers.length != positions.length) {
            throw new GameErrorException();
        }

        String[] coordinates = new String[checkers.length];

        for (int i = 0; i < checkers.length; i++) {
            coordinates[i] = getCoordinate(checkers[i], positions[i]);
        }

        coordinates = Arrays.stream(coordinates)
                .sorted()
                .toArray(String[]::new);

        return String.join(COORDINATE_DELIMITER, coordinates);
    }

    private String getCoordinate(List<Checker> checkerColumn, Board.Position position) {
        String coordinate = position.getColumn().toString() + position.getRow() + COLUMN_DELIMITER;

        for (Checker checker : checkerColumn) {
            coordinate += getSymbol(checker);
        }

        return coordinate;
    }

    private String getSymbol(Checker checker) {
        String string = "";
        char symbol = checker.isBlack() ? BLACK_CHECKER : WHITE_CHECKER;

        if (checker.isKing()) {
            string += Character.toUpperCase(symbol);
        } else {
            string += symbol;
        }

        return string;
    }

    private List<Checker>[] parseCheckers(String[] coordinates) {
        List<List<Checker>> checkers = Arrays.stream(coordinates)
                .map(coordinate -> this.parseCheckerColumn(coordinate))
                .collect(Collectors.toList());

        return checkers.toArray(new List[checkers.size()]);
    }

    private List<Checker> parseCheckerColumn(String coordinate) {
        char[] symbols = coordinate.split(COLUMN_DELIMITER)[1].toCharArray();
        List<Checker> column = new ArrayList<>();

        for (char symbol : symbols) {
            Checker checker = parseChecker(symbol);
            if (checker != null) {
                column.add(checker);
            }
        }

        return column;
    }

    private Checker parseChecker(Character symbol) {
        Boolean isKing = Character.isUpperCase(symbol);
        Checker.Color color = Checker.Color.WHITE;
        if (Character.toLowerCase(symbol) == WHITE_CHECKER) {
            color = Checker.Color.WHITE;
        } else if (Character.toLowerCase(symbol) == BLACK_CHECKER) {
            color = Checker.Color.BLACK;
        }

        return new Checker(isKing, color);
    }

    private Board.Position[] parsePositions(String[] coordinates) {
        List<Board.Position> positions = Arrays.stream(coordinates)
                .map(this::parsePosition)
                .collect(Collectors.toList());

        return positions.toArray(new Board.Position[positions.size()]);
    }

    private Board.Position parsePosition(String coordinate) {
        String shortCoordinate = coordinate.split(COLUMN_DELIMITER)[0];

        if (!shortCoordinate.matches("(-|:)?(" + COORDINATE_REGEX + ")")) {
            return null;
        }

        int row = Character.getNumericValue(shortCoordinate.charAt(1));
        char column = shortCoordinate.charAt(0);

        return new Board.Position(row, column);
    }

    private void moveChecker(String move) throws GameErrorException {
        Pattern regex = Pattern.compile("(-|:)?(" + COORDINATE_REGEX + ")");
        Matcher regexMatcher = regex.matcher(move);

        Board.Position oldPosition = null;
        while (regexMatcher.find()) {
            String string = regexMatcher.group();
            boolean isHit = false;

            if (string.matches("(-|:)(" + COORDINATE_REGEX + ")")) {
                isHit = string.charAt(0) == ':';
                string = string.substring(1);
            }

            Board.Position position = parsePosition(string);

            if (oldPosition != null) {
                if (isHit) {
                    board.hitChecker(oldPosition, position);
                } else {
                    board.moveChecker(oldPosition, position);
                }
            }

            oldPosition = position;
        }
    }

    private void printMove(String move) {
        System.out.println();
        System.out.println(move);
        System.out.println();
    }
}
