package com.alysonnbs.gekitai.server;

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.alysonnbs.gekitai.communication.Request;

class ServerThread extends Thread {
    private Socket p1;
    private Socket p2;

    public ServerThread(Socket p1, Socket p2) {
        super();
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inP1 = new ObjectInputStream(p1.getInputStream());
            ObjectOutputStream outP2 = new ObjectOutputStream(p2.getOutputStream());

            while (true) {
                Request request = (Request) inP1.readObject();
                outP2.writeObject(request);
            }
        } catch (Exception e) {
            System.out.print("Um erro: ");
            System.out.println(e);
        }
    }
}