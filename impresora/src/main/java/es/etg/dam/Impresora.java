package es.etg.dam;

import es.etg.dam.util.RegistroImpresion;

public class Impresora {

    public static final double PRECIO_BN    = 0.5;
    public static final double PRECIO_COLOR = 1.0;

    public static final String TIPO_BN    = "BN";
    public static final String TIPO_COLOR = "COLOR";

    public static final String MSG_OK = "OK %.2f euros | Hojas restantes: BN=%d, COLOR=%d";
    public static final String MSG_KO = "KO | Hojas restantes: BN=%d, COLOR=%d";

    private final Tinta tinta;

    public Impresora(Tinta tinta) {
        this.tinta = tinta;
    }

    public String imprimir(String tipo, int hojas) {

        boolean exito  = false;
        double  precio = 0.0;

        if (TIPO_BN.equals(tipo)) {
            exito  = tinta.consumirBN(hojas);
            precio = PRECIO_BN * hojas;

        } else if (TIPO_COLOR.equals(tipo)) {
            exito  = tinta.consumirColor(hojas);
            precio = PRECIO_COLOR * hojas;
        }

        if (exito) {
            RegistroImpresion.registrarOK(tipo, hojas, precio,
                    tinta.getHojasBN(), tinta.getHojasColor());
            return String.format(MSG_OK, precio,
                    tinta.getHojasBN(), tinta.getHojasColor());
        } else {
            RegistroImpresion.registrarKO(tipo, hojas,
                    tinta.getHojasBN(), tinta.getHojasColor());
            return String.format(MSG_KO,
                    tinta.getHojasBN(), tinta.getHojasColor());
        }
    }
}