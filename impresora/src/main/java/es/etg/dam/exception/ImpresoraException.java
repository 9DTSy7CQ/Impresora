package es.etg.dam.exception;

public class ImpresoraException extends Exception {

    private static final String MSG = "Error en la impresora: %s";

    public ImpresoraException(String detalle) {
        super(String.format(MSG, detalle));
    }

    public ImpresoraException(String detalle, Throwable causa) {
        super(String.format(MSG, detalle), causa);
    }
}