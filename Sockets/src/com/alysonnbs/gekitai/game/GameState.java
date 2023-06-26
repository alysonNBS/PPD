package com.alysonnbs.gekitai.game;

import java.util.ArrayList;

import com.alysonnbs.gekitai.communication.Request;

public class GameState {
    private int[][] board;
    private int currentPlayer; // 0 = draw, -x (1 or 2) = x won, x (1 or 2) = x's turn
    private int[] piecesRemaining;
    private ArrayList<ChatMessage> messages;
    private int quittingPlayer; // 0 = nothing, x (1 or 2) = x quit
    private int drawRequest; // 0 = nothing, x (1 or 2) = x quit

    public GameState() {
        board = new int[8][8];
        piecesRemaining = new int[3];
        piecesRemaining[1] = piecesRemaining[2] = 8;
        currentPlayer = 1;
        messages = new ArrayList<ChatMessage>();
    }

    public void update(Request change, int player) {
        if (change.type == Request.RequestType.MESSAGE) {
            messages.add(new ChatMessage(player, change.data));
        }
        if (currentPlayer <= 0) {
            return;
        }
        else if (change.type == Request.RequestType.DRAW) {
            if (drawRequest == 0) {
                drawRequest = player;
            }
            else if (drawRequest != player) {
                currentPlayer = drawRequest = 0;
            }
        }
        else if (change.type == Request.RequestType.RESIGN) {
            quittingPlayer = player;
            currentPlayer = (player == 1 ? -2 : -1);
        }
        else if (change.type == Request.RequestType.MOVE && player == currentPlayer){
            int data = Integer.parseInt(change.data);
            System.out.println(data);
            int i = data/10, j = data % 10;
            if (board[i][j] != 0) {
                return;
            }
            board[i][j] = player;
            --piecesRemaining[player];

            // gekitai move
            int[][] dir = {{0,1}, {1,0}, {-1,0}, {0,-1}, {1,1}, {-1,1}, {1,-1}, {-1,-1}};
            for (int k = 0; k < 8; ++k) {
                if (board[i + dir[k][0]][j + dir[k][1]] != 0
                    && board[i + 2 * dir[k][0]][j + 2 * dir[k][1]] == 0) {
                    board[i + 2 * dir[k][0]][j + 2 * dir[k][1]] = board[i + dir[k][0]][j + dir[k][1]];
                    board[i + dir[k][0]][j + dir[k][1]] = 0;
                }
            }

            // return of out board pieces
            for (int k = 0; k < 8; ++k) {
                if (board[0][k] != 0) {
                    ++piecesRemaining[board[0][k]];
                    board[0][k] = 0;
                }
                if (board[7][k] != 0) {
                    ++piecesRemaining[board[7][k]];
                    board[7][k] = 0;
                }
                if (board[k][0] != 0) {
                    ++piecesRemaining[board[k][0]];
                    board[k][0] = 0;
                }
                if (board[k][7] != 0) {
                    ++piecesRemaining[board[k][7]];
                    board[k][7] = 0;
                }
            }

            // check if the player won
            boolean playerWon = piecesRemaining[player] == 0;
            for (int k = 3; k <= 6 && !playerWon; ++k) {
                for (int l = 1; l <= 6 && !playerWon; ++l) {
                    playerWon = (board[l][k] == player && board[l][k - 1] == player && board[l][k - 2] == player) ||
                                (board[k][l] == player && board[k - 1][l] == player && board[k - 2][l] == player);
                }
            }
            for (int k = 1; k <= 4 && !playerWon; ++k) {
                for (int l = 1; l <= 4 && !playerWon; ++l) {
                    playerWon = board[l][k] == player && board[l + 1][k + 1] == player && board[l + 2][k + 2] == player;
                }
            }
            for (int k = 6; k >= 3 && !playerWon; --k) {
                for (int l = 1; l <= 4 && !playerWon; ++l) {
                    playerWon = board[k][l] == player && board[k - 1][l + 1] == player && board[k - 2][l + 2] == player;
                }
            }

            if (playerWon) {
                currentPlayer = -player;
            }
            else {
                currentPlayer = player == 1 ? 2 : 1;
            }
            for (int k = 0; k < 8; ++k) {
                for (int l = 0; l < 8; ++l) {
                    System.out.print(board[k][l]);
                    System.out.print(' ');
                }
                System.out.println();
            }
        }
    }

    public boolean gameActived() {
        return currentPlayer > 0;
    }

    public int[][] getBoard() {
        return board;
    }

    public int getBoard(int x, int y) {
        return board[x][y];
    }

    public int getWinner() {
        return -currentPlayer;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isDraw() {
        return currentPlayer == 0;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    public int getPiecesRemaining(int player) {
        return piecesRemaining[player];
    }

    public int getQuittingPlayer() {
        return quittingPlayer;
    }
}
