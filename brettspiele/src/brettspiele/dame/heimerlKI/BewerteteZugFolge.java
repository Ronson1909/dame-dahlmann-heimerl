package brettspiele.dame.heimerlKI;

import brettspiele.dame.ZugFolge;

public class BewerteteZugFolge extends ZugFolge implements Comparable<BewerteteZugFolge> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4119627504245193957L;
	private int bewertung;

	public int getBewertung() {
		return bewertung;
	}

	public void setBewertung(int bewertung) {
		this.bewertung = bewertung;
	}

	public int compareTo(BewerteteZugFolge o) {
		return -Double.compare(bewertung, o.bewertung);
	}
	
	public String toString() {
		return super.toString() + " (Wert: " + bewertung + ")";
	}
}
