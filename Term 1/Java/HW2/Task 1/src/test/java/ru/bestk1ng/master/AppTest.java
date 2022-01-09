package ru.bestk1ng.master;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ru.bestk1ng.master.game.Game;
import ru.bestk1ng.master.game.errors.GameInvalidMoveException;
import ru.bestk1ng.master.game.errors.GameWhiteCellException;

public class AppTest {
    @Test
    void test1() throws Exception {
        String whiteCoordinates = "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2";
        String blackCoordinates = "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8";

        Game game = new Game(whiteCoordinates, blackCoordinates);

        game.makeMove("g3-f4 f6-e5");
        game.makeMove("c3-d4 e5:c3");
        game.makeMove("b2:d4 d6-c5");
        game.makeMove("d2-c3 g7-f6");
        game.makeMove("h2-g3 h8-g7");
        game.makeMove("c1-b2 f6-g5");
        game.makeMove("g3-h4 g7-f6");
        game.makeMove("f4-e5 f8-g7");

        assertEquals("a1 a3 b2 c3 d4 e1 e3 e5 f2 g1 h4", game.getWhiteCoordinates());
        assertEquals("a7 b6 b8 c5 c7 d8 e7 f6 g5 g7 h6", game.getBlackCoordinates());
    }

    @Test
    void test2() throws Exception {
        String whiteCoordinates = "a1 d2 d4 f2";
        String blackCoordinates = "b4 d6 f4";

        Game game = new Game(whiteCoordinates, blackCoordinates);

        game.makeMove("a1-b2 b4-a3");
        game.makeMove("d4-c5 a3:c1:e3:g1");
        game.makeMove("c5:e7 G1-C5");
        game.makeMove("e7-f8 C5-D4");

        assertEquals("F8", game.getWhiteCoordinates());
        assertEquals("D4 f4", game.getBlackCoordinates());
    }

    @Test
    void test3() throws Exception {
        String whiteCoordinates = "D8 c5 f6 g1";
        String blackCoordinates = "a5 f4 h6 h8";

        Game game = new Game(whiteCoordinates, blackCoordinates);

        assertThrows(GameInvalidMoveException.class, () -> {
            game.makeMove("c5-d6 f4-e3");
            game.makeMove("d6-c7 h8-g7");
            game.makeMove("c7-b8 g7:e5");
            game.makeMove("B8:F4:D2 h6-g5");
        });
    }

    @Test
    void test4() throws Exception {
        String whiteCoordinates = "E7 G3 b4";
        String blackCoordinates = "a7 b8 h6";

        Game game = new Game(whiteCoordinates, blackCoordinates);

        assertThrows(GameInvalidMoveException.class, () -> {
            game.makeMove("b4-c5 b8-c7");
            game.makeMove("G3:B8 h6-g5");
            game.makeMove("E7:H4 a7-b6");
            game.makeMove("c5-d6 b6-a5");
        });
    }

    @Test
    void test5() throws Exception {
        String whiteCoordinates = "a7 b4 d4 d6 h6";
        String blackCoordinates = "C1 d8 f2 f6 f8";

        Game game = new Game(whiteCoordinates, blackCoordinates);

        assertThrows(GameWhiteCellException.class, () -> {
            game.makeMove("d4-e5 f6:d4");
            game.makeMove("h6-g7 f8:h6");
            game.makeMove("d6-e7 d8:f6");
            game.makeMove("a7-b8 f6-e5");
            game.makeMove("B8:G3:E1 d4-d3");
            game.makeMove("b4-a5 h6-g5");
        });
    }
}
