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
	 * Gibt die Farbe des Spielers zurück.
	 * @return Die Farbe des Spielers.
	 */
	public int getEigeneFarbe();
	
	/**
	 * Damit wird die Zugausführung angestoßen. Das kann z.B. das Starten der KI sein.
	 * @param sb Das aktuelle Spielbrett (vielleicht eine Kopie?).
	 */
	public void startGettingNaechstenZug(Spielbrett sb);
	
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

/**
 * Enthält die Parameter eines ausgeführten Zuges, also wer, was gemacht hat.
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
	 * Gibt die durchgeführte Zugfolge zurück.
	 * @return Die durchgeführte Zugfolge.
	 */
	public ArrayList<Zug> getZugfolge() {
		return zugfolge;
	}

	/**
	 * Gibt den ausführenden Spieler zurück.
	 * @return Der ausführende Spieler.
	 */
	public ISpieler getSpieler() {
		return spieler;
	}
}

/**
 * Interface, um auf ZugBeendet-Events reagieren zu können.
 * @author Christian
 *
 */
interface ZugBeendetListener extends java.util.EventListener {
	public void zugBeendet(ZugBeendetEvent zbe);
}

/**
 * Standardklasse, um auf ZugBeendet-Events reagieren zu können.
 * @author Christian
 *
 */
class ZugBeendetAdapter implements ZugBeendetListener {
	public void zugBeendet(ZugBeendetEvent zbe) {
		
	}
}
