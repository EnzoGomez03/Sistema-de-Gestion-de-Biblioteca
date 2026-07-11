package com.enzo.biblioteca.servicio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.enzo.biblioteca.excepciones.LibroNoDisponibleException;
import com.enzo.biblioteca.excepciones.SocioConDeudaException;
import com.enzo.biblioteca.modelo.Libro;
import com.enzo.biblioteca.modelo.Prestamo;
import com.enzo.biblioteca.modelo.Socio;

public class Biblioteca {
    private List<Socio> socios;
    private List<Libro> libros;
    private List<Prestamo> prestamos;

    public Biblioteca() {
        this.socios = new ArrayList<>();
        this.libros = new ArrayList<>();
        this.prestamos = new ArrayList<>();
    }

    // ---------- Altas ----------

    public void agregarSocio(Socio socio) {
        socios.add(socio);
    }

    public void agregarLibro(Libro libro) {
        libros.add(libro);
    }

    // ---------- Operación central: registrar préstamo ----------

    public Prestamo registrarPrestamo(Socio socio, Libro libro, LocalDate fechaPrestamo, LocalDate fechaLimite, int valor)throws LibroNoDisponibleException, SocioConDeudaException {

        if (!libros.contains(libro)) {
            throw new IllegalArgumentException("El libro no pertenece a esta biblioteca.");
        }
        if (!socios.contains(socio)) {
            throw new IllegalArgumentException("El socio no está registrado en esta biblioteca.");
        }

        Prestamo prestamo = new Prestamo(socio, libro, fechaPrestamo, fechaLimite, valor);
        prestamos.add(prestamo);
        return prestamo;
    }

    public void registrarDevolucion(Prestamo prestamo) {
        prestamo.finalizar();
    }

    // ---------- Búsquedas ----------

    public Libro buscarLibroPorTitulo(String titulo) {
        for (Libro l : libros) {
            if (l.getTitulo().equalsIgnoreCase(titulo)) {
                return l;
            }
        }
        return null;
    }

    public List<Libro> listarLibrosDisponibles() {
        List<Libro> disponibles = new ArrayList<>();
        for (Libro l : libros) {
            if (l.estaDisponible()) {
                disponibles.add(l);
            }
        }
        return disponibles;
    }

    public List<Prestamo> listarPrestamosVencidos() {
        List<Prestamo> vencidos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.estaVencido()) {
                vencidos.add(p);
            }
        }
        return vencidos;
    }

    // ---------- Getters ----------

    public List<Socio> getSocios() {
        return socios;
    }
    
    public List<Libro> getLibros() {
        return libros;
    }
    
    public List<Prestamo> getPrestamos() {
        return prestamos;
    }


    //---------Ordenamiento----------------

    public List<Libro> listarLibrosOrdenadosPorTitulo() {
        List<Libro> copia = new ArrayList<>(libros);
        copia.sort(Comparator.comparing(Libro::getTitulo));
        return copia;
    }

    //Ordenamiento de socios por deuda de menor a mayor
    public List<Socio> listarSociosPorDeuda() {
        List<Socio> copia = new ArrayList<>(socios);
        copia.sort(Comparator.comparingInt(Socio::getDeuda).reversed());
        return copia;
    }

    public List<Prestamo> listarPrestamosPorFechaLimite() {
        List<Prestamo> copia = new ArrayList<>(prestamos);
        copia.sort(Comparator.comparing(Prestamo::getFechaLimite));
        return copia;
    }

}