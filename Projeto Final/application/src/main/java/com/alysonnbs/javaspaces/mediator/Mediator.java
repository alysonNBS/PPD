package com.alysonnbs.javaspaces.mediator;

import java.awt.Color;
import javax.jms.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Mediator extends JFrame implements MessageListener {
	// URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
 	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;//"tcp://192.168.100.100:61613";
	private static String topicName = "MENSAGENS_SUSPEITAS";
    
    private final JTextArea chatTextArea;
    private final JScrollPane scrollPane;

    public static void main(String[] args) {
        new Mediator();
    }

    public Mediator() {
        try {
		// Estabelecendo conexão com o Servidor JMS		
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// Criando Session
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// Criando Topic 
		Destination dest = session.createTopic(topicName);
		
		// Criando Consumidor
		MessageConsumer subscriber = session.createConsumer(dest);

		// Setando Listener
		subscriber.setMessageListener(this);	
		} catch(Exception e) {}

        setTitle("Moderador");
        setResizable(false);
        setSize(600, 600);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        scrollPane = new JScrollPane(chatTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(20, 20, 550, 530);

        add(scrollPane);
        setVisible(true);
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                chatTextArea.append(((TextMessage) message).getText() + '\n');
            } catch (Exception e) {}
        }
    }
}
