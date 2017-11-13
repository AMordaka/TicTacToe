package JUnit;

import game.Game;
import game.TicTacToeServer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;


public class TestServerClientGame {

        private static final int TEST_PORT = 5004;
        private TicTacToeServer ticTacToeServer = null;
        private Game game = null;


        @Before
        public void initialize() {
            ticTacToeServer = new TicTacToeServer();
            this.game = new Game();
        }

        @Test
        public void testClientsConnects() {
            try {
                ServerSocket socket = null;
                Game.Player playerX = null;
                Game.Player playerO = null;
                Game.Player playerY = null;
                assertTrue(ticTacToeServer.clientsConnect(socket, game, playerX, playerY, playerO, TEST_PORT));
            } catch (Exception e) {
                assertTrue(false);
            }
        }

        @Test
        public void testEmptyBoard(){
            assertFalse(game.hasWinner());
        }

        @Test
        public void testCheckVerticalWin() throws IOException {

            Game.Player X = game.new Player(new Socket(), 'Y');

            Game.Player[] board = {
                    X, null, null, null, null, null, null, null, null, null,
                    X, null, null, null, null, null, null, null, null, null,
                    X, null, null, null, null, null, null, null, null, null,
                    X, null, null, null, null, null, null, null, null, null,
                    X, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null,
                    null, null, null, null, null, null, null, null, null, null};
            game.setBoard(board);
            assertTrue(game.hasWinner());
        }

    @Test
    public void testCheckHorizontalWin() throws IOException {

        Game.Player X = game.new Player(new Socket(), 'Y');

        Game.Player[] board = {
                X, X, X, X, X, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null};
        game.setBoard(board);
        assertTrue(game.hasWinner());
    }

    @Test
    public void testCheckObliqueWin() throws IOException {

        Game.Player X = game.new Player(new Socket(), 'Y');

        Game.Player[] board = {
                X, null, null, null, null, null, null, null, null, null,
                null, X, null, null, null, null, null, null, null, null,
                null, null, X, null, null, null, null, null, null, null,
                null, null, null, X, null, null, null, null, null, null,
                null, null, null, null, X, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null};
        game.setBoard(board);
        assertTrue(game.hasWinner());
    }


}
