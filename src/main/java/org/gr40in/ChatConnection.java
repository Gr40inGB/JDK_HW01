package org.gr40in;

import java.io.*;
import java.net.Socket;

public class ChatConnection extends Thread {
    Socket socket;
    Thread thread;
    ConnectionListener listener;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;


    public ChatConnection(Socket socket, ConnectionListener listener) throws IOException {
        this.listener = listener;
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        this.start();
    }

    @Override
    public void run() {
        try {

        }

    }
}
