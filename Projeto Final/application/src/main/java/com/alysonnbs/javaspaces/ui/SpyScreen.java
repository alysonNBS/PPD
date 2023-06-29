package com.alysonnbs.javaspaces.ui;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.alysonnbs.javaspaces.Handler;
import com.alysonnbs.javaspaces.models.SpyWord;

public class SpyScreen extends JFrame {
    private final Handler handler;
    private final JLabel wordsAreaTitle;
    private final JScrollPane wordScrollPane;
    private final JList<String> wordJList;
    private final DefaultListModel<String> wordModel;
    private final JButton createWordButton;
    private final JButton deleteWordButton;

    public SpyScreen(Handler handler) {
        this.handler = handler;
        
        setTitle("Chat com Espião - Palavras Suspeitas");
        setResizable(false);
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        wordsAreaTitle = new JLabel("Palavras Supeitas");
        wordsAreaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        wordsAreaTitle.setForeground(Color.BLACK);
        wordsAreaTitle.setBounds(20, 10, 150, 30);
        
        wordModel = new DefaultListModel<String>();
        wordJList = new JList<String>();
        wordJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                deviceJListOnMouseClicked(event);
            }
        });
        wordScrollPane = new JScrollPane(wordJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        wordScrollPane.setBounds(20, 40, 150, 400);
        
        createWordButton = new JButton("Adicionar palavra");
        createWordButton.setBounds(190, 40, 180, 50);
        createWordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createDeviceButtonActionPerformed(event);
            }
        });
        
        deleteWordButton = new JButton("Apagar palavra");
        deleteWordButton.setBounds(190, 110, 180, 50);
        deleteWordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                deleteDeviceButtonActionPerformed(event);
            }
        });
        deleteWordButton.setEnabled(false);
        
        
        add(wordsAreaTitle);
        add(wordScrollPane);
        add(createWordButton);
        add(deleteWordButton);

        setDeviceList();
        setVisible(true);
    }

    private void deviceJListOnMouseClicked(MouseEvent event) {
        if (wordJList.getSelectedValue() != null) {
            deleteWordButton.setEnabled(true);
        }
        else {
            deleteWordButton.setEnabled(false);
        }
    }
    
    private void createDeviceButtonActionPerformed(ActionEvent event) {
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(200, 30));
        JPanel panel = new JPanel();
        panel.add(new JLabel("Digite a nova palavra suspeita:"));
        panel.add(textField);
        textField.requestFocus();
        Object[] options = {"Cadastrar", "Cancelar"};
        int ans = JOptionPane.showOptionDialog(null, panel, "Adicionar nova palavra suspeita", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (ans == 0) {
            handler.addWord(textField.getText());
            setDeviceList();
        }
    }
    
    private void deleteDeviceButtonActionPerformed(ActionEvent event) {
        deleteWordButton.setEnabled(false);
        handler.deleteWord(wordJList.getSelectedValue());
        setDeviceList();
    }

    private void setDeviceList() {
        ArrayList<SpyWord> devices = handler.getWords();
        wordModel.clear();
        for (int i = 0; i < devices.size(); ++i) {
            wordModel.add(i, devices.get(i).word);
        }
        wordJList.setModel(wordModel);
    }
}
