package game.server;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;

@XmlRootElement(name = "Server")
public class TicTacToeServer {


    @XmlTransient
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {

        JAXBContext jaxbContext = JAXBContext.newInstance(TicTacToeServer.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        System.out.println(new File("config.xml").getAbsolutePath());
        TicTacToeServer config = (TicTacToeServer) jaxbUnmarshaller.unmarshal(new File("src/game/server/config.xml"));
        System.out.println(config.getPort());
        ServerSocket listener = new ServerSocket(config.getPort());
        System.out.println("Tic Tac Toe Server is Running");
        try {
            while (true) {
                Game game = new Game();
                Game.Player playerX = game.new Player(listener.accept(), 'X');
                Game.Player playerO = game.new Player(listener.accept(), 'O');
                Game.Player playerY = game.new Player(listener.accept(), 'Y');
                playerX.setOpponents(playerO,playerY);
                playerO.setOpponents(playerY,playerX);
                playerY.setOpponents(playerX,playerO);
                game.currentPlayer = playerX;
                playerX.start();
                playerO.start();
                playerY.start();
            }
        } finally {
            listener.close();
        }
    }

    public boolean clientsConnect(ServerSocket socket, Game game, Game.Player playerX, Game.Player playerY, Game.Player playerO, int port) {
        try {
                socket = new ServerSocket(port);
                while(playerO != null && playerX != null && playerY != null) {
                    playerX = game.new Player(socket.accept(), 'X');
                    playerO = game.new Player(socket.accept(), 'O');
                    playerY = game.new Player(socket.accept(), 'Y');
                }
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

