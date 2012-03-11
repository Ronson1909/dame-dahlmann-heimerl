package brettspiele.halma.heimerlKI;

import brettspiele.halma.Zug;

public class BewerteterZug extends Zug implements Comparable<BewerteterZug> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4538937826918633094L;
	private int bewertung;

	public int getBewertung() {
		return bewertung;
	}

	public void setBewertung(int bewertung) {
		this.bewertung = bewertung;
	}

	public int compareTo(BewerteterZug o) {
		return -Double.compare(bewertung, o.bewertung);
	}
	
	public String toString() {
		return super.toString() + " (Wert: " + bewertung + ")";
	}
}
