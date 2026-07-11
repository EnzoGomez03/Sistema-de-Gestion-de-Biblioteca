package com.enzo.biblioteca.modelo;

import com.enzo.biblioteca.excepciones.LibroNoDisponibleException;
import com.enzo.biblioteca.util.ValidadorUtils;

/**
 * libro
 */
public class Libro implements Prestable {
    
    private int id;
    private String titulo;
    private String autor;
    private boolean disponible;


    public Libro(int id, String titulo, String autor){
        ValidadorUtils.validarString(titulo,"titulo");
        ValidadorUtils.validarId(id);
        ValidadorUtils.validarString(autor, "autor");
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.disponible = true; //Como es un libro nuevo,arranca en true
    }

    @Override
    public void prestar() throws LibroNoDisponibleException{

        if(!disponible){
            throw new LibroNoDisponibleException("El libro '" + titulo + "' ya está prestado.");
        }
        this.disponible = false;

    }


    @Override
    public boolean estaDisponible() {
        return disponible;
    }

    public int getId() {
        return id;
    }
    
    public String getTitulo() {
        return titulo;
    }


    public String getAutor() {
        return autor; 
    }



    @Override
    public String toString() {
        String estado = disponible ? "Disponible" : "Prestado";
        return "id=" + id +
            ", titulo=" + titulo +
            ", autor=" + autor +
            ", estado=" + estado;
}


    @Override
    public void devolver() {
        this.disponible = true;
    }



}
