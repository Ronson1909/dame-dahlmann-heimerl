package brettspiele.halma;

import brettspiele.ZugBeendetListener;

public abstract class AbstractKI extends AbstractSpieler implements java.lang.Runnable {
	protected HalmaSpielbrett sb;
	
	public AbstractKI(int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
	}

	@Override
	public final void startGettingNaechstenZug(HalmaSpielbrett sb) {
		this.sb = sb;
		
		Thread th = new Thread(this, "Halma_KI");
		th.start();
	}
	
	/**
	 * Hier sollten die Berechnungen erfolgen und am Ende ein Aufruf von beendeZug erfolgen.
	 */
	public abstract void run();
}
