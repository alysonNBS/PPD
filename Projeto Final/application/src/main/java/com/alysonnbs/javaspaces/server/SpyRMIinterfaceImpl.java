package com.alysonnbs.javaspaces.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.alysonnbs.javaspaces.models.MyMessage;
import com.alysonnbs.javaspaces.rmiinteface.SpyRMIinterface;

public class SpyRMIinterfaceImpl extends UnicastRemoteObject implements SpyRMIinterface {
	// URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
 	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;//"tcp://192.168.100.100:61613";
	private static String topicName = "MENSAGENS_SUSPEITAS";

    private final MessageProducer publisher;
    private final TextMessage message;
    private final Set<String> words;

    private final Semaphore mutexWords;
    private final Semaphore mutexMessage;
    private final Semaphore mutexPubllisher;

    public SpyRMIinterfaceImpl() throws RemoteException, JMSException {
        super();

        words = new HashSet<>();

        mutexMessage = new Semaphore(1);
        mutexWords = new Semaphore(1);
        mutexPubllisher = new Semaphore(1);
        
        // Estabelecendo conexão com o Servidor JMS
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        
        // Criando Session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Criando Topic   
        Destination dest = session.createTopic(topicName);

        // Criando Produtor
        publisher = session.createProducer(dest);

        message = session.createTextMessage();
    }

    @Override
    public void sendToMediator(MyMessage msg) throws RemoteException {
        try {
            mutexMessage.acquire();
            message.setText("Mensagem de " + msg.from + " para " + msg.to + ": " + msg.content);
            mutexMessage.release();
            mutexPubllisher.acquire();
            publisher.send(this.message);
            mutexPubllisher.release();
        } catch (Exception e) {}
    }

    @Override
    public boolean isASuspectMessage(MyMessage message) throws RemoteException {
        boolean notSuspect = true;
        try {
            String[] messageWords = message.content.split(" ");
            mutexWords.acquire();
            for (int i = 0; i < messageWords.length && notSuspect; ++i) {
                System.out.print(messageWords[i] + ' ');
                notSuspect = !words.contains(messageWords[i]);
            }
            System.out.println();
            mutexWords.release();
        } catch (Exception e) {}
        return !notSuspect;
    }

    @Override
    public void addSuspectWord(String word) throws RemoteException {
        try {
            mutexWords.acquire();
            words.add(word);
            mutexPubllisher.release();
        } catch (Exception e) {}
    }
    
    @Override
    public void removeSuspectWord(String word) throws RemoteException {
        try {
            mutexWords.acquire();
            words.remove(word);
            mutexPubllisher.release();
        } catch (Exception e) {}
    }
}
