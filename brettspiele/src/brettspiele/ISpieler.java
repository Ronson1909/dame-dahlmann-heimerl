package brettspiele;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Spielerinterface
 * @author Christian
 *
 */
public interface ISpieler extends Serializable, ZugBeendetListener {
	/**
	 * Gibt die Farbe des Spielers zurück.
	 * @return Die Farbe des Spielers.
	 */
	public int getEigeneFarbe();
	
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
	public void addZugBeendetListener(ZugBeendetListener zbl);
	
	/**
	 * Entfernt einen ZugBeendetListener.
	 * @param zbl Der zu entfernende ZugBeendetListener.
	 */
	public void removeZugBeendetListener(ZugBeendetListener zbl);
	
	/**
	 * Entfernt alle ZugBeendetListener.
	 *
	 */
	public void clearZugBeendetListener();
}
