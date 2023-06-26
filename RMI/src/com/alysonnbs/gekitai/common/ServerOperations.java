package com.alysonnbs.gekitai.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerOperations extends Remote {
    void connect(ClientCallback callback) throws RemoteException;
}
