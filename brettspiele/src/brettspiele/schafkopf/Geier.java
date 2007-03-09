package brettspiele.schafkopf;

import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;

public class Geier extends AbstractEinBildTrumpfSoli {
	public Geier() {
		super(Bilder.OBER);
	}

	public Geier(Farben farbe) {
		super(Bilder.OBER, farbe);
	}

	@Override
	public int getSpielartValue() {
		return getSpielartValueStatic();
	}

	public static int getSpielartValueStatic() {
		return 10;
	}

	@Override
	public String toString() {
		if (getTrumpfFarben()==null || getTrumpfFarben().length==0) {
			return "Farbloser Geier";
		}
		else {
			switch (getTrumpfFarben()[0]) {
			case EICHEL:
				return "Eichel-Geier";
			case GRAS:
				return "Gras-Geier";
			case HERZ:
				return "Herz-Geier";
			case SCHELLEN:
				return "Schellen-Geier";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}
