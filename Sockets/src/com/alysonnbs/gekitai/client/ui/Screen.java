package com.alysonnbs.gekitai.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.alysonnbs.gekitai.communication.Request;
import com.alysonnbs.gekitai.game.ChatMessage;
import com.alysonnbs.gekitai.game.GameState;

public class Screen extends Thread implements MouseListener, ActionListener {
    private final Socket socket;
    private final GameState state;
    private final int player;
    private final Semaphore mutex;
    private ObjectOutputStream output;

    private JFrame window;
    private JPanel boardPanel;
    private JLabel infoLabel;
    private Square[] squares;
    private JButton resignButton;
    private JButton submitTextButton;
    private JTextField textField;
    private JTextArea chatTextArea;
    private JScrollPane scrollPane;

    public Screen(Socket socket, GameState state, int player, Semaphore mutex) {
        super();
        this.socket = socket;
        this.state = state;
        this.player = player;
        this.mutex = mutex;

        boardPanel = new JPanel();
        squares = new Square[36];
        boardPanel.setBounds(25, 100, 600, 600);
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.black, 3));
        boardPanel.setLayout(new GridLayout(6, 6));
        Color clearColor = new Color(181, 136, 99);
        Color blackColor = new Color(240, 217, 181);
        for (int i = 0; i < 36; ++i) {
            squares[i] = new Square(state, i / 6  + 1, i % 6 + 1);
            if ((i / 6) % 2 == 0) {
                if (i % 2 == 1)
                squares[i].setBackground(clearColor);
                else
                squares[i].setBackground(blackColor);
            }
            else {
                if (i % 2 == 0)
                squares[i].setBackground(clearColor);
                else
                squares[i].setBackground(blackColor);
            }
            
            boardPanel.add(squares[i]);
        }

        chatTextArea = new JTextArea();
        chatTextArea.setEditable(false);
        scrollPane = new JScrollPane(chatTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(650, 100, 300, 500);

        infoLabel = new JLabel();
        infoLabel.setFont(new Font("Arial", Font.BOLD, 40));
        infoLabel.setBounds(0, 0, 1000, 100);
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        infoLabel.setVerticalAlignment(JLabel.CENTER);

        textField = new JTextField();
        submitTextButton = new JButton("Enviar Mensagem");
        resignButton = new JButton("Abandonar Jogo");
        textField.setBounds(650, 615, 300, 40);
        submitTextButton.setBounds(810, 670, 140, 30);
        resignButton.setBounds(650, 670, 140, 30);
        
        window = new JFrame("Gekitai - Sockets");
        window.setResizable(false);
        window.setSize(1000, 800);
        window.setLayout(null);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        window.add(boardPanel);
        window.add(scrollPane);
        window.add(textField);
        window.add(submitTextButton);
        window.add(resignButton);
        window.add(infoLabel);
        
        window.setVisible(true);
    }
    
    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            submitTextButton.addActionListener(this);
            textField.addActionListener(this);
            resignButton.addActionListener(this);
            textField.requestFocus();
            for (int i = 0; i < 36; ++i) {
                squares[i].addMouseListener(this);
            }
            while (true) {
                setState();
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.print("Houve um erro: ");
            System.out.println(e);
        }
    }

    void setState() {
        try {
            mutex.acquire();
            if (state.gameActived()) {
                if (state.getCurrentPlayer() == player)
                    infoLabel.setText("Sua vez!");
                else
                    infoLabel.setText("Vez do adversário!");
            }
            else {
                resignButton.setEnabled(false);
                if (state.getQuittingPlayer() != 0) {
                    if (state.getWinner() == player)
                        infoLabel.setText("O adversário abandonou o jogo! Você ganhou!");
                    else
                        infoLabel.setText("Você abandonou o jogo! Você perdeu!");
                }
                else {
                    if (state.getWinner() == player)
                        infoLabel.setText("Parabéns! Você ganhou!");
                    else
                        infoLabel.setText("Que pena! Você perdeu!");
                }
            }
            for (int i = 1; i <= 6; ++i) {
                for (int j = 1; j <= 6; ++j) {
                    squares[(i - 1) * 6 + j - 1].repaint();
                }
            }
            ArrayList<ChatMessage> messages = state.getMessages();
            chatTextArea.setText("");
            for (int i = 0; i < messages.size(); ++i) {
                String prefix = " ";
                if (messages.get(i).player == this.player) {
                    prefix += "Me: ";
                }
                else {
                    prefix += "Opponent: ";
                }
                chatTextArea.append(prefix + messages.get(i).message + '\n');
            }
            mutex.release();
        } catch (Exception e) {
            System.out.print("Houve um erro: ");
            System.out.println(e);
        }
    }

    private class Square extends JPanel {
        private final GameState state;
        private final int x, y;

        public Square(GameState state, int x, int y) {
            super();
            this.state = state;
            this.x = x;
            this.y = y;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int val = state.getBoard(x, y);
            if (val != 0) {
                g.setColor(val == 1 ? Color.red : Color.blue);
                g.fillOval(15, 15, 70, 70);
            }
        }
    }
   
	@Override
	public void mouseClicked(MouseEvent e) {
        try {
            for (int i = 0; i < 36; ++i) {
                if (e.getSource() == squares[i]) {
                    mutex.acquire();
                    Request request = new Request(Request.RequestType.MOVE, Integer.valueOf(i/6 + 1).toString() + Integer.valueOf(i%6 + 1).toString());
                    state.update(request, player);
                    output.writeObject(request);
                    mutex.release();
                }
            }
        } catch (Exception ex) {
            System.out.print("Houve um erro: ");
            System.out.println(ex);
        }
    }

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == submitTextButton || e.getSource() == textField) {
                mutex.acquire();
                Request request = new Request(Request.RequestType.MESSAGE, textField.getText());
                state.update(request, player);
                textField.setText("");
                output.writeObject(request);
                mutex.release();
            }
            if (e.getSource() == resignButton) {
                mutex.acquire();
                Request request = new Request(Request.RequestType.RESIGN, "");
                state.update(request, player);
                output.writeObject(request);
                mutex.release();
            }
        } catch (Exception ex) {
            System.out.print("Houve um erro: ");
            System.out.println(ex);
        }
	}
}
