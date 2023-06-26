package com.alysonnbs.gekitai.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {
    void onIncomingGameState(GameState state, int player) throws RemoteException;
}
