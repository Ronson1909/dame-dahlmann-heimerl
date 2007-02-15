package brettspiele;

import java.util.ArrayList;

/**
 * Enthält die Parameter eines ausgeführten Zuges, also wer, was gemacht hat.
 * @author Christian
 *
 */
public class ZugBeendetEvent extends java.util.EventObject {
	protected IZug zug;
	protected ISpieler spieler;
	
	public ZugBeendetEvent(Object source, ISpieler spieler, IZug zug) {
		super(source);
		this.spieler=spieler;
		this.zug=zug;
	}
	
	/**
	 * Gibt den durchgeführten Zug zurück.
	 * @return Der durchgeführte Zug.
	 */
	public IZug getZug() {
		return zug;
	}

	/**
	 * Gibt den ausführenden Spieler zurück.
	 * @return Der ausführende Spieler.
	 */
	public ISpieler getSpieler() {
		return spieler;
	}
}
