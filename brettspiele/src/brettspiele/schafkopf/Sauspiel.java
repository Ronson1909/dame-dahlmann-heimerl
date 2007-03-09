package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;
import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;

public class Sauspiel extends AbstractSpielart {
	private Spielkarten gesuchteSau;

	/**
	 * @param gesuchteSau
	 */
	public Sauspiel(Spielkarten gesuchteSau) {
		super();
		setGesuchteSau(gesuchteSau);
	}

	/**
	 * @param gesuchteSau
	 */
	public Sauspiel(Farben gesuchteSau) {
		super();
		setGesuchteSau(gesuchteSau);
	}

	@Override
	public int getSpielartValue() {
		return 5;
	}
	
	public int compare(Spielkarten arg0, Spielkarten arg1) {
		Bilder wert0 =SchafkopfSpielsituation.getBild(arg0);
		Bilder wert1 =SchafkopfSpielsituation.getBild(arg1);
		Farben farbe0 =SchafkopfSpielsituation.getFarbe(arg0);
		Farben farbe1 =SchafkopfSpielsituation.getFarbe(arg1);
		
		if (wert0==wert1 && (wert0==Bilder.OBER || wert0==Bilder.UNTER)) {
			return -farbe0.compareTo(farbe1);
		}
		else if (wert0==Bilder.OBER) {
			return 1;
		}
		else if (wert1==Bilder.OBER) {
			return -1;
		}
		else if (wert0==Bilder.UNTER) {
			return 1;
		}
		else if (wert1==Bilder.UNTER) {
			return -1;
		}
		else if (farbe0==farbe1) {
			return -wert0.compareTo(wert1);
		}
		else if (farbe0==Farben.HERZ)
			return 1;
		else if (farbe1==Farben.HERZ)
			return -1;
		else
			return -farbe0.compareTo(farbe1);
	}

	public void prüfeAufGültigeKarte(SchafkopfSpielsituation sss, Spielkarten zuSpielendeKarte) {
		List<Spielkarten> stich = sss.getStich();
		List<Spielkarten> eigeneKarten = sss.getSpielerkarten(sss.getSpielerAmZug());
		boolean habDieGesuchteSau = eigeneKarten.contains(gesuchteSau);
		boolean weggelaufen = false;
		
		//erste Karte des Stichs?
		if (stich.size()==0 || stich.size()==4) {

			if (habDieGesuchteSau) {
				if (SchafkopfSpielsituation.getFarbe(zuSpielendeKarte)==SchafkopfSpielsituation.getFarbe(gesuchteSau)) {
					//Spieler hat die Sau und will die Farbe spielen

					//wenn er vier von der Farbe hat kann er beliebige Karte spielen
					if (getKartenDerFarbe(eigeneKarten, SchafkopfSpielsituation.getFarbe(zuSpielendeKarte)).size()>=4) {
						//okay
						weggelaufen=true;
					}

					//sonst muss es die Sau sein
					else if (SchafkopfSpielsituation.getBild(zuSpielendeKarte)!=Bilder.AS) {
						throw new IllegalArgumentException("Von der gesuchten Farbe muss es die Sau sein!");
					}
				}
			}
		}
		else {
			//nicht erste Karte des Stichs
			
			Spielkarten ersteKarte = stich.get(0);

			//Trumpf zugeben
			if (isTrumpf(ersteKarte)) {
				if (getTrümpfe(eigeneKarten).size()>0) {
					if (!isTrumpf(zuSpielendeKarte)) {
						throw new IllegalArgumentException("Trumpf zugeben!");
					}
				}				
			}
			
			//bzw. Farbe zugeben
			else {
				ArrayList<Spielkarten> kartenDerFarbe = getKartenDerFarbe(eigeneKarten, SchafkopfSpielsituation.getFarbe(ersteKarte));
				
				if (kartenDerFarbe.size()>0) {
					//gesuchte Sau zugeben außer weggelaufen
					if (kartenDerFarbe.contains(gesuchteSau) && !weggelaufen && zuSpielendeKarte != gesuchteSau) {
						throw new IllegalArgumentException("Sau zugeben!");
					}
					
					//sonst Farbe zugeben
					if (!kartenDerFarbe.contains(zuSpielendeKarte)) {
						throw new IllegalArgumentException("Farbe zugeben!");
					}
				}
				
				//nicht die gesuchte Sau schmieren, außer letzte Karte.
				else {
					if (gesuchteSau == zuSpielendeKarte && !weggelaufen && eigeneKarten.size()>1) {
						throw new IllegalArgumentException("Nicht die gesuchte Sau schmieren!");
					}
				}
			}
		}
	}

