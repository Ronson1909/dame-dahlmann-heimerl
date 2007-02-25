package brettspiele;

import java.io.Serializable;

/**
 * Spielerinterface
 * @author Christian
 *
 */
public interface ISpieler<Z extends IZug> extends Serializable, ZugBeendetListener<Z> {
	/**
	 * Gibt die Farbe des Spielers zurück.
	 * @return Die Farbe des Spielers.
	 */
	public int getEigeneFarbe();

	/**
	 * Gibt den Namen oder die Farbe des Spielers als Text zurück.
	 * @return Den Name oder die Farbe des Spielers.
	 */
	public String getName();

	/**
	 * Damit wird die Zugausführung angestoßen. Das kann z.B. das Starten der KI sein.
	 * @param sb Das aktuelle Spielbrett (vielleicht eine Kopie?).
	 */
	public void startGettingNaechstenZug(ISpielsituation ss);
	
	/**
	 * Bricht die Zugausführung ab. Z.B. könnte dadurch das Rechnen der KI abgebrochen werden.
	 *
	 */
	public void cancelGettingNaechstenZug();

	/**
	 * Fügt einen ZugBeendetListener hinzu. 
	 * @param zbl Der hinzuzufügende ZugBeendetListener.
	 */
	public void addZugBeendetListener(ZugBeendetListener<Z> zbl);
	
	/**
	 * Entfernt einen ZugBeendetListener.
	 * @param zbl Der zu entfernende ZugBeendetListener.
	 */
	public void removeZugBeendetListener(ZugBeendetListener<Z> zbl);
	
	/**
	 * Entfernt alle ZugBeendetListener.
	 *
	 */
	public void clearZugBeendetListener();
}
