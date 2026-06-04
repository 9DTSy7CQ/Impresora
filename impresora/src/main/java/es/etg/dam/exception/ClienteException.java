package es.etg.dam.exception;

public class ClienteException extends Exception {

    private static final String MSG = "Error en el cliente: %s";

    public ClienteException(String detalle) {
        super(String.format(MSG, detalle));
    }

    public ClienteException(String detalle, Throwable causa) {
        super(String.format(MSG, detalle), causa);
    }
}