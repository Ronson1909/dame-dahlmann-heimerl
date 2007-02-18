package brettspiele;

/**
 * Standardklasse, um auf ZugBeendet-Events reagieren zu können.
 * @author Christian
 *
 */
public class ZugBeendetAdapter<Z extends IZug> implements ZugBeendetListener<Z> {
	public void zugBeendet(ZugBeendetEvent<Z> zbe) {
		
	}
}
