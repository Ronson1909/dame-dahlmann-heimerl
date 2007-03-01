package brettspiele.schafkopf.heimerlKI;

import brettspiele.ZugBeendetListener;
import brettspiele.schafkopf.AbstractKI;
import brettspiele.schafkopf.Ausspielvorgang;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public class HeimerlKI extends AbstractKI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2389766114324885200L;

	public HeimerlKI(int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
	}

	@Override
	public void run() {
		Spielkarten[] eigeneKarten = this.sss.getEigeneKarten();
		
		for (Spielkarten karte : eigeneKarten) {
			try {
				sss.getAktuellesSpiel().prüfeAufGültigeKarte(this.sss, karte);
				
				beendeZug(new Ausspielvorgang(karte));
				return;
			}
			catch (Exception ex) {
			}
		}
	}

}
