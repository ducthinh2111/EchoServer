package com.starea;

import java.util.ArrayList;
import java.util.List;

public class Artboard {

    private volatile String code;
    private volatile String drawingObjects;
    private volatile List<Client> clients;

    public Artboard() {
        this.clients = new ArrayList<>();
    }

    public synchronized String getCode() {
        return code;
    }

    public synchronized void setCode(String code) {
        this.code = code;
    }

    public synchronized String getDrawingObjects() {
        return drawingObjects;
    }

    public synchronized void setDrawingObjects(String drawingObjects) {
        this.drawingObjects = drawingObjects;
    }

    public synchronized List<Client> getClients() {
        return clients;
    }
}
