package es.etg.dam.cliente;

import es.etg.dam.Conexion;
import es.etg.dam.exception.ClienteException;
import es.etg.dam.servidor.Servidor;

import java.net.Socket;

public class Cliente {

    private static final String MSG_USO       = "Uso: java Cliente \"BN 5\" | \"COLOR 3\"";
    private static final String MSG_RESPUESTA = "Servidor responde: %s";

    public static void main(String[] args) throws ClienteException {

        if (args.length != 1) {
            System.out.println(MSG_USO);
            return;
        }

        try (Socket socket = new Socket("localhost", Servidor.PUERTO)) {
            Conexion.enviar(args[0].trim(), socket);
            String respuesta = Conexion.recibir(socket);
            System.out.println(String.format(MSG_RESPUESTA, respuesta));

        } catch (Exception e) {
            throw new ClienteException(e.getMessage(), e);
        }
    }
}