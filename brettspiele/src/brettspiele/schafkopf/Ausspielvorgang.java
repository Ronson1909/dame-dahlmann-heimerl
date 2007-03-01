package brettspiele.schafkopf;

import brettspiele.IZug;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public class Ausspielvorgang implements IZug {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4158886544057886863L;
	private Spielkarten ausgespielteKarte;
	
	public Ausspielvorgang(Spielkarten karte) {
		this.ausgespielteKarte = karte;
	}

	public Spielkarten getAusgespielteKarte() {
		return ausgespielteKarte;
	}

	public void setAusgespielteKarte(Spielkarten ausgespielteKarte) {
		this.ausgespielteKarte = ausgespielteKarte;
	}
}
