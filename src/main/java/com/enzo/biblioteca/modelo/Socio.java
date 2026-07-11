package com.enzo.biblioteca.modelo;

import com.enzo.biblioteca.util.ValidadorUtils;

public class Socio extends Persona {
    private String direccion;
    private String nroTelefono;
    private int deuda;
    private String dni;

    public Socio(String nombre, int edad,String dni ,String direccion, String nroTelefono) {
        super(nombre, edad);
        ValidadorUtils.validarString(direccion, "direccion");
        ValidadorUtils.validarString(nroTelefono, "nroTelefono");
        ValidadorUtils.validarString(dni, "dni");
        this.dni = dni;
        this.direccion = direccion;
        this.nroTelefono = nroTelefono;
        this.deuda = 0; //Un cliente nuevo arranca sin deuda
    }

    public String getDni(){
        return dni;
    }


    public String getDireccion() {
        return direccion; 
    }
    
    public String getNroTelefono() {
        return nroTelefono;
    }

    public int getDeuda(){
        return deuda;
    }

    public void agregarDeuda(int monto) {
        this.deuda += monto;
    }

    public void pagarDeuda(int monto) {
        this.deuda -= monto;
    }

    public boolean tieneDeuda() {
        return deuda > 0;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", dni=" + dni +
                ", direccion=" + direccion +
                ", nroTelefono=" + nroTelefono;
    }
}