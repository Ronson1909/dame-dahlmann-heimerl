package brettspiele;

/**
 * Enthält die Parameter eines ausgeführten Zuges, also wer, was gemacht hat.
 * @author Christian
 *
 */
public class ZugBeendetEvent<Z extends IZug> extends java.util.EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5090320113997141299L;
	protected Z zug;
	protected ISpieler<? extends Z> spieler;
	
	public ZugBeendetEvent(Object source, ISpieler<? extends Z> spieler, Z zug) {
		super(source);
		this.spieler=spieler;
		this.zug=zug;
	}
	
	/**
	 * Gibt den durchgeführten Zug zurück.
	 * @return Der durchgeführte Zug.
	 */
	public Z getZug() {
		return zug;
	}

	/**
	 * Gibt den ausführenden Spieler zurück.
	 * @return Der ausführende Spieler.
	 */
	public ISpieler<? extends Z> getSpieler() {
		return spieler;
	}
}
