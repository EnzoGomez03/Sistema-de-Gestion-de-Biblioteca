package com.enzo.biblioteca.db;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

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
            crearIndicesUnicos(cliente.getDatabase(NOMBRE_BASE_DATOS));
        }

        return cliente.getDatabase(NOMBRE_BASE_DATOS);
    }

    private static void crearIndicesUnicos(MongoDatabase db) {
        MongoCollection<Document> socios = db.getCollection("socios");
        socios.createIndex(Indexes.ascending("dni"), new IndexOptions().unique(true));
    }

    public static void cerrarConexion() {
        if (cliente != null) {
            cliente.close();
            cliente = null;
        }
    }
}