package brettspiele;

import java.io.Serializable;

/**
 * Spielerinterface
 * @author Christian
 *
 */
public interface ISpieler<Z extends IZug> extends Serializable, ZugBeendetListener<Z> {
	/**
	 * Gibt die Farbe des Spielers zur�ck.
	 * @return Die Farbe des Spielers.
	 */
	public int getEigeneFarbe();

	/**
	 * Gibt den Namen oder die Farbe des Spielers als Text zur�ck.
	 * @return Den Name oder die Farbe des Spielers.
	 */
	public String getName();

	/**
	 * Damit wird die Zugausf�hrung angesto�en. Das kann z.B. das Starten der KI sein.
	 * @param sb Das aktuelle Spielbrett (vielleicht eine Kopie?).
	 */
	public void startGettingNaechstenZug(ISpielsituation ss);
	
	/**
	 * Bricht die Zugausf�hrung ab. Z.B. k�nnte dadurch das Rechnen der KI abgebrochen werden.
	 *
	 */
	public void cancelGettingNaechstenZug();

	/**
	 * F�gt einen ZugBeendetListener hinzu. 
	 * @param zbl Der hinzuzuf�gende ZugBeendetListener.
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
