package com.company;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6788);
        System.out.println("Listening on port 6788");

        new MenuThread().start();

        while(true) {
            Socket socket = serverSocket.accept();

            new com.company.EchoThread(socket).start();

            InetAddress ip = socket.getInetAddress();
            int port = socket.getPort();

            System.out.println("New client connected from: " + ip + ":" + port);


        }
    }
}
