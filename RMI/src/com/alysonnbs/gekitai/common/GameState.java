package com.alysonnbs.gekitai.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface GameState extends Remote {
    public void sendMessage(ChatMessage message) throws RemoteException;
    public void resign(int player) throws RemoteException;
    public void move(int i, int j, int player) throws RemoteException;
    public boolean gameActived() throws RemoteException;
    public int getBoard(int x, int y) throws RemoteException;
    public int getWinner() throws RemoteException;
    public int getCurrentPlayer() throws RemoteException;
    public int getQuittingPlayer() throws RemoteException;
    public ArrayList<ChatMessage> getMessages() throws RemoteException;
}
