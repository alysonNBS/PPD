package com.alysonnbs.javaspaces.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.alysonnbs.javaspaces.Handler;

public class MainScreen extends JFrame {
    private final Handler handler;
    private final JButton userButton;
    private final JButton spyButton;

    public MainScreen(Handler handler) {
        this.handler = handler;

        setTitle("Chat com Espião");
        setResizable(false);
        setSize(300, 260);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        userButton = new JButton("Usuários");
        userButton.setBounds(50, 40, 200, 50);
        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                userButtonActionPerformed();
            }
        });
        
        spyButton = new JButton("Palavras Suspeitas");
        spyButton.setBounds(50, 130, 200, 50);;
        spyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                deviceButtonActionPerformed();
            }
        });

        add(userButton);
        add(spyButton);
        
        setVisible(true);
    }

    private void userButtonActionPerformed() {
        new UserScreen(this.handler);
    }

    private void deviceButtonActionPerformed() {
        new SpyScreen(this.handler);
    }
}
