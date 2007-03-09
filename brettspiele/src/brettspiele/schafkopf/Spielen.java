package brettspiele.schafkopf;

import brettspiele.IZug;

public class Spielen implements IZug {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1341769707817548206L;
	private boolean spielen;

	/**
	 * @param spielen
	 */
	public Spielen(boolean spielen) {
		super();
		this.spielen = spielen;
	}

	/**
	 * @return the spielen
	 */
	public boolean isSpielen() {
		return spielen;
	}

	/**
	 * @param spielen the spielen to set
	 */
	public void setSpielen(boolean spielen) {
		this.spielen = spielen;
	}
}
