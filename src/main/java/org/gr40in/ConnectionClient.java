package org.gr40in;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectionClient implements ConnectionListener {
    public static void main(String[] args) throws Exception {


        try (Socket socket = new Socket("localhost", 5555);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            Scanner scanner = new Scanner(System.in);
//            while (true) {
                bufferedWriter.write("4123");
                bufferedWriter.flush();

//            }

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
