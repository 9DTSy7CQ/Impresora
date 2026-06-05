package es.etg.dam;

import es.etg.dam.common.Conexion;
import es.etg.dam.exception.GestionClienteException;
import es.etg.dam.servidor.Impresora;
import es.etg.dam.util.LogUtil;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteHandler implements Runnable {

    private final Socket socket;
    private final Impresora impresora;
    private final Logger logger;

    public static final String MSG_KO = "KO";
    public static final int PARTES_ESPERADAS = 2;
    public static final String SPLITTER = " ";
    public static final int INDEX_TIPO = 0;
    public static final int INDEX_HOJAS = 1;
    public static final String MSG_PETICION = "Petición recibida: %s";
    public static final String MSG_RESPUESTA = "Respuesta enviada: %s";
    public static final String MSG_ERROR = "Error gestionando cliente: %s";
    public static final String MSG_FORMATO_KO = "Formato incorrecto: %s";

    public ClienteHandler(Socket socket, Impresora impresora, Logger logger) {
        this.socket = socket;
        this.impresora = impresora;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            String peticion = Conexion.recibir(socket);
            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_PETICION, peticion));

            String[] partes = peticion.split(SPLITTER);

            if (partes.length != PARTES_ESPERADAS) {
                LogUtil.escribirLog(logger, Level.WARNING, String.format(MSG_FORMATO_KO, peticion));
                throw new GestionClienteException(String.format(MSG_FORMATO_KO, peticion));
            }

            String tipo = partes[INDEX_TIPO].toUpperCase();
            int hojas = Integer.parseInt(partes[INDEX_HOJAS]);

            String respuesta = impresora.imprimir(tipo, hojas);
            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_RESPUESTA, respuesta));
            Conexion.enviar(respuesta, socket);

        } catch (GestionClienteException e) {
            try {
                Conexion.enviar(MSG_KO, socket);
            } catch (Exception ignored) {
            }
            throw e;
        } catch (Exception e) {
            try {
                Conexion.enviar(MSG_KO, socket);
            } catch (Exception ignored) {
            }
            throw new GestionClienteException(e.getMessage(), e);
        } finally {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }
}
