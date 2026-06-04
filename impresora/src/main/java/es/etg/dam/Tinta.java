package es.etg.dam;

public class Tinta {

    public static final int HOJAS_BN_INICIAL    = 50;
    public static final int HOJAS_COLOR_INICIAL = 20;

    private int hojasBN;
    private int hojasColor;

    public Tinta() {
        hojasBN    = HOJAS_BN_INICIAL;
        hojasColor = HOJAS_COLOR_INICIAL;
    }

    public synchronized boolean consumirBN(int hojas) {
        if (hojasBN < hojas) return false;
        hojasBN -= hojas;
        return true;
    }

    public synchronized boolean consumirColor(int hojas) {
        if (hojasColor < hojas) return false;
        hojasColor -= hojas;
        return true;
    }

    public int getHojasBN()    { return hojasBN; }
    public int getHojasColor() { return hojasColor; }
}