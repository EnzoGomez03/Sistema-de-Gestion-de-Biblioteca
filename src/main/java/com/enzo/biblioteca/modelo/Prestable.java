package com.enzo.biblioteca.modelo;

import com.enzo.biblioteca.excepciones.LibroNoDisponibleException;

public interface Prestable {
    
    void prestar() throws LibroNoDisponibleException;

    void devolver();

    boolean estaDisponible();

}
