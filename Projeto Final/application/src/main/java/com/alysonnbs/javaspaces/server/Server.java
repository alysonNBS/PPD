package com.alysonnbs.javaspaces.server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class Server {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            
            Remote spyStub = new SpyRMIinterfaceImpl();
            Naming.rebind("//localhost/SpyRMI", spyStub);
            System.out.println("Stub disponibilizado");
        } catch (Exception e) {}
    }
}