	public static boolean isTrumpfStatic(Spielkarten karte) {
		Bilder wert =SchafkopfSpielsituation.getBild(karte);
		
		if (wert==Bilder.OBER || wert==Bilder.UNTER)
			return true;
		else if (SchafkopfSpielsituation.getFarbe(karte)==Farben.HERZ)
			return true;
		else
			return false;
	}

	public boolean isTrumpf(Spielkarten karte) {
		return isTrumpfStatic(karte);
	}
	
	public Spielkarten getHoechsteKarte(List<Spielkarten> stich) {
		if (stich == null || stich.size()>4 || stich.size() < 1)
			throw new IllegalArgumentException("Ungültiger Stich in getHoechsteKarte");
		
		//ist erste Karte Trumpf?
		if (isTrumpf(stich.get(0))) {
			//dann zählt die höchste Karte
			return Collections.max(stich, this);
		}
		else {
			//wenn nicht, wurde überhaupt ein Trumpf gespielt? Der höchste sticht.
			Spielkarten max = Collections.max(stich, this);
			
			if (isTrumpf(max))
				return max;
			else {
				//sonst zählt der höchste der Farbe der ersten Karte.
				return Collections.max(getKartenDerFarbe(stich, SchafkopfSpielsituation.getFarbe(stich.get(0))), this);
			}
		}
	}

	public static ArrayList<Spielkarten> getTrümpfeStatic(Collection<Spielkarten> hand) {
		ArrayList<Spielkarten> tr = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (isTrumpfStatic(karte))
				tr.add(karte);
		
		return tr;
	}

	public ArrayList<Spielkarten> getTrümpfe(Collection<Spielkarten> hand) {
		return getTrümpfeStatic(hand);
	}

	public static ArrayList<Spielkarten> getKartenDerFarbeStatic(Collection<Spielkarten> hand, Farben farbe) {
		if (farbe==Farben.HERZ)
			throw new IllegalArgumentException();

		ArrayList<Spielkarten> kart = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (!isTrumpfStatic(karte) && SchafkopfSpielsituation.getFarbe(karte) == farbe)
				kart.add(karte);
		
		return kart;
	}

	public ArrayList<Spielkarten> getKartenDerFarbe(Collection<Spielkarten> hand, Farben farbe) {
		return getKartenDerFarbeStatic(hand, farbe);
	}

	public static ArrayList<Spielkarten> getKartenMitAndererFarbeStatic(Collection<Spielkarten> hand, Farben farbe) {
		if (farbe==Farben.HERZ)
			throw new IllegalArgumentException();

		ArrayList<Spielkarten> kart = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (isTrumpfStatic(karte) || SchafkopfSpielsituation.getFarbe(karte) != farbe)
				kart.add(karte);
		
		return kart;
	}

	public ArrayList<Spielkarten> getKartenMitAndererFarbe(Collection<Spielkarten> hand, Farben farbe) {
		return getKartenMitAndererFarbeStatic(hand, farbe);
	}

	public Spielkarten getGesuchteSau() {
		return gesuchteSau;
	}

	public void setGesuchteSau(Spielkarten gesuchteSau) {
		if (SchafkopfSpielsituation.getBild(gesuchteSau) != Bilder.AS)
			throw new IllegalArgumentException("Sie müssen auf eine Sau spielen!");
		
		this.gesuchteSau = gesuchteSau;
	}

	public void setGesuchteSau(Farben gesuchteSau) {
		switch (gesuchteSau) {
		case EICHEL:
			this.gesuchteSau=Spielkarten.As_Eichel;
			break;
		case GRAS:
			this.gesuchteSau=Spielkarten.As_Gras;
			break;
		case SCHELLEN:
			this.gesuchteSau=Spielkarten.As_Schellen;
			break;
		default:
			throw new IllegalArgumentException("Sie können nicht auf die Herz-Sau spielen!");
		}
	}
	
	@Override
	public String toString() {
		switch (SchafkopfSpielsituation.getFarbe(gesuchteSau)) {
		case EICHEL:
			return "Auf die Eichel";
		case GRAS:
			return "Auf die Blaue";
		case SCHELLEN:
			return "Auf die Schellen";
		}
		
		throw new IllegalArgumentException();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Sauspiel) {
			Sauspiel sau = (Sauspiel)obj;
			return gesuchteSau.equals(sau.gesuchteSau);
		}
		else
			return false;
	}
}
