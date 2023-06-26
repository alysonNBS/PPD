package com.alysonnbs.gekitai.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Semaphore;

import com.alysonnbs.gekitai.common.ClientCallback;
import com.alysonnbs.gekitai.common.GameState;
import com.alysonnbs.gekitai.common.ServerOperations;

public class ServerOperationsImpl extends UnicastRemoteObject implements ServerOperations {
    ClientCallback waitCallback;
    Semaphore mutex;

    public ServerOperationsImpl() throws RemoteException {
        super();
        waitCallback = null;
        mutex = new Semaphore(1);
    }

    @Override
    public void connect(ClientCallback callback) throws RemoteException {
        try {
            mutex.acquire();
            if (waitCallback == null) {
                waitCallback = callback;
            }
            else {
                GameStateImpl stateImpl = new GameStateImpl();
                GameState stub = (GameState) UnicastRemoteObject.toStub(stateImpl);
                waitCallback.onIncomingGameState(stub, 1);
                callback.onIncomingGameState(stub, 2);
                waitCallback = null;
            }
            mutex.release();
        } catch (Exception e) {
            System.out.print("Houve um erro: ");
            System.out.println(e);
        }
    }
}
