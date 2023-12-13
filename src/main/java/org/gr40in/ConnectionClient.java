package org.gr40in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionClient extends JFrame implements ConnectionListener {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ConnectionClient();
            }
        });
    }

    private User user;
    private String server = "localhost";
    private int port = 5555;
    private final int HEIGHT = 400;

    private final int WIDTH = 500;
    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(3, 2));
    private final JTextField tfIPAddress = new JTextField(server);
    private final JTextField tfPort = new JTextField(String.valueOf(port));
    private final JTextField tfLogin = new JTextField("Richy");
    private final JPasswordField tfPassword = new JPasswordField("");
    private final JButton btnLogin = new JButton("Login");
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private ChatConnection connection;

    public ConnectionClient() throws HeadlessException {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ConnectionClient.this.connection = new ChatConnection(tfIPAddress.getText(),
                            Integer.parseInt(tfPort.getText()),
                            ConnectionClient.this);
                    log.append("Connection ok\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tfMessage.getText().equals("")) return;
                try {
                    connection.sendMessage(tfLogin.getText() + ": " + tfMessage.getText());
                    tfMessage.setText("");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog);
        setVisible(true);
    }


    @Override
    public void connectionOk(ChatConnection connection) {

    }

    @Override
    public void getMessage(String message) {
        log.append(message + "\n");
    }

    @Override
    public void sendMessage(String message) {
        log.append(message);
    }

    @Override
    public void disconnect(ChatConnection connection) {
        try {
            connection.sendMessage(tfLogin.getText() +" is exit :(");
        } catch (IOException e) {
            System.out.println("logout");
        }
    }
}
