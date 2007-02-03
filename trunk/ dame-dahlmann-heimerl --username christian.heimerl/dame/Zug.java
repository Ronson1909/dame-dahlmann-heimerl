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
        //Feldbegrenzungen
        if (endeY<0 || endeY>7 || startY<0 || startY>7)
            return false;

        //Feldbegrenzungen
        if (endeX<0 || endeX>7 || startX<0 || startX>7)
            return false;

        //weiße Felder sind ungültig
        if (startX % 2 != startY % 2 || endeX % 2 != endeY % 2)
            return false;

        return true;
    }

    public boolean istGueltigerZug(Spielbrett sb) {
        //prüfe: ist eigener Stein

        //prüfe: Ziel ist freies Feld

        //wenn keine Dame
        //prüfe: richtige Richtung (nach oben)

        //prüfe: Felder liegen nebeneinander

        //prüfe: oder erlaubter Sprung über Gegner

        //wenn Dame
        //prüfe: erlaubter Sprung über Gegner
    }
}
