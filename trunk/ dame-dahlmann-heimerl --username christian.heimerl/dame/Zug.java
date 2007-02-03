package dame;

/**
 * <p>Überschrift: </p>
 *
 * <p>Beschreibung: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Organisation: </p>
 *
 * @author unbekannt
 * @version 1.0
 */
public class Zug {
    public Zug() {
    }

    private int startX;
    private int startY;
    private int endeX;
    private int endeY;

    public int gibStartX() {
        return startX;
    }

    public void setzeStartX(int wert) {
        startX=wert;
    }

    public int gibStartY() {
        return startY;
    }

    public void setzeStartY(int wert) {
        startY=wert;
    }

    public int gibEndeX() {
        return endeX;
    }

    public void setzeEndeX(int wert) {
        endeX=wert;
    }

    public int gibEndeY() {
        return endeY;
    }

    public void setzeEndeY(int wert) {
        endeY=wert;
    }

    public boolean hatGueltigeKoordinaten() {
        if (endeY<0 || endeY>7 || startY<0 || startY>7)
            return false;

        if (endeX<0 || endeX>7 || startX<0 || startX>7)
            return false;

        if (startX % 2 != startY || endeX % 2 != endeY)
            return false;

        return true;
    }

    public boolean istGueltigerZug(Spielbrett sb) {

    }
}
