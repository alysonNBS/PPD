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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import com.alysonnbs.javaspaces.Handler;
import com.alysonnbs.javaspaces.models.User;

public class UserScreen extends JFrame {
    private final Handler handler;
    private final JLabel usersAreaTitle;
    private final JScrollPane userScrollPane;
    private final JList<String> userJList;
    private final DefaultListModel<String> userModel;
    private final JButton createUserButton;
    private final JButton startChatButton;

    public UserScreen(Handler handler) {
        this.handler = handler;
        
        setTitle("Chat com Espião - Usuários");
        setResizable(false);
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        usersAreaTitle = new JLabel("Ambientes Criados");
        usersAreaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        usersAreaTitle.setForeground(Color.BLACK);
        usersAreaTitle.setBounds(20, 10, 150, 30);
        
        userModel = new DefaultListModel<String>();
        userJList = new JList<String>();
        userJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                userJListOnMouseClicked(event);
            }
        });
        userScrollPane = new JScrollPane(userJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        userScrollPane.setBounds(20, 40, 150, 400);
        
        createUserButton = new JButton("Criar novo usuário");
        createUserButton.setBounds(190, 40, 180, 50);
        createUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createUserButtonActionPerformed(event);
            }
        });
        
        startChatButton = new JButton("Chat com usuários");
        startChatButton.setBounds(190, 180, 180, 50);
        startChatButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                startChatButtonActionPerformed(event);
            }
        });
        startChatButton.setEnabled(false);

        add(usersAreaTitle);
        add(userScrollPane);
        add(createUserButton);
        add(startChatButton);

        setUserList();
        setVisible(true);
    }

    private void userJListOnMouseClicked(MouseEvent event) {
        if (userJList.getSelectedValue() != null) {
            startChatButton.setEnabled(true);
        }
        else {
            startChatButton.setEnabled(false);
        }
    }
    
    private void createUserButtonActionPerformed(ActionEvent event) {
        handler.createUser();
        setUserList();
    }
    
    private void startChatButtonActionPerformed(ActionEvent event) {
        ArrayList<User> users = handler.getUsers();
        DefaultListModel<String> model = new DefaultListModel<String>();
        for (int i = 0; i < users.size(); ++i) {
            model.add(i, users.get(i).name);
        }
        JList<String> list = new JList<String>();
        list.setModel(model);
        JScrollPane scrollPane = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 150, 100);
        
        Object[] options = {"Iniciar Chat", "Cancelar"};
        int ans = JOptionPane.showOptionDialog(null, scrollPane, "Escolha outro usuário de seu Ambiente", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (ans == 0) {
            new ChatScreen(handler, userJList.getSelectedValue(), list.getSelectedValue());
        }
    }

    private void setUserList() {
        ArrayList<User> users = handler.getUsers();
        userModel.clear();
        for (int i = 0; i < users.size(); ++i) {
            userModel.add(i, users.get(i).name);
        }
        userJList.setModel(userModel);
    }
}
