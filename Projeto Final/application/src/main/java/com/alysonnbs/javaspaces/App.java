package com.alysonnbs.javaspaces;

import java.rmi.Naming;

import com.alysonnbs.javaspaces.models.MyMessage;
import com.alysonnbs.javaspaces.rmiinteface.SpyRMIinterface;
import com.alysonnbs.javaspaces.ui.MainScreen;

public class App {
    public static void main( String[] args ) {
        try {
            SpyRMIinterface stub = (SpyRMIinterface) Naming.lookup("//localhost:1099/SpyRMI");
            System.out.println("Encontrou o stub");
            Handler handler = new Handler();
            new MainScreen(handler);

            while (true) {
                MyMessage message = handler.spyWatchingMessages();
                System.out.println(message.from + " -> " + message.to + ": " + message.content);
                new AppThread(handler, stub, message).start();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
