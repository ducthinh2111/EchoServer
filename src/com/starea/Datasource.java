package com.starea;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Datasource {
    private static volatile LinkedList<Artboard> artboards = new LinkedList<>();
    private static volatile LinkedList<Client> connectedClients = new LinkedList<>();
    private static volatile Datasource instance;

    public static Datasource getInstance() {
        if(instance == null) {
            synchronized (Datasource.class) {
                if(instance == null) {
                    instance = new Datasource();
                }
            }
        }
        return instance;
    }

    public synchronized LinkedList<Artboard> getArtboards() {
        return artboards;
    }

    public synchronized LinkedList<Client> getConnectedClients() {
        return connectedClients;
    }
}
