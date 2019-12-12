package com.starea;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Datasource {
    private volatile LinkedList<Artboard> artboards;
    private volatile LinkedList<Client> connectedClients;
    private static Datasource instance = new Datasource();

    public static Datasource getInstance() {
        return instance;
    }

    public Datasource() {
        this.artboards = new LinkedList<>();
        this.connectedClients = new LinkedList<>();
    }

    public LinkedList<Artboard> getArtboards() {
        return artboards;
    }

    public void setArtboards(LinkedList<Artboard> artboards) {
        this.artboards = artboards;
    }

    public LinkedList<Client> getConnectedClients() {
        return connectedClients;
    }

    public void setConnectedClients(LinkedList<Client> connectedClients) {
        this.connectedClients = connectedClients;
    }
}
