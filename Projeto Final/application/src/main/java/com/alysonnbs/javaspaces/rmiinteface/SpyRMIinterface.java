package com.alysonnbs.javaspaces.rmiinteface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.alysonnbs.javaspaces.models.MyMessage;

public interface SpyRMIinterface extends Remote {
    void sendToMediator(MyMessage message) throws RemoteException;
    boolean isASuspectMessage(MyMessage message) throws RemoteException;
    void addSuspectWord(String word) throws RemoteException;
    void removeSuspectWord(String word) throws RemoteException;
}
