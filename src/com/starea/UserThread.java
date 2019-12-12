package com.starea;

import javax.sql.DataSource;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserThread extends Thread {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public UserThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            System.out.println(socket.getPort());
            while (true) {
                String[] message = input.readLine().split(":");
                System.out.println(message);
                if (message[0].equals("INVITE")) {
                    inviteAction(message);
                }

                if (message[0].equals("JOIN")) {
                    joinAction(message);
                }

                if(message[0].equals("LEAVE")) {
                    Artboard artboard = null;
                    Client client = null;
                    String code = message[1];
                    String name = message[2];
                    System.out.println(message[1]);
                    System.out.println(message[2]);

                    for(Artboard a: Datasource.getInstance().getArtboards()) {
                        if(a.getCode().equals(code)) {
                            artboard = a;
                        }
                    }

                    assert artboard != null;
                    for (Client c: artboard.getClients()) {
                        if(c.getClientName().equals(name)) {
                            client = c;
                        }
                    }
                    assert client != null;

                    leaveAction(artboard, client);

                }

                if(message[0].equals("UPDATE")) {
                    Artboard artboard = null;
                    String code = message[1];
                    String data = message[2];
                    for(Artboard a: Datasource.getInstance().getArtboards()) {
                        if(a.getCode().equals(code)) {
                            artboard = a;
                        }
                    }

                    updateAction(artboard, data);
                }

                if(message[0].equals("SENDMESSAGE")) {
                    Artboard artboard = null;
                    Client client = null;
                    String code = message[1];
                    String name = message[2];
                    System.out.println(message[1]);
                    System.out.println(message[2]);

                    for(Artboard a: Datasource.getInstance().getArtboards()) {
                        if(a.getCode().equals(code)) {
                            artboard = a;
                        }
                    }

                    assert artboard != null;
                    for (Client c: artboard.getClients()) {
                        if(c.getClientName().equals(name)) {
                            client = c;
                        }
                    }
                    assert client != null;

                    broadcastMessage(artboard, client, message[3]);
                }
            }
        } catch (IOException e) {
            System.out.println("Oops: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {

            }
        }
    }

    private void inviteAction(String[] message) throws IOException {

        Client client = Datasource.getInstance().getConnectedClients().pop();

        client.setClientName("host");

        Random r = new Random();
        int code = r.nextInt((9999 - 1000) + 1) + 9999;

        for (Artboard artboard : Datasource.getInstance().getArtboards()) {
            if (code == Integer.parseInt(artboard.getCode())) {
                code = r.nextInt((9999 - 1000) + 1) + 9999;
            }
        }

        Artboard newArtBoard = new Artboard();
        newArtBoard.setDrawingObjects(message[1]);
        System.out.println(code);
        System.out.println(message[1]);
        newArtBoard.getClients().add(client);
        newArtBoard.setCode(String.valueOf(code));

        Datasource.getInstance().getArtboards().push(newArtBoard);

        output.println("CODE" + ":" + code);
        output.println("NOTIFICATION" + ":" + "Open connection Successfully");
    }

    private void joinAction(String[] message) throws IOException {

        boolean flag = false;

        Client newClient = Datasource.getInstance().getConnectedClients().pop();

        String name = message[1];
        newClient.setClientName(name);
        System.out.println(newClient.getClientName());

        String code = message[2];
        System.out.println(code);

        for (Artboard artboard : Datasource.getInstance().getArtboards()) {
            if (artboard.getCode().equals(code)) {
                for (Client client : artboard.getClients()) {
                    if (client.getClientName().equals(newClient.getClientName())) {
                        output.println("NOTIFICATION" + ":" + "Name is already used by another client");
                        output.println("RESULT" + ":" + "Failed");
                        socket.close();
                        return;
                    }
                }
                addNewUser(artboard, newClient);
                output.println("DATA" + ":" + artboard.getDrawingObjects());
                flag = true;
            }
        }

        if (!flag) {
            output.println("NOTIFICATION" + ":" + "Cannot find artboard");
            output.println("RESULT" + ":" + "Failed");
            socket.close();
        } else {
            output.println("NOTIFICATION" + ":" + "Join Successfully");
        }
    }

    private void addNewUser(Artboard artboard, Client newClient) {
        for (Client client : artboard.getClients()) {
            client.getClientThread().output.println("NOTIFICATION" + ":" + newClient.getClientName() + " has joined this artboard");
        }
        artboard.getClients().add(newClient);
    }

    public void leaveAction(Artboard artboard, Client client) throws IOException {
        if (client.getClientName().equals("host")) {
            for (Client c : artboard.getClients()) {
                c.getClientThread().output.println("NOTIFICATION" + ":" + "Disconnecting");
                c.getClientThread().output.println("RESULT" + ":" + "Failed");
                c.getClientThread().socket.close();
            }
            artboard.getClients().clear();
            Datasource.getInstance().getArtboards().remove(artboard);
        } else {
            client.getClientThread().output.println("NOTIFICATION" + ":" + "Disconnecting");
            client.getClientThread().output.println("RESULT" + ":" + "Failed");
            client.getClientThread().socket.close();
            artboard.getClients().remove(client);
            for (Client c : artboard.getClients()) {
                c.getClientThread().output.println("NOTIFICATION" + ":" + client.getClientName() + " has left this artboard");
            }
        }
    }

    public void updateAction(Artboard artboard, String data) {
        for(Client client : artboard.getClients()) {
            client.getClientThread().output.println("NEEDUPDATE" + ":" + data);
            client.getClientThread().output.println("NOTIFICATION" + ":" + "New update released");
        }
    }

    public void broadcastMessage(Artboard artboard, Client client, String data) {
        for (Client c : artboard.getClients()) {
            c.getClientThread().output.println("MESSAGE" + ":" +"["+client.getClientName()+"]" + " " + data);
        }
    }
}
