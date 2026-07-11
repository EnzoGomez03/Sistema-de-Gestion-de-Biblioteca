package com.enzo.biblioteca.modelo;

public class Bibliotecario extends Persona {
    private int credencial;

    public Bibliotecario(String nombre, int edad, int credencial) {
        super(nombre, edad);
        this.credencial = credencial;
    }

    public int getCredencial() { return credencial; }

    @Override
    public String toString() {
        return super.toString() + ", credencial=" + credencial;
    }
}