package com.alysonnbs.gekitai.server;

import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(54321);
            while (true) {
                Socket player1 = serverSocket.accept();
                Socket player2 = serverSocket.accept();
                
                makeNewGame(player1, player2);
            }
        } catch (Exception e) {
            System.out.print("(Server.main) throws an Exception: ");
            System.out.println(e);
        }
    }

    private static void makeNewGame(Socket p1, Socket p2) {
        try {
            DataOutputStream outP1 = new DataOutputStream(p1.getOutputStream());
            DataOutputStream outP2 = new DataOutputStream(p2.getOutputStream());
            outP1.writeInt(1);
            outP2.writeInt(2);

            ServerThread thread1 = new ServerThread(p1, p2);
            ServerThread thread2 = new ServerThread(p2, p1);
            thread1.start();
            thread2.start();
        } catch (Exception e) {
            System.out.print("(Server.makeNewGame) throws an Exception: ");
            System.out.println(e);
        }
    }
}
