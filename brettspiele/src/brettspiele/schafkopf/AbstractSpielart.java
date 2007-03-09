package brettspiele.schafkopf;

import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public abstract class AbstractSpielart implements ISpielart {
	public final int compareTo(ISpielart o) {
		//dieses Spiel kleiner?
		return new Integer( this.getSpielartValue() ).compareTo( this.getSpielartValue() );
	}
	
	public int compare(Spielkarten arg0, Spielkarten arg1) {
		Bilder wert0 =SchafkopfSpielsituation.getBild(arg0);
		Bilder wert1 =SchafkopfSpielsituation.getBild(arg1);
		Farben farbe0 =SchafkopfSpielsituation.getFarbe(arg0);
		Farben farbe1 =SchafkopfSpielsituation.getFarbe(arg1);
		
		if (farbe0==farbe1)
			return -wert0.compareTo(wert1);
		else
			return -farbe0.compareTo(farbe1);
	}
}
