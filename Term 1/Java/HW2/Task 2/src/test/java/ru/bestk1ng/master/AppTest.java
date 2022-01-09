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
        String whiteCoordinates = "a1_w c1_w e1_w f2_ww h2_w g5_wbb";
        String blackCoordinates = "a3_b e3_b a5_bww c5_bwww e7_b g7_b";

        Game game = new Game(whiteCoordinates, blackCoordinates, false);

        assertThrows(GameInvalidMoveException.class, () -> {
            game.makeMove("f2_ww:d4_wwb:b6_wwbb g7_b-f6_b");
            game.makeMove("h2_w-g3_w f6_b:h4_bw:f2_bww");
            game.makeMove("e1_w:g3_wb g5_bb-h4_bb");
        });
    }

    @Test
    void test2() throws Exception {
        String whiteCoordinates = "a7_wbb b2_ww c1_w e1_w f2_w g1_w";
        String blackCoordinates = "b4_bwww b8_b c3_b c7_b e5_bww e7_b f8_b g5_b g7_b h8_b";

        Game game = new Game(whiteCoordinates, blackCoordinates, false);

        game.makeMove("b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb b4_bwww-a3_bwww");

        assertEquals("a7_wbb b6_Wwbbbb c1_w e1_w e5_ww f2_w g1_w", game.getWhiteCoordinates());
        assertEquals("a3_bwww b8_b f8_b g5_b g7_b h8_b", game.getBlackCoordinates());
    }

    @Test
    void test3() throws Exception {
        String whiteCoordinates = "a1_w c1_w d2_w e1_w f2_w g1_w g3_wbbb h4_ww";
        String blackCoordinates = "a3_bbww b4_bb b8_b d6_bw f4_b g7_bb";

        Game game = new Game(whiteCoordinates, blackCoordinates, false);

        assertThrows(GameInvalidMoveException.class, () -> {
            game.makeMove("g3_wbbb:e5_wbbbb:c7_wbbbbb g7_bb-h6_bb");
            game.makeMove("d6_w-e7_w b8_b-a7_b");
        });
    }
}
