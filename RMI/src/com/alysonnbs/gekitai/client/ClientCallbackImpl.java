package com.alysonnbs.gekitai.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import com.alysonnbs.gekitai.common.ClientCallback;
import com.alysonnbs.gekitai.common.GameState;

public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback {
    public GameState state;
    public int player;
    private Semaphore mutex;

    protected ClientCallbackImpl() throws RemoteException {
        super();
        state = null;
        mutex = new Semaphore(1);
    }

    @Override
    public void onIncomingGameState(GameState state, int player) throws RemoteException {
        try {
            mutex.acquire();
            this.player = player;
            this.state = state;
            mutex.release();
        } catch (Exception e) {
        }
    }

    public boolean isOk() {
        boolean ok = false;
        try {
            mutex.acquire();
            ok = this.state != null;
            mutex.release();
        } catch (Exception e) {
        }
        return ok;
    }
}
