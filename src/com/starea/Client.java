package com.starea;

public class Client {
    private volatile String clientName;
    private volatile UserThread ClientThread;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public UserThread getClientThread() {
        return ClientThread;
    }

    public void setClientThread(UserThread clientThread) {
        ClientThread = clientThread;
    }
}
