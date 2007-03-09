package brettspiele.schafkopf;

import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;

public class Wenz extends AbstractEinBildTrumpfSoli {
	public Wenz() {
		super(Bilder.UNTER);
	}

	public Wenz(Farben farbe) {
		super(Bilder.UNTER, farbe);
	}

	@Override
	public int getSpielartValue() {
		return getSpielartValueStatic();
	}

	public static int getSpielartValueStatic() {
		return 20;
	}

	@Override
	public String toString() {
		if (getTrumpfFarben()==null || getTrumpfFarben().length==0) {
			return "Farbloser Wenz";
		}
		else {
			switch (getTrumpfFarben()[0]) {
			case EICHEL:
				return "Eichel-Wenz";
			case GRAS:
				return "Gras-Wenz";
			case HERZ:
				return "Herz-Wenz";
			case SCHELLEN:
				return "Schellen-Wenz";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}
