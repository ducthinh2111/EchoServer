package com.starea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class RemoteDrawServer {
    private int port;

    public RemoteDrawServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        RemoteDrawServer server = new RemoteDrawServer(5000);
        server.execute();
    }

    private void execute() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                Client client = new Client();
                client.setClientThread(new UserThread(socket));
                Datasource.getInstance().getConnectedClients().push(client);
                client.getClientThread().start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
