package es.etg.dam.util;

import java.security.MessageDigest;
import java.util.Base64;

public class HashUtil {

    private static final String ALGORITMO = "SHA-256";
    private static final String CODIFICACION = "UTF-8";

    public static String convertirSHA256(String cadena) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITMO);
        byte[] hash = md.digest(cadena.getBytes(CODIFICACION));
        return Base64.getEncoder().encodeToString(hash);
    }
}
