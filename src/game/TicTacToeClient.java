package game;

import javafx.stage.Stage;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TicTacToeClient {

    private static final String LOCALHOST = "localhost";

    private static int PORT = 4501;


    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel messageLabel = new JLabel("");
    private ImageIcon icon;
    private ImageIcon firstOpponentIcon;
    private ImageIcon secondOpponentIcon;

    private Square[] board = new Square[100];
    private Square currentSquare;
    private Stage stage;


    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public TicTacToeClient(String serverAddress) throws Exception {

        // Setup networking
        socket = new Socket(serverAddress, PORT);
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Layout GUI
        messageLabel.setBackground(Color.GREEN);
        frame.getContentPane().add(messageLabel, "South");

        JPanel boardPanel = new JPanel();
        boardPanel.setBackground(Color.black);
        boardPanel.setLayout(new GridLayout(10, 10, 2, 2));
        for (int i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Square();
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentSquare = board[j];
                    out.println("MOVE " + j);}});
            boardPanel.add(board[i]);
        }
        frame.getContentPane().add(boardPanel, "Center");
    }

    public void play() throws Exception {
        String response;
        try {
            response = in.readLine();
            if (response.startsWith("WELCOME")) {
                char mark = response.charAt(8);
                if(mark == 'X'){
                    icon = new ImageIcon("x.gif");
                    firstOpponentIcon = new ImageIcon("o.gif");
                    secondOpponentIcon = new ImageIcon("y.gif");
                    frame.setTitle("Tic Tac Toe - Player " + mark);
                }else if(mark == 'O'){
                    icon = new ImageIcon("o.gif");
                    firstOpponentIcon = new ImageIcon("y.gif");
                    secondOpponentIcon = new ImageIcon("x.gif");
                    frame.setTitle("Tic Tac Toe - Player " + mark);
                }else if(mark == 'Y'){
                    icon = new ImageIcon("y.gif");
                    firstOpponentIcon = new ImageIcon("x.gif");
                    secondOpponentIcon = new ImageIcon("o.gif");
                    frame.setTitle("Tic Tac Toe - Player " + mark);
                }
            }
            while (true) {
                response = in.readLine();
                if (response.startsWith("VALID_MOVE")) {
                    messageLabel.setText("Valid move, please wait");
                    currentSquare.setIcon(icon);
                    currentSquare.repaint();
                } else if (response.startsWith("OPPONENT_MOVED")) {
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc].setIcon(firstOpponentIcon);
                    board[loc].repaint();
                    messageLabel.setText("Opponent moved, your turn");
                } else if(response.startsWith("SECONDPL_MOVED")){
                    int loc = Integer.parseInt(response.substring(15));
                    board[loc].setIcon(secondOpponentIcon);
                    board[loc].repaint();
                }
                else if (response.startsWith("VICTORY")) {
                    messageLabel.setText("You win");
                    break;
                } else if (response.startsWith("DEFEAT")) {
                    messageLabel.setText("You lose");
                    break;
                } else if (response.startsWith("TIE")) {
                    messageLabel.setText("You tied");
                    break;
                } else if (response.startsWith("MESSAGE")) {
                    messageLabel.setText(response.substring(8));
                }
            }
           if(wantsToPlayAgain() == false) {
               out.println("QUIT");
               System.exit(1);
           }
        }
        finally {
            socket.close();
        }
    }

    private boolean wantsToPlayAgain() {
        int response = JOptionPane.showConfirmDialog(frame,
                "Want to play again?",
                "Tic Tac Toe ",
                JOptionPane.YES_NO_OPTION);
        frame.dispose();
        return response == JOptionPane.YES_OPTION;
    }

    static class Square extends JPanel {
        JLabel label = new JLabel((Icon)null);

        public Square() {
            setBackground(Color.white);
            add(label);
        }

        public void setIcon(Icon icon) {
            label.setIcon(icon);
        }
    }

    public static void main(String[] args) throws Exception {
        while (true) {
            TicTacToeClient client = new TicTacToeClient(LOCALHOST);
            client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            client.frame.setSize(800   , 600);
            client.frame.setVisible(true);
            client.frame.setResizable(false);
            client.play();
        }
    }
}