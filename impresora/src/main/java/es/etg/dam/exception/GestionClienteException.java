package es.etg.dam.exception;

public class GestionClienteException extends RuntimeException {

    private static final String MSG = "Error gestionando cliente: %s";

    public GestionClienteException(String detalle) {
        super(String.format(MSG, detalle));
    }

    public GestionClienteException(String detalle, Throwable causa) {
        super(String.format(MSG, detalle), causa);
    }
}
