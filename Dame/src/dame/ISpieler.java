package dame;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Spielerinterface
 * @author Christian
 *
 */
public interface ISpieler extends Serializable, ZugBeendetListener {
	/**
	 * Gibt die Farbe des Spielers zur�ck.
	 * @return Die Farbe des Spielers.
	 */
	public int getEigeneFarbe();
	
	/**
	 * Damit wird die Zugausf�hrung angesto�en. Das kann z.B. das Starten der KI sein.
	 * @param sb Das aktuelle Spielbrett (vielleicht eine Kopie?).
	 */
	public void startGettingNaechstenZug(Spielbrett sb);
	
	/**
	 * Bricht die Zugausf�hrung ab. Z.B. k�nnte dadurch das Rechnen der KI abgebrochen werden.
	 *
	 */
	public void cancelGettingNaechstenZug();

	/**
	 * F�gt einen ZugBeendetListener hinzu. 
	 * @param zbl Der hinzuzuf�gende ZugBeendetListener.
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

/**
 * Enth�lt die Parameter eines ausgef�hrten Zuges, also wer, was gemacht hat.
 * @author Christian
 *
 */
class ZugBeendetEvent extends java.util.EventObject {
	private ArrayList<Zug> zugfolge;
	private ISpieler spieler;
	
	public ZugBeendetEvent(Object source, ISpieler spieler, ArrayList<Zug> zugfolge) {
		super(source);
		this.spieler=spieler;
		this.zugfolge=zugfolge;
	}
	
	/**
	 * Gibt die durchgef�hrte Zugfolge zur�ck.
	 * @return Die durchgef�hrte Zugfolge.
	 */
	public ArrayList<Zug> getZugfolge() {
		return zugfolge;
	}

	/**
	 * Gibt den ausf�hrenden Spieler zur�ck.
	 * @return Der ausf�hrende Spieler.
	 */
	public ISpieler getSpieler() {
		return spieler;
	}
}

/**
 * Interface, um auf ZugBeendet-Events reagieren zu k�nnen.
 * @author Christian
 *
 */
interface ZugBeendetListener extends java.util.EventListener {
	public void zugBeendet(ZugBeendetEvent zbe);
}

/**
 * Standardklasse, um auf ZugBeendet-Events reagieren zu k�nnen.
 * @author Christian
 *
 */
class ZugBeendetAdapter implements ZugBeendetListener {
	public void zugBeendet(ZugBeendetEvent zbe) {
		
	}
}
