package org.gr40in;

import java.io.*;
import java.net.Socket;

public class ChatConnection extends Thread {
    Socket socket;
    Thread thread;
    ConnectionListener listener;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    User user;


    public ChatConnection(String ip, int port, ConnectionListener listener) throws IOException {
        this(new Socket(ip, port), listener);
    }

    public ChatConnection(Socket socket, ConnectionListener listener) throws IOException {
//        this.user = user;
        this.listener = listener;
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.start();
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                listener.getMessage(bufferedReader.readLine());
            }
        } catch (IOException e) {
            listener.disconnect(this);
        }
    }

    public synchronized void sendMessage(String message) throws IOException {
        bufferedWriter.write(message + "\n");
        bufferedWriter.flush();
    }

}
