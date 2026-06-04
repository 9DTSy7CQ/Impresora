package es.etg.dam.util;

import java.security.MessageDigest;

public class HashUtil {

    private static final String ALGORITMO   = "SHA-256";
    private static final String CODIFICACION = "UTF-8";

    public static String convertirSHA256(String cadena) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITMO);
        byte[] hash = md.digest(cadena.getBytes(CODIFICACION));

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}