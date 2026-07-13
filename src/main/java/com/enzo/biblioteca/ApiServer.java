package com.enzo.biblioteca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;


import com.enzo.biblioteca.db.ConexionMongo;
import com.enzo.biblioteca.modelo.Socio;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.javalin.Javalin;

public class ApiServer {
    public static void main(String[] args) {
        //Arranca el servidor, escuchando en el puerto 7070 de mi compu.
        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> it.anyHost());
            });
        }).start(7070);

        //Registra un endpoint:"Cuando alguien pida GET a la URL raiz, ejecuta esta funcion."
        app.get("/", ctx -> {
            ctx.result("¡Funciona! El servidor está corriendo.");
        });

        app.get("/socios", ctx -> {
            MongoDatabase db = ConexionMongo.obtenerBaseDeDatos();
            MongoCollection<Document> coleccion = db.getCollection("socios");

            List<Map<String, Object>> socios = new ArrayList<>();

            for (Document doc : coleccion.find()) {
                Map<String, Object> socio = new HashMap<>();
                socio.put("dni", doc.getString("dni"));
                socio.put("nombre", doc.getString("nombre"));
                socio.put("edad", doc.getInteger("edad"));
                socio.put("direccion", doc.getString("direccion"));
                socio.put("nroTelefono", doc.getString("nroTelefono"));
                socio.put("deuda", doc.getInteger("deuda"));
                socios.add(socio);
            }

            ctx.json(socios);
        });


        app.post("/socios", ctx -> {
            Map<String, Object> datos = ctx.bodyAsClass(Map.class);

            try {
                String dni = (String) datos.get("dni");
                String nombre = (String) datos.get("nombre");
                int edad = ((Number) datos.get("edad")).intValue();
                String direccion = (String) datos.get("direccion");
                String nroTelefono = (String) datos.get("nroTelefono");

                Socio socio = new Socio(nombre, edad, dni, direccion, nroTelefono);

                MongoDatabase db = ConexionMongo.obtenerBaseDeDatos();
                MongoCollection<Document> coleccion = db.getCollection("socios");

                Document doc = new Document("dni", socio.getDni())
                        .append("nombre", socio.getNombre())
                        .append("edad", socio.getEdad())
                        .append("direccion", socio.getDireccion())
                        .append("nroTelefono", socio.getNroTelefono())
                        .append("deuda", socio.getDeuda());

                coleccion.insertOne(doc);

                ctx.status(201);
                ctx.json(doc);

            } catch (com.mongodb.MongoWriteException e) {
                ctx.status(409);
                ctx.json(Map.of("error", "Ya existe un socio con ese DNI."));
            } catch (IllegalArgumentException e) {
                ctx.status(400);//codigo de pedido mal formado, basicamente se le informa al cliente que mando algo mal.
                ctx.json(Map.of("error", e.getMessage()));
            }
        });

        System.out.println("Servidor corriendo en http://localhost:7070");
    }
}