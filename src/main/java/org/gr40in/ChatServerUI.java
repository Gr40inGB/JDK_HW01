package org.gr40in;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ChatServerUI extends JFrame implements ConnectionListener {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ChatServerUI();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private final int HEIGHT = 400;
    private final int WIDTH = 500;

    private final JTextArea log = new JTextArea();

    private final JButton btnStart = new JButton("Start");
    private final JButton btnExit = new JButton("Exit");
    private final JPanel panelBottom = new JPanel(new BorderLayout());

    public List<ChatConnection> connectionList = new ArrayList<>();
    private static Path logPath = Path.of("log.txt");
    public static BufferedWriter logWriter;
    public static BufferedReader logReader;

    static {
        try {
            logWriter = Files.newBufferedWriter(logPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
            logReader = Files.newBufferedReader(logPath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("logging failed");
        }
    }

    public ChatServerUI() throws IOException {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Server");
        add(log, BorderLayout.NORTH);
        panelBottom.add(btnStart, BorderLayout.WEST);
        panelBottom.add(btnExit, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);
        log.setEditable(false);
        log.append("start");

        setVisible(true);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
                log.append("Server started");
            }
        });


    }

    public synchronized void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            while (true) {
                ChatConnection newChat = new ChatConnection(serverSocket.accept(), ChatServerUI.this);
                connectionList.add(newChat);
                newChat.sendMessage(getLog());
                System.out.println("connect ok");
            }
        } catch (IOException ex) {
            try {
                logWriter.close();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            throw new RuntimeException(ex);
        }
    }

    @Override
    public synchronized void connectionOk(ChatConnection connection) {

    }

    @Override
    public synchronized void getMessage(String message) {
        sendMessage(message);
    }

    @Override
    public synchronized void sendMessage(String message) {
        if (!message.isEmpty()) writeToLog(message + "\n");
        try {
            for (ChatConnection chat : connectionList) {
                chat.sendMessage(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void writeToLog(String data) {
        try {
            logWriter.append(data);
            logWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized String getLog() {
        StringBuilder builder = new StringBuilder();
        try {
            logReader = Files.newBufferedReader(logPath);
            String line = "";
            while ((line = logReader.readLine()) != null) builder.append(line).append("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

    @Override
    public synchronized void disconnect(ChatConnection connection) {
        connectionList.remove(connection);
    }
}
