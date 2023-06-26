package com.alysonnbs.gekitai.server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            
            Remote serverOperations = new ServerOperationsImpl();
            Naming.rebind("//localhost/ServerOperations", serverOperations);
        } catch (Exception e) {
            System.out.print("(Server.main) throws an Exception: ");
            System.out.println(e);
        }
    }
}
