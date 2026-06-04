package es.etg.dam.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class RegistroImpresion {

    private static final String FICHERO     = "impresora.log";
    private static final String FORMATO_OK  = "[%s] OK  | Tipo: %-5s | Hojas: %2d | Coste: %.2f euros | BN restantes: %2d | COLOR restantes: %2d";
    private static final String FORMATO_KO  = "[%s] KO  | Tipo: %-5s | Hojas: %2d | Motivo: sin tinta suficiente | BN restantes: %2d | COLOR restantes: %2d";

    public static synchronized void registrarOK(String tipo, int hojas, double coste, int bn, int color) {
        escribir(String.format(FORMATO_OK, LocalDateTime.now(), tipo, hojas, coste, bn, color));
    }

    public static synchronized void registrarKO(String tipo, int hojas, int bn, int color) {
        escribir(String.format(FORMATO_KO, LocalDateTime.now(), tipo, hojas, bn, color));
    }

    private static void escribir(String linea) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHERO, true))) {
            pw.println(linea);
        } catch (IOException e) {
            System.err.println("Error escribiendo log: " + e.getMessage());
        }
    }
}