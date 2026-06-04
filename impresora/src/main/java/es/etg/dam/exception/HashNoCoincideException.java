package es.etg.dam.exception;

public class HashNoCoincideException extends RuntimeException {

    private static final String MSG = "Hash no coincide para el mensaje: %s";

    public HashNoCoincideException(String mensaje) {
        super(String.format(MSG, mensaje));
    }

    public HashNoCoincideException(String mensaje, Throwable causa) {
        super(String.format(MSG, mensaje), causa);
    }
}