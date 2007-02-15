package brettspiele.dame;

import brettspiele.*;

public class ZugFolgeBeendetEvent extends ZugBeendetEvent {
	public ZugFolgeBeendetEvent(Object source, ISpieler spieler, ZugFolge zug) {
		super(source, spieler, zug);
	}

	/**
	 * Gibt die durchgeführte Zugfolge zurück.
	 * @return Die durchgeführte Zugfolge.
	 */
	public ZugFolge getZug() {
		return (ZugFolge)zug;
	}

}
