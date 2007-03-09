package brettspiele.schafkopf;

import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public abstract class AbstractEinBildTrumpfSoli extends AbstractSoli {
	public AbstractEinBildTrumpfSoli(Bilder bild) {
		super(new Bilder[]{bild}, null);
	}

	public AbstractEinBildTrumpfSoli(Bilder bild, Farben farbe) {
		super(new Bilder[] {bild}, new Farben[] {farbe});
	}

	public AbstractEinBildTrumpfSoli(Spielkarten bildUndFarbe) {
		super(new Bilder[] {SchafkopfSpielsituation.getBild(bildUndFarbe)}, new Farben[] {SchafkopfSpielsituation.getFarbe(bildUndFarbe)});
	}
}
