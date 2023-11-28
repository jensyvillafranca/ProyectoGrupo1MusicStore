package com.example.proyectogrupo1musicstore.Adapters;

public class AppData {
    private static AppData instance;
    private String id;

    private AppData() {
        // Constructor privado para evitar instanciación directa
    }

    public static synchronized AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
