package com.enzo.biblioteca.util;
import java.time.LocalDate;

public final class ValidadorUtils {

    private ValidadorUtils() {
        throw new AssertionError("Esta clase no se puede instanciar.");
    }

    public static void validarString(String valor, String nombreCampo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser vacío o null.");
        }
    }

    public static void validarId(int idAValidar) {
        if (idAValidar < 0) {
            throw new IllegalArgumentException("El Id no puede ser menor a 0");
        }
    }

    public static void validarEdad(int edadAValidar){
        if(edadAValidar < 18){
            throw new IllegalArgumentException("La edad no puede ser menor a 18.");
        }
    }

    public static void validarFechas(LocalDate fechaPrestamo, LocalDate fechaLimite){
        if (fechaPrestamo == null || fechaLimite == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser null.");
        }
        if (fechaLimite.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha límite no puede ser anterior a la fecha del préstamo.");
        }
        if (fechaPrestamo.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del préstamo no puede ser en el futuro.");
        }
    }
}
