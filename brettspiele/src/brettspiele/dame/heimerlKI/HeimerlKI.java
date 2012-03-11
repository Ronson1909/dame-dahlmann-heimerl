package brettspiele.dame.heimerlKI;

import java.awt.Window;

import brettspiele.*;
import brettspiele.dame.*;

public class HeimerlKI extends AbstractKI {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5613680373587200298L;

	public HeimerlKI(int eigeneFarbe, ZugBeendetListener<ZugFolge> zbl) {
		super(eigeneFarbe, zbl);
	}
	
	private Tiefensuche sm = new Tiefensuche(8);
	
	@Override
	public void run() {
		BewerteteZugFolge zf = sm.calcBestenZug(sb,0);
		beendeZug(zf);
	}

	@Override
	public void configure(Window owner, Object info) {
		sm.configure(owner);
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
