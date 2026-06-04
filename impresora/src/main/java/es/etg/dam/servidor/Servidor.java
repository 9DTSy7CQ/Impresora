package es.etg.dam.servidor;

import es.etg.dam.ClienteHandler;
import es.etg.dam.Impresora;
import es.etg.dam.Tinta;
import es.etg.dam.exception.ServidorException;
import es.etg.dam.util.LogUtil;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {

    public static final int    PUERTO          = 8080;
    public static final String MSG_ESCUCHA     = "Servidor iniciado en puerto %d";
    public static final String MSG_CLIENTE     = "Cliente conectado: %s";
    public static final String FICHERO_LOG     = "servidor.log";

    public static void main(String[] args) throws ServidorException {

        Logger logger = null;
        Tinta     tinta     = new Tinta();
        Impresora impresora = new Impresora(tinta);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {

            logger = LogUtil.crearLog(FICHERO_LOG);
            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_ESCUCHA, PUERTO));
            System.out.println(String.format(MSG_ESCUCHA, PUERTO));

            while (true) {
                Socket socket = serverSocket.accept();
                LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_CLIENTE, socket.getInetAddress()));
                new Thread(new ClienteHandler(socket, impresora, logger)).start();
            }

        } catch (Exception e) {
            if (logger != null) LogUtil.escribirLog(logger, Level.SEVERE, e.getMessage(), e);
            throw new ServidorException(e.getMessage(), e);
        }
    }
}