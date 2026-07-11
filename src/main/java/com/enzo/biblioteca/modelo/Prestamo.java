package com.enzo.biblioteca.modelo;
import java.time.LocalDate;

import com.enzo.biblioteca.excepciones.LibroNoDisponibleException;
import com.enzo.biblioteca.excepciones.SocioConDeudaException;
import com.enzo.biblioteca.util.ValidadorUtils;



public class Prestamo {
    private Socio socio;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaLimite;
    private int valor;

    public Prestamo(Socio socio, Libro libro, LocalDate fechaPrestamo, LocalDate fechaLimite, int valor)throws LibroNoDisponibleException, SocioConDeudaException {
        validarDeuda(socio);
        ValidadorUtils.validarFechas(fechaPrestamo, fechaLimite);
        validarValor(valor);
        this.socio = socio;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.valor = valor;

        libro.prestar(); // al crear el préstamo, el libro pasa a "no disponible"
    }

    private void validarValor(int valorAValidar){
        if(valorAValidar < 20_000){
            throw new IllegalArgumentException("El valor del prestamo no puede ser menor a 20.000");
        }
    }

    private void validarDeuda(Socio socioAValidar) throws SocioConDeudaException{
        if (socioAValidar.tieneDeuda()) {
        throw new SocioConDeudaException("El socio '" + socioAValidar.getNombre() + "' tiene una deuda de $" + socioAValidar.getDeuda() + " y no puede pedir más libros.");
        }
    }

    public Socio getSocio() {
        return socio; 
    }

    public Libro getLibro() {
        return libro; 
    }


    public LocalDate getFechaPrestamo() {
        return fechaPrestamo; 
    }

    public LocalDate getFechaLimite() {
        return fechaLimite; 
    }

    public int getValor() {
        return valor; 
    }

    public boolean estaVencido(){
        return LocalDate.now().isAfter(fechaLimite);
    }

    public void finalizar() {
        libro.devolver(); // al finalizar el préstamo, el libro vuelve a estar disponible
    }

    @Override
    public String toString() {
        return "socio=" + socio.getNombre() +
                ", libro=" + libro.getTitulo() +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaLimite=" + fechaLimite +
                ", valor=" + valor;
    }
}