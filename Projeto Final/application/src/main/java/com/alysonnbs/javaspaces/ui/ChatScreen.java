package com.alysonnbs.javaspaces.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.alysonnbs.javaspaces.Handler;
import com.alysonnbs.javaspaces.models.MyMessage;


public class ChatScreen extends JFrame implements WindowListener {
    private final Handler handler;
    private final String user1, user2;
    private final JTextArea chatTextArea;
    private final JScrollPane scrollPane;
    private final JTextField textField;
    private final Thread lister;

    public ChatScreen(Handler handler, String user1, String user2) {
        this.handler = handler;
        this.user1 = user1;
        this.user2 = user2;
        
        setTitle("Chat de " + user1 + " e " + user2);
        setResizable(false);
        setSize(600, 600);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        scrollPane = new JScrollPane(chatTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(20, 20, 550, 480);

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                textFieldActionPerformed(event);
            }
        });
        textField.setBounds(20, 510, 550, 50);

        add(scrollPane);
        add(textField);

        setVisible(true);

        textField.requestFocus();

        lister = new Thread(new Runnable() {
            @Override
            public void run() {
                checkNewMessages();
            }
        });
        lister.start();
    }

    private void textFieldActionPerformed(ActionEvent event) {
        chatTextArea.append(user1 + ": " + textField.getText() + '\n');
        handler.sendMessage(user1, user2, textField.getText());
        textField.setText("");
        textField.requestFocus();
    }

    private void checkNewMessages() {
        while (true) {
            MyMessage message = handler.getMessage(user2, user1);
            chatTextArea.append(user2 + ": " + message.content + '\n');
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
        lister.interrupt();
    }

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowDeactivated(WindowEvent e) {}
}
