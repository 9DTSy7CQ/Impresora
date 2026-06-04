package es.etg.dam;

import es.etg.dam.exception.GestionClienteException;
import java.net.Socket;

public class ClienteHandler implements Runnable {

    private final Socket    socket;
    private final Impresora impresora;

    public static final String MSG_KO          = "KO";
    public static final int    PARTES_ESPERADAS = 2;
    public static final String SPLITTER         = " ";
    public static final int    INDEX_TIPO        = 0;
    public static final int    INDEX_HOJAS       = 1;

    public ClienteHandler(Socket socket, Impresora impresora) {
        this.socket    = socket;
        this.impresora = impresora;
    }

    @Override
    public void run() {
        try {
            String   peticion = Conexion.recibir(socket);
            String[] partes   = peticion.split(SPLITTER);

            if (partes.length != PARTES_ESPERADAS) {
                Conexion.enviar(MSG_KO, socket);
                return;
            }

            String tipo  = partes[INDEX_TIPO].toUpperCase();
            int    hojas = Integer.parseInt(partes[INDEX_HOJAS]);

            String respuesta = impresora.imprimir(tipo, hojas);
            Conexion.enviar(respuesta, socket);

        } catch (Exception e) {
            try { Conexion.enviar(MSG_KO, socket); } catch (Exception ignored) {}
            throw new GestionClienteException(e.getMessage(), e);
        } finally {
            try { socket.close(); } catch (Exception ignored) {}
        }
    }
}