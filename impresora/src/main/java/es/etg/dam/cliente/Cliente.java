package es.etg.dam.cliente;

import es.etg.dam.Conexion;
import es.etg.dam.exception.ClienteException;
import es.etg.dam.servidor.Servidor;
import es.etg.dam.util.LogUtil;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {

    private static final String MSG_USO       = "Uso: java Cliente \"BN 5\" | \"COLOR 3\"";
    private static final String MSG_RESPUESTA = "Servidor responde: %s";
    private static final String MSG_ENVIANDO  = "Enviando peticion: %s";
    private static final String FICHERO_LOG   = "cliente.log";

    public static void main(String[] args) throws ClienteException {

        if (args.length != 1) {
            System.out.println(MSG_USO);
            return;
        }

        Logger logger = null;

        try (Socket socket = new Socket("localhost", Servidor.PUERTO)) {

            logger = LogUtil.crearLog(FICHERO_LOG);
            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_ENVIANDO, args[0].trim()));

            Conexion.enviar(args[0].trim(), socket);
            String respuesta = Conexion.recibir(socket);

            LogUtil.escribirLog(logger, Level.INFO, String.format(MSG_RESPUESTA, respuesta));
            System.out.println(String.format(MSG_RESPUESTA, respuesta));

        } catch (Exception e) {
            if (logger != null) LogUtil.escribirLog(logger, Level.SEVERE, e.getMessage(), e);
            throw new ClienteException(e.getMessage(), e);
        }
    }
}