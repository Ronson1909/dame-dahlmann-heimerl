package brettspiele.dame;

import brettspiele.*;

public class ZugFolgeBeendetEvent extends ZugBeendetEvent {
	public ZugFolgeBeendetEvent(Object source, ISpieler spieler, ZugFolge zug) {
		super(source, spieler, zug);
	}

	/**
	 * Gibt die durchgef�hrte Zugfolge zur�ck.
	 * @return Die durchgef�hrte Zugfolge.
	 */
	public ZugFolge getZug() {
		return (ZugFolge)zug;
	}

}
