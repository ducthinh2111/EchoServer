package com.starea;

public class Client {
    private volatile String clientName;
    private volatile UserThread ClientThread;

    public synchronized String getClientName() {
        return clientName;
    }

    public synchronized void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public synchronized UserThread getClientThread() {
        return ClientThread;
    }

    public synchronized void setClientThread(UserThread clientThread) {
        ClientThread = clientThread;
    }
}
