package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import brettspiele.halma.KarteSpielen;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;
import brettspiele.schafkopf.SchafkopfSpielsituation.Bilder;

public class Sauspiel implements Comparator<Spielkarten> {
	private Spielkarten gesuchteSau;
	
	public int compare(Spielkarten arg0, Spielkarten arg1) {
		Bilder wert0 =SchafkopfSpielsituation.getBild(arg0);
		Bilder wert1 =SchafkopfSpielsituation.getBild(arg1);
		Farben farbe0 =SchafkopfSpielsituation.getFarbe(arg0);
		Farben farbe1 =SchafkopfSpielsituation.getFarbe(arg1);
		
		if (wert0==wert1 && (wert0==Bilder.Ober || wert0==Bilder.Unter)) {
			return -farbe0.compareTo(farbe1);
		}
		else if (wert0==Bilder.Ober) {
			return 1;
		}
		else if (wert1==Bilder.Ober) {
			return -1;
		}
		else if (wert0==Bilder.Unter) {
			return 1;
		}
		else if (wert1==Bilder.Unter) {
			return -1;
		}
		else if (farbe0==farbe1) {
			return -wert0.compareTo(wert1);
		}
		else if (farbe0==Farben.Herz)
			return 1;
		else if (farbe1==Farben.Herz)
			return -1;
		else
			return -farbe0.compareTo(farbe1);
	}

	public void prüfeAufGültigeKarte(SchafkopfSpielsituation sss, Spielkarten zuSpielendeKarte) {
		Spielkarten[] stich = sss.getStich();
		ArrayList<Spielkarten> eigeneKarten = new ArrayList<Spielkarten>();
		Collections.addAll(eigeneKarten, sss.getKartenDesSpielers(sss.getSpielerAmZug()));
		
		if (stich.length==0 || stich.length==4) {
			boolean habDieGesuchteSau = eigeneKarten.contains(gesuchteSau);

			//erste Karte des Stichs
			if (habDieGesuchteSau) {
				if (SchafkopfSpielsituation.getFarbe(zuSpielendeKarte)==SchafkopfSpielsituation.getFarbe(gesuchteSau)) {
					//Spieler hat die Sau und will die Farbe spielen
					//--> darf die Farbe nur spielen,
					//wenn er hat nix anderes mehr, dann die Sau
					if (Sauspiel.getKartenMitAndererFarbe(eigeneKarten, SchafkopfSpielsituation.getFarbe(gesuchteSau)).size() == 0) {
						if (SchafkopfSpielsituation.getBild(zuSpielendeKarte)!=Bilder.As) {
							throw new IllegalArgumentException("Geht nicht anders, Sau rausspielen!");
						}
					}

					//oder er vier von der Farbe hat, und dann nicht die Sau
					else if (Sauspiel.getKartenDerFarbe(eigeneKarten, SchafkopfSpielsituation.getFarbe(zuSpielendeKarte)).size()>=4) {
						if (SchafkopfSpielsituation.getBild(zuSpielendeKarte)==Bilder.As) {
							throw new IllegalArgumentException("Weglaufen!");
						}
					}
				}
			}
		}
		else {
			Spielkarten ersteKarte = stich[0];

			//Trumpf zugeben
			if (Sauspiel.isTrumpf(ersteKarte)) {
				if (Sauspiel.getTrümpfe(eigeneKarten).size()>0 && !Sauspiel.isTrumpf(zuSpielendeKarte)) {
					throw new IllegalArgumentException("Trumpf zugeben!");
				}
			}
			
			//bzw. Farbe zugeben
			else {
				ArrayList<Spielkarten> kartenDerFarbe = Sauspiel.getKartenDerFarbe(eigeneKarten, SchafkopfSpielsituation.getFarbe(ersteKarte));
				
				if (kartenDerFarbe.size()>0) {
					if (kartenDerFarbe.contains(gesuchteSau) && zuSpielendeKarte != gesuchteSau) {
						throw new IllegalArgumentException("Sau zugeben!");
					}
					else if (!kartenDerFarbe.contains(zuSpielendeKarte)) {
						throw new IllegalArgumentException("Farbe zugeben!");
					}
				}
				
				//nicht die gesuchte Sau schmieren
				else {
					if (gesuchteSau == zuSpielendeKarte) {
						throw new IllegalArgumentException("Nicht die gesuchte Sau schmieren!");
					}
				}
			}
		}
	}
	
	public static boolean isTrumpf(Spielkarten karte) {
		Bilder wert =SchafkopfSpielsituation.getBild(karte);
		
		if (wert==Bilder.Ober || wert==Bilder.Unter)
			return true;
		else if (SchafkopfSpielsituation.getFarbe(karte)==Farben.Herz)
			return true;
		else
			return false;
		
	}
	
	public static ArrayList<Spielkarten> getTrümpfe(ArrayList<Spielkarten> hand) {
		ArrayList<Spielkarten> tr = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (isTrumpf(karte))
				tr.add(karte);
		
		return tr;
	}

	public static ArrayList<Spielkarten> getKartenDerFarbe(ArrayList<Spielkarten> hand, Farben farbe) {
		if (farbe==Farben.Herz)
			throw new IllegalArgumentException();

		ArrayList<Spielkarten> kart = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (!isTrumpf(karte) && SchafkopfSpielsituation.getFarbe(karte) == farbe)
				kart.add(karte);
		
		return kart;
	}

	public static ArrayList<Spielkarten> getKartenMitAndererFarbe(ArrayList<Spielkarten> hand, Farben farbe) {
		if (farbe==Farben.Herz)
			throw new IllegalArgumentException();

		ArrayList<Spielkarten> kart = new ArrayList<Spielkarten>();
		
		for (Spielkarten karte : hand)
			if (isTrumpf(karte) || SchafkopfSpielsituation.getFarbe(karte) != farbe)
				kart.add(karte);
		
		return kart;
	}

	public Spielkarten getGesuchteSau() {
		return gesuchteSau;
	}

	public void setGesuchteSau(Spielkarten gesuchteSau) {
		if (SchafkopfSpielsituation.getBild(gesuchteSau) != Bilder.As)
			throw new IllegalArgumentException();
		
		this.gesuchteSau = gesuchteSau;
	}
}
