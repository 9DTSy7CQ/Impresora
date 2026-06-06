package es.etg.dam.cliente;

import es.etg.dam.common.Conexion;
import es.etg.dam.exception.ClienteException;
import es.etg.dam.servidor.Servidor;
import es.etg.dam.util.LogUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private static final String MSG_USO       = "Uso: java Cliente \"BN 5\" | \"COLOR 3\"";
    private static final String MSG_RESPUESTA = "Servidor responde: %s";
    private static final String MSG_ENVIANDO  = "Enviando peticion: %s";
    private static final String FICHERO_LOG   = "cliente.log";
    private static final int    INDEX_PETICION = 0;

    public static void main(String[] args) throws ClienteException {

        Logger logger;

        try {
            logger = LogUtil.crearLog(FICHERO_LOG);
        } catch (IOException e) {
            throw new ClienteException(e.getMessage(), e);
        }

        try (Socket socket = new Socket(Servidor.HOST, Servidor.PUERTO)) {

            String peticion = args[INDEX_PETICION].trim();

            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_ENVIANDO, peticion));

            Conexion.enviar(peticion, socket);
            String respuesta = Conexion.recibir(socket);

            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_RESPUESTA, respuesta));
            System.out.println(String.format(MSG_RESPUESTA, respuesta));

        } catch (Exception e) {
            LogUtil.escribirLog(logger, Level.SEVERE, e.getMessage(), e);
            throw new ClienteException(e.getMessage(), e);
        }
    }
}