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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDrawingObjects() {
        return drawingObjects;
    }

    public void setDrawingObjects(String drawingObjects) {
        this.drawingObjects = drawingObjects;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
