package brettspiele;

import java.util.ArrayList;

/**
 * Enth�lt die Parameter eines ausgef�hrten Zuges, also wer, was gemacht hat.
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
	 * Gibt den durchgef�hrten Zug zur�ck.
	 * @return Der durchgef�hrte Zug.
	 */
	public IZug getZug() {
		return zug;
	}

	/**
	 * Gibt den ausf�hrenden Spieler zur�ck.
	 * @return Der ausf�hrende Spieler.
	 */
	public ISpieler getSpieler() {
		return spieler;
	}
}
