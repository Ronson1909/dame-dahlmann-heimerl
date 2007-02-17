package brettspiele;

import java.util.ArrayList;

public interface ISpielablauf extends java.io.Serializable, ZugBeendetListener {
    public ISpielsituation getSpielsituation();
    public int getFarbeAmZug();

    public IBrettspielComponent createBrettspielComponent();
    
    /**
     * Gibt den aktuellen Spieler zurück.
     * @return Der aktuelle Spieler.
     */
    public ISpieler getSpielerAmZug();

    /**
     * Gibt alle Spieler als Array zurück
     * @return Die Spieler des Spiels als Array.
     */
    public ISpieler[] getSpieler();

    /**
     * Macht mehrere Züge rückgängig. Wenn
     * kein Zug rückgängig gemacht werden kann, wird keine
     * Exception geworfen.
     * @param anzahlZuege Die Anzahl der rückgängig zu machenden Züge. 
     */
    public void undoZug(int anzahlZuege);

    /**
     * Stellt mehrere rückgängig gemachte Züge wieder her. Wenn
     * kein Zug wiederhergestellt werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void redoZug(int anzahlZuege);

    /**
     * Gibt die Anzahl der Züge im Undo-Puffer zurück.
     * @return Die Anzahl der Züge im Undo-Puffer.
     */
    public int getUndoCount();

    /**
     * Gibt die Anzahl der Züge im Redo-Puffer zurück.
     * @return Die Anzahl der Züge im Redo-Puffer.
     */
    public int getRedoCount();
}
