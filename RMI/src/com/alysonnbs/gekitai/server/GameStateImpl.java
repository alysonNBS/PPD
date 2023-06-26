package com.alysonnbs.gekitai.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.alysonnbs.gekitai.common.ChatMessage;
import com.alysonnbs.gekitai.common.GameState;

public class GameStateImpl extends UnicastRemoteObject implements GameState {
    private int[][] board;
    private int currentPlayer; // 0 = draw, -x (1 or 2) = x won, x (1 or 2) = x's turn
    private int[] piecesRemaining;
    private ArrayList<ChatMessage> messages;
    private int quittingPlayer; // 0 = nothing, x (1 or 2) = x quit
    private Semaphore mutex;

    public GameStateImpl() throws RemoteException {
        board = new int[8][8];
        piecesRemaining = new int[3];
        piecesRemaining[1] = piecesRemaining[2] = 8;
        currentPlayer = 1;
        messages = new ArrayList<ChatMessage>();
        mutex = new Semaphore(1);
    }

    @Override
    public void sendMessage(ChatMessage message) throws RemoteException {
        try {
            mutex.acquire();
            messages.add(message);
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.sendMessage: ");
            System.out.println(e);
        }
    }

    @Override
    public void resign(int player) throws RemoteException {
        try {
            mutex.acquire();
            if (currentPlayer <= 0) {
                mutex.release();
                return;
            }
            quittingPlayer = player;
            currentPlayer = (player == 1 ? -2 : -1);
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.resign: ");
            System.out.println(e);
        }
    }
    
    @Override
    public void move(int i, int j, int player) throws RemoteException {
        try {
            mutex.acquire();
            if (currentPlayer <= 0 || player != currentPlayer) {
                mutex.release();
                return;
            }
            if (board[i][j] != 0) {
                mutex.release();
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
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.move: ");
            System.out.println(e);
        }
    }

    @Override
    public boolean gameActived() throws RemoteException {
        boolean ret = true;
        try {
            mutex.acquire();
            ret = currentPlayer > 0;
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.gameActived: ");
            System.out.println(e);
        }
        return ret;
    }

    @Override
    public int getBoard(int x, int y) throws RemoteException {
        int val = 0;
        try {
            mutex.acquire();
            val = board[x][y];
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.getBoard: ");
            System.out.println(e);
        }
        return val;
    }

    @Override
    public int getWinner() throws RemoteException {
        int winner = 0;
        try {
            mutex.acquire();
            winner = -currentPlayer;
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.getWinner: ");
            System.out.println(e);
        }
        return winner;
    }

    @Override
    public int getCurrentPlayer() throws RemoteException {
        int ret = 0;
        try {
            mutex.acquire();
            ret = currentPlayer;
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.getCurrentPlayer: ");
            System.out.println(e);
        }
        return ret;
    }

    @Override
    public int getQuittingPlayer() throws RemoteException {
        int ret = 0;
        try {
            mutex.acquire();
            ret = quittingPlayer;
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.getQuittingPlayer: ");
            System.out.println(e);
        }
        return ret;
    }

    @Override
    public ArrayList<ChatMessage> getMessages() throws RemoteException {
        ArrayList<ChatMessage> ret = new ArrayList<ChatMessage>();
        try {
            mutex.acquire();
            messages.forEach((ChatMessage c) -> ret.add(new ChatMessage(c.player, c.message)));
            mutex.release();
        } catch (Exception e) {
            System.out.print("Error GameState.getMessages: ");
            System.out.println(e);
        }
        return ret;
    }
}
