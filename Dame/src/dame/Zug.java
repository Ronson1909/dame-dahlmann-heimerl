package dame;

/**
 * <p>�berschrift: </p>
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
        //Feldbegrenzungen
        if (endeY<0 || endeY>7 || startY<0 || startY>7)
            return false;

        //Feldbegrenzungen
        if (endeX<0 || endeX>7 || startX<0 || startX>7)
            return false;

        //wei�e Felder sind ung�ltig
        if (startX % 2 != startY % 2 || endeX % 2 != endeY % 2)
            return false;

        return true;
    }

    public boolean istGueltigerZug(Spielbrett sb) {
        //pr�fe: ist eigener Stein

        //pr�fe: Ziel ist freies Feld

        //wenn keine Dame
        //pr�fe: richtige Richtung (nach oben)

        //pr�fe: Felder liegen nebeneinander

        //pr�fe: oder erlaubter Sprung �ber Gegner

        //wenn Dame
        //pr�fe: erlaubter Sprung �ber Gegner
    }
}
