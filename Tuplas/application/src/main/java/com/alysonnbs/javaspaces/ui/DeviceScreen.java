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
import com.alysonnbs.javaspaces.models.Device;

public class DeviceScreen extends JFrame {
    private final Handler handler;
    private final JLabel devicesAreaTitle;
    private final JScrollPane deviceScrollPane;
    private final JList<String> deviceJList;
    private final DefaultListModel<String> deviceModel;
    private final JButton createDeviceButton;
    private final JButton deleteDeviceButton;
    private final JButton chageDeviceEnvironmentButton;

    public DeviceScreen(Handler handler) {
        this.handler = handler;
        
        setTitle("JavaSpaces Project - Dispositivos");
        setResizable(false);
        setSize(400, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        devicesAreaTitle = new JLabel("Dispositivos Criados");
        devicesAreaTitle.setFont(new Font("Arial", Font.BOLD, 14));
        devicesAreaTitle.setForeground(Color.BLACK);
        devicesAreaTitle.setBounds(20, 10, 150, 30);
        
        deviceModel = new DefaultListModel<String>();
        deviceJList = new JList<String>();
        deviceJList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                deviceJListOnMouseClicked(event);
            }
        });
        deviceScrollPane = new JScrollPane(deviceJList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        deviceScrollPane.setBounds(20, 40, 150, 400);
        
        createDeviceButton = new JButton("Criar novo dispositivo");
        createDeviceButton.setBounds(190, 40, 180, 50);
        createDeviceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                createDeviceButtonActionPerformed(event);
            }
        });
        
        deleteDeviceButton = new JButton("Apagar dispositivo");
        deleteDeviceButton.setBounds(190, 110, 180, 50);
        deleteDeviceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                deleteDeviceButtonActionPerformed(event);
            }
        });
        deleteDeviceButton.setEnabled(false);
        
        chageDeviceEnvironmentButton = new JButton("Alterar ambiente");
        chageDeviceEnvironmentButton.setBounds(190, 180, 180, 50);
        chageDeviceEnvironmentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                chageDeviceEnvironmentButtonActionPerformed(event);
            }
        });
        chageDeviceEnvironmentButton.setEnabled(false);
        
        add(devicesAreaTitle);
        add(deviceScrollPane);
        add(createDeviceButton);
        add(deleteDeviceButton);
        add(chageDeviceEnvironmentButton);

        setDeviceList();
        setVisible(true);
    }

    private void deviceJListOnMouseClicked(MouseEvent event) {
        if (deviceJList.getSelectedValue() != null) {
            deleteDeviceButton.setEnabled(true);
            chageDeviceEnvironmentButton.setEnabled(true);
        }
        else {
            deleteDeviceButton.setEnabled(false);
            chageDeviceEnvironmentButton.setEnabled(false);
        }
    }
    
    private void createDeviceButtonActionPerformed(ActionEvent event) {
        handler.createDevice();
        setDeviceList();
    }
    
    private void deleteDeviceButtonActionPerformed(ActionEvent event) {
        deleteDeviceButton.setEnabled(false);
        handler.deleteDevice(deviceJList.getSelectedValue());
        setDeviceList();
    }
    
    private void chageDeviceEnvironmentButtonActionPerformed(ActionEvent event) {
        String environment = handler.getEnvironmentOfDevice(deviceJList.getSelectedValue());
        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(50, 30));
        JPanel panel = new JPanel();
        panel.add(new JLabel(deviceJList.getSelectedValue() + " está no ambiente " + environment + ". Digite o novo ambiente:"));
        panel.add(textField);
        textField.requestFocus();
        Object[] options = {"Alterar Ambiente", "Cancelar"};
        int ans = JOptionPane.showOptionDialog(null, panel, "Alterar ambiente de " + deviceJList.getSelectedValue(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (ans == 0) {
            handler.changeDeviceEnvironment(deviceJList.getSelectedValue(), textField.getText());
        }
    }

    private void setDeviceList() {
        ArrayList<Device> devices = handler.getDevices();
        deviceModel.clear();
        for (int i = 0; i < devices.size(); ++i) {
            deviceModel.add(i, devices.get(i).name);
        }
        deviceJList.setModel(deviceModel);
    }
}
