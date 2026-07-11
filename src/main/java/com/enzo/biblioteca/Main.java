package com.enzo.biblioteca;

import java.time.LocalDate;

import org.bson.Document;

import com.enzo.biblioteca.db.ConexionMongo;
import com.enzo.biblioteca.excepciones.LibroNoDisponibleException;
import com.enzo.biblioteca.excepciones.SocioConDeudaException;
import com.enzo.biblioteca.modelo.Bibliotecario;
import com.enzo.biblioteca.modelo.Libro;
import com.enzo.biblioteca.modelo.Prestamo;
import com.enzo.biblioteca.modelo.Socio;
import com.enzo.biblioteca.servicio.Biblioteca;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class Main {
    public static void main(String[] args) {

        System.out.println("===== 0. Prueba de conexión a MongoDB =====");
        MongoDatabase db = ConexionMongo.obtenerBaseDeDatos();
        System.out.println("Conexión exitosa. Base de datos: " + db.getName());

        // ============================================================
        // Instanciamos UNA sola vez cada Socio, Bibliotecario y Libro.
        // De acá en más, todos los tests reutilizan estas mismas variables.
        // ============================================================
        System.out.println("\n===== 1. Creación de Persona (herencia) =====");
        Socio enzo = new Socio("Enzo", 22, "44123456", "Calle Falsa 123", "1165308996");
        Socio marco = new Socio("Marco", 28, "40987654", "Av. Siempre Viva 742", "1122334455");
        Socio lucia = new Socio("Lucia", 30, "41122334", "San Martin 500", "1133445566");
        Bibliotecario ana = new Bibliotecario("Ana", 35, 4521);
        System.out.println(enzo);
        System.out.println(ana);

        System.out.println("\n===== 2. Creación de Libros =====");
        Libro libro1 = new Libro(1, "Cien años de soledad", "García Márquez");
        Libro libro2 = new Libro(2, "1984", "Orwell");
        Libro libroParaDevolver = new Libro(99, "El Principito", "Saint-Exupéry");
        Libro libro3 = new Libro(3, "El Aleph", "Borges");
        Libro libro4 = new Libro(4, "Rayuela", "Cortázar");
        Libro libro5 = new Libro(5, "Ficciones", "Borges");
        System.out.println(libro1);
        System.out.println(libro2);

        System.out.println("\n===== 3. Préstamo exitoso =====");
        try {
            Prestamo p1 = new Prestamo(enzo, libro1, LocalDate.now(), LocalDate.of(2026, 7, 20), 25000);
            System.out.println(p1);
            System.out.println("¿Libro1 disponible?: " + libro1.estaDisponible());
            System.out.println("¿Préstamo vencido?: " + p1.estaVencido());
        } catch (LibroNoDisponibleException | SocioConDeudaException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n===== 4. Finalizar préstamo (devolución) =====");
        try {
            Prestamo p4prueba = new Prestamo(enzo, libroParaDevolver, LocalDate.now(), LocalDate.of(2026, 7, 20), 25000);
            p4prueba.finalizar();
            System.out.println("¿Libro disponible después de devolver?: " + libroParaDevolver.estaDisponible());
        } catch (LibroNoDisponibleException | SocioConDeudaException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n===== 5. Excepción: libro ya prestado =====");
        //Aca reusamos "marco", que en este punto todavía NO tiene deuda
        // bien y lo que falla es el segundo intento sobre el mismo libro.
        try {
            Prestamo p2 = new Prestamo(enzo, libro2, LocalDate.now(), LocalDate.of(2026, 7, 20), 25000);
            Prestamo p3 = new Prestamo(marco, libro2, LocalDate.now(), LocalDate.of(2026, 7, 20), 25000);
        } catch (LibroNoDisponibleException e) {
            System.out.println("Se detectó correctamente: " + e.getMessage());
        } catch (SocioConDeudaException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n===== 6. Excepción: socio con deuda =====");
        // A partir de acá, "marco" queda con $5000 de deuda para el resto del programa.
        try {
            marco.agregarDeuda(5000);
            Prestamo p4 = new Prestamo(marco, libro3, LocalDate.now(), LocalDate.of(2026, 7, 20), 25000);
        } catch (SocioConDeudaException e) {
            System.out.println("Se detectó correctamente: " + e.getMessage());
        } catch (LibroNoDisponibleException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n===== 7. Validación: valor de préstamo muy bajo =====");
        try {
            Prestamo p5 = new Prestamo(enzo, libro4, LocalDate.now(), LocalDate.of(2026, 7, 20), 5000);
        } catch (IllegalArgumentException e) {
            System.out.println("Se detectó correctamente: " + e.getMessage());
        } catch (LibroNoDisponibleException | SocioConDeudaException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n===== 8. Validación: fecha límite anterior a fecha de préstamo =====");
        try {
            Prestamo p6 = new Prestamo(enzo, libro5, LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 5), 25000);
        } catch (IllegalArgumentException e) {
            System.out.println("Se detectó correctamente: " + e.getMessage());
        } catch (LibroNoDisponibleException | SocioConDeudaException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }

        System.out.println("\n===== 9. Validación: campo vacío en Libro =====");
        try {
            Libro libroInvalido = new Libro(6, "", "Autor Random");
        } catch (IllegalArgumentException e) {
            System.out.println("Se detectó correctamente: " + e.getMessage());
        }

        System.out.println("\n===== 10. Validación: id negativo en Libro =====");
        try {
            Libro libroInvalido = new Libro(-1, "Un titulo", "Un autor");
        } catch (IllegalArgumentException e) {
            System.out.println("Se detectó correctamente: " + e.getMessage());
        }

        System.out.println("\n===== 11. Biblioteca: registrar préstamo =====");
        // libro1 ya está prestado desde el test 3 y nunca se devolvió acá,
        // así que este intento correctamente tira "no disponible".
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.agregarSocio(enzo);
        biblioteca.agregarSocio(marco);
        biblioteca.agregarSocio(lucia);
        biblioteca.agregarLibro(libro1);

        try {
            Prestamo p = biblioteca.registrarPrestamo(enzo, libro1, LocalDate.now(), LocalDate.of(2026, 7, 20), 25000);
            System.out.println(p);
            System.out.println("Libros disponibles: " + biblioteca.listarLibrosDisponibles());
        } catch (LibroNoDisponibleException | SocioConDeudaException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("\n===== 12. Comparator =====");
        // marco ya tiene $5000 de deuda (del test 6). Le sumamos deuda a lucia acá.
        lucia.agregarDeuda(8000);

        Libro rayuela = new Libro(10, "Rayuela", "Cortázar");
        Libro aleph = new Libro(11, "El Aleph", "Borges");
        Libro cienAnios = new Libro(12, "Cien años de soledad", "García Márquez");

        biblioteca.agregarLibro(rayuela);
        biblioteca.agregarLibro(aleph);
        biblioteca.agregarLibro(cienAnios);

        System.out.println("\nLibros ordenados por título (alfabético):");
        for (Libro l : biblioteca.listarLibrosOrdenadosPorTitulo()) {
            System.out.println(l);
        }

        System.out.println("\nSocios ordenados por deuda (mayor a menor):");
        for (Socio s : biblioteca.listarSociosPorDeuda()) {
            System.out.println(s);
        }

        // ============================================================
        // ===== MongoDB: upsert por DNI (no duplica si ya existe) =====
        // ============================================================
        System.out.println("\n===== 13. Upsert de socios en MongoDB =====");
        MongoCollection<Document> coleccionSocios = db.getCollection("socios");

        for (Socio s : new Socio[]{enzo, marco, lucia}) {
            UpdateResult resultado = coleccionSocios.updateOne(
                    Filters.eq("dni", s.getDni()),
                    Updates.combine(
                            Updates.set("nombre", s.getNombre()),
                            Updates.set("edad", s.getEdad()),
                            Updates.set("direccion", s.getDireccion()),
                            Updates.set("nroTelefono", s.getNroTelefono()),
                            Updates.set("deuda", s.getDeuda())
                    ),
                    new UpdateOptions().upsert(true)
            );

            if (resultado.getUpsertedId() != null) {
                System.out.println("Socio nuevo insertado (dni=" + s.getDni() + "): " + resultado.getUpsertedId());
            } else {
                System.out.println("Socio existente actualizado (dni=" + s.getDni() + "). Modificados: " + resultado.getModifiedCount());
            }
        }

        // ----- Traer todos los socios -----
        System.out.println("\n--- Todos los socios en MongoDB ---");
        FindIterable<Document> todosLosSocios = coleccionSocios.find();
        for (Document doc : todosLosSocios) {
            System.out.println(doc.toJson());
        }

        // ----- Buscar un socio puntual por dni -----
        System.out.println("\n--- Buscar socio por dni ---");
        Document socioEncontrado = coleccionSocios.find(Filters.eq("dni", enzo.getDni())).first();
        if (socioEncontrado != null) {
            System.out.println("Encontrado: " + socioEncontrado.toJson());
            System.out.println("Deuda: " + socioEncontrado.getInteger("deuda"));
        } else {
            System.out.println("No se encontró ningún socio con ese dni.");
        }

        // ----- Eliminar un socio descartable -----
        System.out.println("\n--- Eliminar socio ---");
        Document socioDescartable = new Document("dni", "00000000")
                .append("nombre", "Prueba Temporal")
                .append("edad", 99)
                .append("direccion", "Ninguna")
                .append("nroTelefono", "0000000000")
                .append("deuda", 0);

        coleccionSocios.insertOne(socioDescartable);
        System.out.println("Insertado socio temporal para poder borrarlo después.");

        DeleteResult resultadoBorrado = coleccionSocios.deleteOne(Filters.eq("dni", "00000000"));
        System.out.println("Documentos eliminados: " + resultadoBorrado.getDeletedCount());

        Document buscarBorrado = coleccionSocios.find(Filters.eq("dni", "00000000")).first();
        System.out.println("¿Se encuentra después de borrar?: " + (buscarBorrado != null));

        ConexionMongo.cerrarConexion();
    }
}