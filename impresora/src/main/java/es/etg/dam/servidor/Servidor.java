package es.etg.dam.servidor;

import es.etg.dam.ClienteHandler;
import es.etg.dam.exception.ServidorException;
import es.etg.dam.util.LogUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static final int PUERTO = 8080;
    public static final String MSG_ESCUCHA = "Servidor iniciado en puerto %d";
    public static final String MSG_CLIENTE = "Cliente conectado: %s";
    public static final String FICHERO_LOG = "servidor.log";

    public static void main(String[] args) throws ServidorException {

        Tinta tinta = new Tinta();
        Impresora impresora = new Impresora(tinta);
        Logger logger;

        try {
            logger = LogUtil.crearLog(FICHERO_LOG);
        } catch (Exception e) {
            throw new ServidorException(e.getMessage(), e);
        }

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {

            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_ESCUCHA, PUERTO));
            System.out.println(String.format(MSG_ESCUCHA, PUERTO));

            while (true) {
                Socket socket = serverSocket.accept();
                LogUtil.escribirLog(logger, Level.INFO,
                        String.format(MSG_CLIENTE, socket.getInetAddress()));
                Thread hilo = new Thread(new ClienteHandler(socket, impresora, logger));
                hilo.start();
            }

        } catch (Exception e) {
            LogUtil.escribirLog(logger, Level.SEVERE, e.getMessage(), e);
            throw new ServidorException(e.getMessage(), e);
        }
    }
}
