package brettspiele;

import java.util.ArrayList;

public interface ISpielablauf extends java.io.Serializable, ZugBeendetListener {
    public ISpielsituation getSpielsituation();
    public int getFarbeAmZug();

    public IBrettspielComponent createBrettspielComponent();
    
    /**
     * Gibt den aktuellen Spieler zur�ck.
     * @return Der aktuelle Spieler.
     */
    public ISpieler getSpielerAmZug();

    /**
     * Gibt alle Spieler als Array zur�ck
     * @return Die Spieler des Spiels als Array.
     */
    public ISpieler[] getSpieler();

    /**
     * Macht mehrere Z�ge r�ckg�ngig. Wenn
     * kein Zug r�ckg�ngig gemacht werden kann, wird keine
     * Exception geworfen.
     * @param anzahlZuege Die Anzahl der r�ckg�ngig zu machenden Z�ge. 
     */
    public void undoZug(int anzahlZuege);

    /**
     * Stellt mehrere r�ckg�ngig gemachte Z�ge wieder her. Wenn
     * kein Zug wiederhergestellt werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void redoZug(int anzahlZuege);

    /**
     * Gibt die Anzahl der Z�ge im Undo-Puffer zur�ck.
     * @return Die Anzahl der Z�ge im Undo-Puffer.
     */
    public int getUndoCount();

    /**
     * Gibt die Anzahl der Z�ge im Redo-Puffer zur�ck.
     * @return Die Anzahl der Z�ge im Redo-Puffer.
     */
    public int getRedoCount();
}
