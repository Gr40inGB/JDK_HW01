package org.gr40in;

import java.io.*;
import java.net.*;

public class ConnectionServer implements ConnectionListener {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555);
             Socket socket = serverSocket.accept();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            System.out.println("connect ok");


            String inputLine;
            while (true) {
                while ((inputLine = bufferedReader.readLine()) != null) {
                    if (".".equals(inputLine)) {
                        System.out.println("byby");
                        break;
                    }
                    System.out.println(inputLine);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void connectionOk(ChatConnection connection) {

    }

    @Override
    public void getMessage(String message) {

    }

    @Override
    public void sendMessage(String message) {

    }
}
