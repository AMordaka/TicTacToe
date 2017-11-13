package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game {

    Player currentPlayer;
    private Player[] board = {
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null,
            null, null, null, null, null, null, null, null, null, null};

    public boolean hasWinner() {
        for(int i = 0; i < 100; i+=10){
            for(int j = 0; j < 6; j++) {
                if (board[j + i] != null && board[j + i] == board[j + i + 1] && board[j + i] == board[j + i + 2] && board[j + i] == board[j + i + 3] && board[j + i] == board[j + i + 4]) {
                    return true;
                }
            }
        }
        for(int i = 0; i < 10; i++) {
            for (int j = 0; j < 60; j+=10) {
                if(board[j + i] != null && board[j + i] == board[j + i + 10] && board[j + i] == board[j + i + 20] &&
                        board[j + i] == board[j + i + 30] && board[j + i] == board[j + i + 40]){
                    return true;
                }
            }
        }
        for (int i = 0; i < 6; i++) {
             for(int j = 0 ; j < 60 ; j+=10) {
                if(board[j + i] != null && board[j + i] == board[j + i + 11] && board[j + i] == board[j + i + 22] && board[j + i] == board[j + i + 33] && board[j + i] == board[j + i + 44]){
                    return true;
                }
            }
        }
        for (int i = 4; i < 10; i++) {
            for(int j = 0 ; j < 60 ; j+=10) {
                if(board[j + i] != null && board[j + i] == board[j + i + 9] && board[j + i] == board[j + i + 18] && board[j + i] == board[j + i + 27] && board[j + i] == board[j + i + 36]){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean boardFilledUp() {
        for (int i = 0; i < board.length; i++) {
            if (board[i] == null) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean legalMove(int location, Player player) {
        if (player == currentPlayer && board[location] == null) {
            board[location] = currentPlayer;
            currentPlayer = currentPlayer.secondOpponent;
            currentPlayer.secondOpponent.SecondPlayerMoved(location);
            currentPlayer.otherPlayerMoved(location);
            return true;
        }
        return false;
    }

    public void setBoard(Player[] board){
        this.board = board;
    }

  public class Player extends Thread {
        char mark;
        Player firstOpponent;
        Player secondOpponent;
        Socket socket;
        BufferedReader input;
        PrintWriter output;

        public Player(Socket socket, char mark) {
            this.socket = socket;
            this.mark = mark;
            try {
                input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("WELCOME " + mark);
                output.println("MESSAGE Waiting for opponent to connect");
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            }
        }

        public void setOpponents(Player opponent, Player secondOpponent) {
            this.firstOpponent = opponent;
            this.secondOpponent = secondOpponent;
        }

        public void otherPlayerMoved(int location) {
            output.println("OPPONENT_MOVED " + location);
            output.println(
                    hasWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
        }
        public void SecondPlayerMoved(int location) {
            output.println("SECONDPL_MOVED " + location);
            output.println(
                    hasWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
        }

        public void run() {
            try {
                output.println("MESSAGE All players connected");
                if (mark == 'X') {
                    output.println("MESSAGE Your move");
                }
                while (true) {
                    String command = input.readLine();
                    if (command != null){
                        if (command.startsWith("MOVE")) {
                            int location = Integer.parseInt(command.substring(5));
                            if (legalMove(location, this)) {
                                output.println("VALID_MOVE");
                                output.println(hasWinner() ? "VICTORY"
                                        : boardFilledUp() ? "TIE"
                                        : "");
                            } else {
                                output.println("MESSAGE ?");
                            }
                        } else if (command.startsWith("QUIT")) {
                            return;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Player died: " + e);
            } finally {
                try {socket.close();} catch (IOException e) {}
            }
        }
    }
}
