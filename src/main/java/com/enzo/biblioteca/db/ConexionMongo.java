package com.enzo.biblioteca.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongo {

    private static final String NOMBRE_BASE_DATOS = "biblioteca";

    private static MongoClient cliente;

    private ConexionMongo() {
        throw new AssertionError("Esta clase no se puede instanciar.");
    }

    public static MongoDatabase obtenerBaseDeDatos() {
        if (cliente == null) {
            String uri = System.getenv("MONGO_URI");

            if (uri == null || uri.isBlank()) {
                throw new IllegalStateException("La variable de entorno MONGO_URI no está configurada.");
            }

            cliente = MongoClients.create(uri);
        }

        return cliente.getDatabase(NOMBRE_BASE_DATOS);
    }

    public static void cerrarConexion() {
        if (cliente != null) {
            cliente.close();
            cliente = null;
        }
    }
}