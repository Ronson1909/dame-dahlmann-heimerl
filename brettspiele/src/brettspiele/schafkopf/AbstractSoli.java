package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public abstract class AbstractSoli extends AbstractSpielart {
	private Farben[] trumpfFarben;
	private Bilder[] trumpfBilder;

	public AbstractSoli() {
		
	}

	public AbstractSoli(Bilder[] trumpfBilder, Farben[] trumpfFarben) {
		this.trumpfFarben = trumpfFarben;
		this.trumpfBilder = trumpfBilder;
	}

	public Spielkarten getHoechsteKarte(List<Spielkarten> stich) {
		if (stich == null || stich.size()>4 || stich.size() < 1)
			throw new IllegalArgumentException("Ung�ltiger Stich in getHoechsteKarte");
		
		//ist erste Karte Trumpf?
		if (isTrumpf(stich.get(0))) {
			//dann z�hlt die h�chste Karte
			return Collections.max(stich, this);
		}
		else {
			//wenn nicht, wurde �berhaupt ein Trumpf gespielt? Der h�chste sticht.
			Spielkarten max = Collections.max(stich, this);
			
			if (isTrumpf(max))
				return max;
			else {
				//sonst z�hlt der h�chste der Farbe der ersten Karte.
				return Collections.max(getKartenDerFarbe(stich, SchafkopfSpielsituation.getFarbe(stich.get(0))), this);
			}
		}
	}

	public static boolean isTrumpf(Spielkarten karte, Bilder[] trumpfBilder, Farben[] trumpfFarben) {
		Bilder wert =SchafkopfSpielsituation.getBild(karte);
		for (Bilder trumpfBild : trumpfBilder)
			if (wert==trumpfBild)
				return true;

		Farben farbe = SchafkopfSpielsituation.getFarbe(karte);
		for (Farben trumpFarbe : trumpfFarben)
			if (farbe==trumpFarbe)
				return true;

		return false;
	}
	
	public final boolean isTrumpf(Spielkarten karte) {
		return isTrumpf(karte, trumpfBilder, trumpfFarben);
	}

	public final ArrayList<Spielkarten> getKartenDerFarbe(Collection<Spielkarten> hand, Farben farbe) {
		for (Farben trumpFarbe : trumpfFarben)
			if (farbe==trumpFarbe)
				throw new IllegalArgumentException();
		
		ArrayList<Spielkarten> kart = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (!isTrumpf(karte) && SchafkopfSpielsituation.getFarbe(karte) == farbe)
				kart.add(karte);
		
		return kart;
	}

	public static ArrayList<Spielkarten> getTruempfe(Collection<Spielkarten> hand, Bilder[] trumpfBilder, Farben[] trumpfFarben) {
		ArrayList<Spielkarten> tr = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (isTrumpf(karte, trumpfBilder, trumpfFarben))
				tr.add(karte);
		
		return tr;
	}

	public final ArrayList<Spielkarten> getTruempfe(Collection<Spielkarten> hand) {
		return getTruempfe(hand, trumpfBilder, trumpfFarben);
	}

	public final void pruefeAufGueltigeKarte(SchafkopfSpielsituation sss, Spielkarten zuSpielendeKarte) {
		List<Spielkarten> stich = sss.getStich();
		
		//erste Karte des Stichs?
		if (stich.size()==0 || stich.size()==4) {
			//alles okay
		}
		else {
			//nicht erste Karte des Stichs

			List<Spielkarten> eigeneKarten = sss.getSpielerkarten(sss.getSpielerAmZug());
			Spielkarten ersteKarte = stich.get(0);

			//Trumpf zugeben
			if (isTrumpf(ersteKarte)) {
				if (getTruempfe(eigeneKarten).size()>0) {
					if (!isTrumpf(zuSpielendeKarte)) {
						throw new IllegalArgumentException("Trumpf zugeben!");
					}
				}				
			}
			
			//bzw. Farbe zugeben
			else {
				ArrayList<Spielkarten> kartenDerFarbe = getKartenDerFarbe(eigeneKarten, SchafkopfSpielsituation.getFarbe(ersteKarte));
				
				if (kartenDerFarbe.size()>0) {
					if (!kartenDerFarbe.contains(zuSpielendeKarte)) {
						throw new IllegalArgumentException("Farbe zugeben!");
					}
				}
			}
		}
	}

	public int compare(Spielkarten o0, Spielkarten o1) {
		Bilder wert0 =SchafkopfSpielsituation.getBild(o0);
		Bilder wert1 =SchafkopfSpielsituation.getBild(o1);
		Farben farbe0 =SchafkopfSpielsituation.getFarbe(o0);
		Farben farbe1 =SchafkopfSpielsituation.getFarbe(o1);
		int trumpfBildIndex0 = -1;
		int trumpfBildIndex1 = -1;
		int trumpfFarbeIndex0 = -1;
		int trumpfFarbeIndex1 = -1;
		
		if (trumpfBilder!=null) {
			for (int i=0; i<trumpfBilder.length ; i++) {
				if (wert0==trumpfBilder[i]) {
					trumpfBildIndex0 = i;
					break;
				}
			}
	
			for (int i=0; i<trumpfBilder.length ; i++) {
				if (wert1==trumpfBilder[i]) {
					trumpfBildIndex1 = i;
					break;
				}
			}
		}

		if (trumpfFarben!=null) {
			for (int i=0; i<trumpfFarben.length ; i++) {
				if (farbe0==trumpfFarben[i]) {
					trumpfFarbeIndex0 = i;
					break;
				}
			}
	
			for (int i=0; i<trumpfFarben.length ; i++) {
				if (farbe1==trumpfFarben[i]) {
					trumpfFarbeIndex1 = i;
					break;
				}
			}
		}
		
		if (trumpfBildIndex0==-1 && trumpfBildIndex0==trumpfBildIndex1) {
			//beides Nicht-Trumpfbilder (z.B. 9 und 10 oder 9 und 9)
			//dann schauen ob es Trumpffarben sind
			
			if (trumpfFarbeIndex0==-1 && trumpfFarbeIndex0==trumpfFarbeIndex1) {
				//beides Nicht-Trumpffarben (z.B. 9 Schellen und 10 Eichel oder 9 Schellen und 9 Eichel)
				//dann entscheidet zuerst die Farbe, dann der Wert
				
				if (farbe0==farbe1)
					return -wert0.compareTo(wert1);
				else
					return -farbe0.compareTo(farbe1);
			}

			else if (trumpfFarbeIndex0==trumpfFarbeIndex1) {
				//gleiche Trumpffarben (z.B. 9 Herz und 10 Herz)
				//dann entscheiden der Wert
				return -wert0.compareTo(wert1);
			}
			else {
				//zwei untersch. Trumpffarben (z.B. Herz und Schellen oder Herz und keine Trumpffarbe)
				//dann entscheidet wer h�her ist
				if (trumpfFarbeIndex0>trumpfFarbeIndex1) {
					return 1;
				}
				else if (trumpfFarbeIndex0<trumpfFarbeIndex1) {
					return -1;
				}
				else
					return 0;
			}
			
		}
		else if (trumpfBildIndex0==trumpfBildIndex1) {
			//gleiche Trumpfbilder (z.B. Ober und Ober)
			//dann entscheiden die Farben
			return -farbe0.compareTo(farbe1);
		}
		else {
			//zwei untersch. Trumpfbilder (z.B. Ober und Unter oder Ober und 9)
			//dann entscheidet wer h�her ist
			if (trumpfBildIndex0>trumpfBildIndex1) {
				return 1;
			}
			else if (trumpfBildIndex0<trumpfBildIndex1) {
				return -1;
			}
			else
				return 0;
		}
	}

	/**
	 * @return the trumpfBilder
	 */
	protected Bilder[] getTrumpfBilder() {
		return trumpfBilder;
	}

	/**
	 * @param trumpfBilder the trumpfBilder to set
	 */
	protected void setTrumpfBilder(Bilder[] trumpfBilder) {
		this.trumpfBilder = trumpfBilder;
	}

	/**
	 * @return the trumpfFarben
	 */
	protected Farben[] getTrumpfFarben() {
		return trumpfFarben;
	}

	/**
	 * @param trumpfFarben the trumpfFarben to set
	 */
	protected void setTrumpfFarben(Farben[] trumpfFarben) {
		this.trumpfFarben = trumpfFarben;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AbstractSoli) {
			AbstractSoli sol = (AbstractSoli)obj;
			
			if (trumpfFarben == null ^ sol.trumpfFarben == null )
				return false;
			else if (trumpfFarben != null && sol.trumpfFarben != null) {
				if (trumpfFarben.length != sol.trumpfFarben.length)
					return false;
				else
					for (int i=0;i<trumpfFarben.length;i++)
						if (trumpfFarben[i] != sol.trumpfFarben[i])
							return false;
			}

			if (trumpfBilder == null ^ sol.trumpfBilder == null )
				return false;
			else if (trumpfBilder != null && sol.trumpfBilder != null) {
				if (trumpfBilder.length != sol.trumpfBilder.length)
					return false;
				else
					for (int i=0;i<trumpfBilder.length;i++)
						if (trumpfBilder[i] != sol.trumpfBilder[i])
							return false;
			}
			
			return true;
		}
		else
			return false;
	}
}
