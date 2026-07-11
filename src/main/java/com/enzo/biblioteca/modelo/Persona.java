package com.enzo.biblioteca.modelo;

import com.enzo.biblioteca.util.ValidadorUtils;

public abstract class Persona {
    private String nombre;
    private int edad;

    public Persona(String nombre, int edad) {
        ValidadorUtils.validarString(nombre, "nombre");
        ValidadorUtils.validarEdad(edad);
        this.nombre = nombre;
        this.edad = edad;
    }


    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return "nombre=" + nombre + ", edad=" + edad;
    }
    
}
