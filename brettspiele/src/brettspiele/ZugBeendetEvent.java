package brettspiele;

/**
 * Enth�lt die Parameter eines ausgef�hrten Zuges, also wer, was gemacht hat.
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
	 * Gibt den durchgef�hrten Zug zur�ck.
	 * @return Der durchgef�hrte Zug.
	 */
	public Z getZug() {
		return zug;
	}

	/**
	 * Gibt den ausf�hrenden Spieler zur�ck.
	 * @return Der ausf�hrende Spieler.
	 */
	public ISpieler<? extends Z> getSpieler() {
		return spieler;
	}
}
