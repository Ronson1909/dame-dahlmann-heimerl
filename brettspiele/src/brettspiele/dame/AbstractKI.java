package brettspiele.dame;

import brettspiele.ZugBeendetListener;

public abstract class AbstractKI extends AbstractSpieler implements java.lang.Runnable {
	protected Spielbrett sb;
	
	public AbstractKI(int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
	}

	@Override
	public final void startGettingNaechstenZug(Spielbrett sb) {
		this.sb = sb;
		Thread th = new Thread(this);
		th.start();
	}
	
	/**
	 * Hier sollten die Berechnungen erfolgen und am Ende ein Aufruf von beendeZug erfolgen.
	 */
	public abstract void run();
}
