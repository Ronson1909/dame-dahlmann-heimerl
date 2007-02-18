package brettspiele;

/**
 * Interface, um auf ZugBeendet-Events reagieren zu k�nnen.
 * @author Christian
 *
 */
public interface ZugBeendetListener<Z extends IZug> extends java.util.EventListener {
	public void zugBeendet(ZugBeendetEvent<Z> zbe);
}