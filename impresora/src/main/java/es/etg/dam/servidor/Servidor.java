package es.etg.dam.servidor;

import es.etg.dam.ClienteHandler;
import es.etg.dam.Impresora;
import es.etg.dam.Tinta;
import es.etg.dam.exception.ServidorException;

import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int    PUERTO      = 8080;
    public static final String MSG_ESCUCHA = "Servidor iniciado. Esperando clientes en puerto %d...";

    public static void main(String[] args) throws ServidorException {

        Tinta     tinta     = new Tinta();
        Impresora impresora = new Impresora(tinta);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {

            System.out.println(String.format(MSG_ESCUCHA, PUERTO));

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClienteHandler(socket, impresora)).start();
            }

        } catch (Exception e) {
            throw new ServidorException(e.getMessage(), e);
        }
    }
}