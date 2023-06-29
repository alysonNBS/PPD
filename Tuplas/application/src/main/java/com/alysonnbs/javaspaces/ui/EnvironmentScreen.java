package com.alysonnbs.javaspaces.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.alysonnbs.javaspaces.Handler;
import com.alysonnbs.javaspaces.models.Device;
import com.alysonnbs.javaspaces.models.Environment;
import com.alysonnbs.javaspaces.models.User;

public class EnvironmentScreen extends JFrame {
    private final Handler handler;
    private final JLabel environmentsAreaTitle;
    private final JScrollPane environmentScrollPane;
    private final JList<String> environmentJList;
    private final DefaultListModel<String> environmentModel;
    private final JButton createEnvironmentButton;
    private final JButton deleteEnvironmentButton;
    private final JLabel usersAreaTitle;
    private final JTextArea userTextArea;
    private final JScrollPane userScrollPane;
    private final JLabel devicesAreaTitle;
    private final JTextArea deviceTextArea;
    private final JScrollPane deviceScrollPane;

    public EnvironmentScreen(Handler handler) {
        this.handler = handler;
        
        setTitle("JavaSpaces Project - Ambientes");
        setResizable(false);
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        environmentsAreaTitle = new JLabel("Ambientes Criados");
        environmentsAreaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        environmentsAreaTitle.setForeground(Color.BLACK);
        environmentsAreaTitle.setBounds(20, 10, 150, 30);
        
        environmentModel = new DefaultListModel<String>();
        environmentJList = new JList<String>();
        environmentJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                environmentJListOnMouseClicked(event);
            }
        });
        environmentScrollPane = new JScrollPane(environmentJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        environmentScrollPane.setBounds(20, 40, 150, 400);
        
        createEnvironmentButton = new JButton("Criar novo ambiente");
        createEnvironmentButton.setBounds(190, 40, 180, 50);
        createEnvironmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createEnvironmentButtonActionPerformed(event);
            }
        });
        
        deleteEnvironmentButton = new JButton("Apagar ambiente");
        deleteEnvironmentButton.setBounds(190, 110, 180, 50);
        deleteEnvironmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                deleteEnvironmentButtonActionPerformed(event);
            }
        });
        deleteEnvironmentButton.setEnabled(false);
        
        usersAreaTitle = new JLabel("Usuários do ambiente");
        usersAreaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        usersAreaTitle.setForeground(Color.BLACK);
        usersAreaTitle.setBounds(190, 180, 180, 20);
        
        userTextArea = new JTextArea();
        userTextArea.setEditable(false);
        userScrollPane = new JScrollPane(userTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userScrollPane.setBounds(190, 200, 180, 100);
        
        devicesAreaTitle = new JLabel("Dispositivos do ambiente");
        devicesAreaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        devicesAreaTitle.setForeground(Color.BLACK);
        devicesAreaTitle.setBounds(190, 320, 180, 20);
        
        deviceTextArea = new JTextArea();
        deviceTextArea.setEditable(false);
        deviceScrollPane = new JScrollPane(deviceTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        deviceScrollPane.setBounds(190, 340, 180, 100);

        add(environmentsAreaTitle);
        add(environmentScrollPane);
        add(createEnvironmentButton);
        add(deleteEnvironmentButton);
        add(usersAreaTitle);
        add(userScrollPane);
        add(devicesAreaTitle);
        add(deviceScrollPane);

        setEnviromentList();
        setVisible(true);
    }

    private void environmentJListOnMouseClicked(MouseEvent event) {
        if (environmentJList.getSelectedValue() != null) {
            deleteEnvironmentButton.setEnabled(true);
        }
        else {
            deleteEnvironmentButton.setEnabled(false);
        }
        setUsersAndDevicesOfEnvironment(environmentJList.getSelectedValue());
    }
    
    private void createEnvironmentButtonActionPerformed(ActionEvent event) {
        handler.createEnvironment();
        setEnviromentList();
    }
    
    private void deleteEnvironmentButtonActionPerformed(ActionEvent event) {
        deleteEnvironmentButton.setEnabled(false);
        handler.deleteEnvironment(environmentJList.getSelectedValue());
        setEnviromentList();
    }

    private void setEnviromentList() {
        ArrayList<Environment> environments = handler.getEnvironments();
        environmentModel.clear();
        for (int i = 0; i < environments.size(); ++i) {
            environmentModel.add(i, environments.get(i).name);
        }
        environmentJList.setModel(environmentModel);
    }
    
    private void setUsersAndDevicesOfEnvironment(String environment) {
        ArrayList<User> users = handler.getUsers(environment);
        ArrayList<Device> devices = handler.getDevices(environment);
        userTextArea.setText("");
        for (int i = 0; i < users.size(); ++i) {
            userTextArea.append(users.get(i).name + '\n');
        }
        deviceTextArea.setText("");
        for (int i = 0; i < devices.size(); ++i) {
            deviceTextArea.append(devices.get(i).name + '\n');
        }
    }
}
