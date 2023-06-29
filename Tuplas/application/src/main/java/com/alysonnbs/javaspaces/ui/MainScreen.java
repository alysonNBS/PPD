package com.alysonnbs.javaspaces.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.alysonnbs.javaspaces.Handler;

public class MainScreen extends JFrame {
    private final Handler handler;
    private final JButton environmentButton;
    private final JButton userButton;
    private final JButton deviceButton;

    public MainScreen(Handler handler) {
        this.handler = handler;

        setTitle("JavaSpaces Project");
        setResizable(false);
        setSize(300, 260);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        environmentButton = new JButton("Ambientes");
        environmentButton.setBounds(50, 20, 200, 50);
        environmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                environmentButtonActionPerformed();
            }
        });
        
        userButton = new JButton("Usuários");
        userButton.setBounds(50, 90, 200, 50);
        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                userButtonActionPerformed();
            }
        });
        
        deviceButton = new JButton("Dispositivos");
        deviceButton.setBounds(50, 160, 200, 50);;
        deviceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                deviceButtonActionPerformed();
            }
        });

        add(environmentButton);
        add(userButton);
        add(deviceButton);
        
        setVisible(true);
    }

    private void environmentButtonActionPerformed() {
        new EnvironmentScreen(this.handler);
    }

    private void userButtonActionPerformed() {
        new UserScreen(this.handler);
    }

    private void deviceButtonActionPerformed() {
        new DeviceScreen(this.handler);
    }
}
