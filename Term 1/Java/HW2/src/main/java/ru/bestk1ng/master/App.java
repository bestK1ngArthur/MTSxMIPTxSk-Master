package ru.bestk1ng.master;

import java.util.Scanner;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String whiteCoordinates = scanner.nextLine();
            String blackCoordinates = scanner.nextLine();

            Game game = new Game(whiteCoordinates, blackCoordinates);

            while (scanner.hasNextLine()) {
                String move = scanner.nextLine();

                if (move.isEmpty()) {
                    break;
                }

                game.makeMove(move);
            }

            System.out.println(game.getWhiteCoordinates());
            System.out.println(game.getBlackCoordinates());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
