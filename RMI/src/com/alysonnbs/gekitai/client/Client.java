package com.alysonnbs.gekitai.client;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

import com.alysonnbs.gekitai.common.ClientCallback;
import com.alysonnbs.gekitai.common.ServerOperations;

import com.alysonnbs.gekitai.client.ui.Screen;

public class Client {
    public static int player;

    public static void main(String[] args) {
        try {
            ServerOperations serverStub = (ServerOperations) Naming.lookup("//localhost:1099/ServerOperations");

            ClientCallbackImpl callbackImpl = new ClientCallbackImpl();
            ClientCallback callbackStub = (ClientCallback) UnicastRemoteObject.toStub(callbackImpl);

            serverStub.connect(callbackStub);
            
            while (!callbackImpl.isOk()) {
                Thread.sleep(1000);
            }

            Screen screen = new Screen(callbackImpl.state, callbackImpl.player);
            screen.start();
        } catch (Exception e) {
            System.out.print("Houve um erro: ");
            System.out.println(e);
        }
    }
}
