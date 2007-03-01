package brettspiele.schafkopf;

import brettspiele.ZugBeendetListener;

public abstract class AbstractKI extends AbstractSpieler implements java.lang.Runnable {
	protected SchafkopfSpielsituation sss;
	
	public AbstractKI(int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
	}

	@Override
	public final void startGettingNaechstenZug(SchafkopfSpielsituation sss) {
		this.sss = sss;
		
		Thread th = new Thread(this, "Schafkopf_KI");
		th.start();
	}
	
	/**
	 * Hier sollten die Berechnungen erfolgen und am Ende ein Aufruf von beendeZug erfolgen.
	 */
	public abstract void run();
}
