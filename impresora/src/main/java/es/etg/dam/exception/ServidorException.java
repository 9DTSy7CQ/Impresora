package es.etg.dam.exception;

public class ServidorException extends Exception {

    private static final String MSG = "Error en el servidor: %s";

    public ServidorException(String detalle) {
        super(String.format(MSG, detalle));
    }

    public ServidorException(String detalle, Throwable causa) {
        super(String.format(MSG, detalle), causa);
    }
}