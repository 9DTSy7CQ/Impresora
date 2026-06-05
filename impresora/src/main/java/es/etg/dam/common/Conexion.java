package es.etg.dam.common;

import es.etg.dam.exception.HashNoCoincideException;
import es.etg.dam.util.CifradoUtil;
import es.etg.dam.util.HashUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Conexion {

    private static final String MSG_HASH = "Hash no coincide para el mensaje: %s";

    public static void enviar(String mensaje, Socket socket) throws Exception {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        String hash = HashUtil.convertirSHA256(mensaje);
        String cifrado = CifradoUtil.cifrar(mensaje, CifradoUtil.PASS);
        out.writeUTF(hash);
        out.writeUTF(cifrado);
        out.flush();
    }

    public static String recibir(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        String hashRecibido = in.readUTF();
        String mensajeCifrado = in.readUTF();
        String mensaje = CifradoUtil.descifrar(mensajeCifrado, CifradoUtil.PASS);
        String hashCalculado = HashUtil.convertirSHA256(mensaje);

        if (!hashRecibido.equals(hashCalculado)) {
            throw new HashNoCoincideException(String.format(MSG_HASH, mensaje));
        }
        return mensaje;
    }
}
