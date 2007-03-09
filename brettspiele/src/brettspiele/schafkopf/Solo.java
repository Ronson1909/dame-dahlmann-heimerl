package brettspiele.schafkopf;

import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;

public class Solo extends AbstractSoli {
	public Solo() {
		super(new Bilder[]{Bilder.OBER, Bilder.UNTER}, null);
	}

	public Solo(Farben farbe) {
		super(new Bilder[]{Bilder.OBER, Bilder.UNTER}, new Farben[] { farbe });
	}

	@Override
	public int getSpielartValue() {
		return getSpielartValueStatic();
	}

	public static int getSpielartValueStatic() {
		return 30;
	}
	
	@Override
	public String toString() {
		if (getTrumpfFarben()==null || getTrumpfFarben().length==0) {
			return "Farbloses Solo";
		}
		else {
			switch (getTrumpfFarben()[0]) {
			case EICHEL:
				return "Eichel-Solo";
			case GRAS:
				return "Gras-Solo";
			case HERZ:
				return "Herz-Solo";
			case SCHELLEN:
				return "Schellen-Solo";
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}
