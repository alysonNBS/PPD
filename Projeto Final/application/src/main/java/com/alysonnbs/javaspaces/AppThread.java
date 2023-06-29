package com.alysonnbs.javaspaces;

import com.alysonnbs.javaspaces.models.MyMessage;
import com.alysonnbs.javaspaces.rmiinteface.SpyRMIinterface;

public class AppThread extends Thread {
    final Handler handler;
    final SpyRMIinterface stub;
    final MyMessage message;

    public AppThread(Handler handler, SpyRMIinterface stub, MyMessage message) {
        this.handler = handler;
        this.stub = stub;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            handler.spySendMessage(message);
            System.out.println(message.content);
            // if (stub.isASuspectMessage(message)) {
                System.out.println("eh suspeita");
                stub.sendToMediator(message);
            // }
        } catch (Exception e) {}
    }
}