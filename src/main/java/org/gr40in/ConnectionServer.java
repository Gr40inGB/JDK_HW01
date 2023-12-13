package org.gr40in;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConnectionServer implements ConnectionListener {
    public static void main(String[] args) {
        try {
            new ConnectionServer();
        } catch (IOException e) {
            System.out.println("server failed");
        }
    }

    public List<ChatConnection> connectionList = new ArrayList<>();
    private static Path logPath = Path.of("log.txt");
    public static BufferedWriter logWriter;
    public static BufferedReader logReader;

    static {
        try {
            logWriter = Files.newBufferedWriter(logPath);
            logReader = Files.newBufferedReader(logPath);
        } catch (IOException e) {
            System.out.println("logging failed");
        }
    }

    public ConnectionServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            while (true) {
                ChatConnection newChat = new ChatConnection(serverSocket.accept(), this);
                connectionList.add(newChat);
                newChat.sendMessage(getLog());
                System.out.println("connect ok");
            }
        } catch (IOException e) {
            logWriter.close();
            throw new RuntimeException(e);
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
