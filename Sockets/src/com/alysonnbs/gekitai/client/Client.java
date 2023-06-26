package com.alysonnbs.gekitai.client;

import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

import com.alysonnbs.gekitai.client.ui.Screen;
import com.alysonnbs.gekitai.communication.Request;
import com.alysonnbs.gekitai.game.GameState;

public class Client {
    public static Semaphore mutex;
    public static int player;

    public static void main(String[] args) {
        mutex = new Semaphore(1);
        try {
            Socket socket = new Socket("localhost", 54321);
            
            {
                DataInputStream input = new DataInputStream(socket.getInputStream());
                player = input.readInt();
            }

            GameState state = new GameState();
            Screen screen = new Screen(socket, state, player, mutex);
            screen.start();

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Request response = (Request) input.readObject();

                mutex.acquire();
                state.update(response, (player == 1 ? 2 : 1));
                mutex.release();
            }
        } catch (Exception e) {
            System.out.print("Houve um erro: ");
            System.out.println(e);
        }
    }
}
