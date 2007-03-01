package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import brettspiele.ISpielsituation;

public class SchafkopfSpielsituation implements ISpielsituation {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2968108732794429540L;

	public enum Farben {
		Eichel,
		Gras,
		Herz,
		Schellen
	}

	public enum Bilder {
		As,
		Zehn,
		König,
		Ober,
		Unter,
		Neun,
		Acht,
		Sieben
	}

	public enum Spielkarten {
		As_Eichel,
		As_Gras,
		As_Herz,
		As_Schellen,
		König_Eichel,
		König_Gras,
		König_Herz,
		König_Schellen,
		Ober_Eichel,
		Ober_Gras,
		Ober_Herz,
		Ober_Schellen,
		Unter_Eichel,
		Unter_Gras,
		Unter_Herz,
		Unter_Schellen,
		Zehn_Eichel,
		Zehn_Gras,
		Zehn_Herz,
		Zehn_Schellen,
		Neun_Eichel,
		Neun_Gras,
		Neun_Herz,
		Neun_Schellen,
		Acht_Eichel,
		Acht_Gras,
		Acht_Herz,
		Acht_Schellen,
		Sieben_Eichel,
		Sieben_Gras,
		Sieben_Herz,
		Sieben_Schellen,
	}
	
	private int spielerAmZug = 0;

	private Sauspiel aktuellesSpiel = new Sauspiel();
	
	private ArrayList<Spielkarten> kartenDerSpieler[] = new ArrayList[4];
	
	public SchafkopfSpielsituation() {
		for (int i=0;i<=3;i++)
			kartenDerSpieler[i] = new ArrayList<Spielkarten>(8);
		
		teileAus();
	}

	public SchafkopfSpielsituation clone() {
		return this;
	}
	
	public int isSpielBeendet() {
		return -1;
	}
	
	public void teileAus() {
		ArrayList<Spielkarten> stapel = new ArrayList<Spielkarten>(32);
		for (Spielkarten sp : Spielkarten.values())
			stapel.add(sp);
		
		Random rnd = new Random();
		
		for (int sp=0;sp<=3;sp++) {
			for (int i=1;i<=8;i++) {
				kartenDerSpieler[sp].add(stapel.remove(rnd.nextInt(stapel.size())));
			}
			
			Collections.sort(kartenDerSpieler[sp], Collections.reverseOrder(aktuellesSpiel)); 
		}
	}

	public Spielkarten[] getEigeneKarten() {
		return kartenDerSpieler[spielerAmZug].toArray(new Spielkarten[0]);
	}

	public Spielkarten[] getKartenDesSpielers(int sp) {
		return kartenDerSpieler[sp].toArray(new Spielkarten[0]);
	}
	
	private ArrayList<Spielkarten> stich = new ArrayList<Spielkarten>();
	public Spielkarten[] getStich() {
		return stich.toArray(new Spielkarten[0]);
	}

	private int ausspielenderSpieler=0;
	public int getAusspielenderSpieler() {
		return ausspielenderSpieler;
	}
	
	public void ausspielen(Ausspielvorgang zug) {
		ArrayList<Spielkarten> eigeneKarten = kartenDerSpieler[spielerAmZug];
		Spielkarten zuSpielendeKarte = zug.getAusgespielteKarte();
		
		if (!eigeneKarten.contains(zuSpielendeKarte)) {
			throw new IllegalArgumentException("Der aktuelle Spieler hat diese Karte nicht!");
		}
		
		try {
			aktuellesSpiel.prüfeAufGültigeKarte(this, zuSpielendeKarte);
			
			if (stich.size()==4) {
				stich.clear();
			}
			
			stich.add(zuSpielendeKarte);
			eigeneKarten.remove(zuSpielendeKarte);

			spielerAmZug += 1;
			if (spielerAmZug>3)
				spielerAmZug=0;
			
			if (stich.size()==4) {
				System.out.println("Stich mit " + getPunkte(stich) + " Punkten.");
			}
			
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static Farben getFarbe(Spielkarten sp) {
		switch(sp) {
		case Sieben_Eichel: 
		case Acht_Eichel:
		case Neun_Eichel: 
		case Zehn_Eichel:
		case Unter_Eichel: 
		case Ober_Eichel:
		case König_Eichel: 
		case As_Eichel:
			return Farben.Eichel;
			
		case Sieben_Gras: 
		case Acht_Gras:
		case Neun_Gras: 
		case Zehn_Gras:
		case Unter_Gras: 
		case Ober_Gras:
		case König_Gras: 
		case As_Gras:
			return Farben.Gras;
			
		case Sieben_Herz: 
		case Acht_Herz:
		case Neun_Herz: 
		case Zehn_Herz:
		case Unter_Herz: 
		case Ober_Herz:
		case König_Herz: 
		case As_Herz:
			return Farben.Herz;
			
		case Sieben_Schellen: 
		case Acht_Schellen:
		case Neun_Schellen: 
		case Zehn_Schellen:
		case Unter_Schellen: 
		case Ober_Schellen:
		case König_Schellen: 
		case As_Schellen:
			return Farben.Schellen;
		}
		
		throw new IllegalArgumentException();
	}

	public static int getPunkte(ArrayList<Spielkarten> stich) {
		int punkte = 0;
		
		for (Spielkarten karte : stich)
			punkte += getPunkte(karte);
		
		return punkte;
	}

	public static int getPunkte(Spielkarten sp) {
		switch (getBild(sp)) {
		case As:
			return 11;
		case Zehn:
			return 10;
		case König:
			return 4;
		case Ober:
			return 3;
		case Unter:
			return 2;
		}
		
		return 0;
	}

	public static Bilder getBild(Spielkarten sp) {
		switch(sp) {
		case Sieben_Eichel: 
		case Sieben_Gras:
		case Sieben_Herz: 
		case Sieben_Schellen: 
			return Bilder.Sieben;
			
		case Acht_Eichel:
		case Acht_Gras:
		case Acht_Herz:
		case Acht_Schellen:
			return Bilder.Acht;
			
		case Neun_Eichel: 
		case Neun_Gras: 
		case Neun_Herz: 
		case Neun_Schellen: 
			return Bilder.Neun;

		case Zehn_Eichel:
		case Zehn_Gras:
		case Zehn_Herz:
		case Zehn_Schellen:
			return Bilder.Zehn;

		case Unter_Eichel: 
		case Unter_Gras: 
		case Unter_Herz: 
		case Unter_Schellen: 
			return Bilder.Unter;

		case Ober_Eichel:
		case Ober_Gras:
		case Ober_Herz:
		case Ober_Schellen:
			return Bilder.Ober;

		case König_Eichel: 
		case König_Gras: 
		case König_Herz: 
		case König_Schellen: 
			return Bilder.König;

		case As_Eichel:
		case As_Gras:
		case As_Herz:
		case As_Schellen:
			return Bilder.As;
		}
		
		throw new IllegalArgumentException();
	}

	public int getSpielerAmZug() {
		return spielerAmZug;
	}

	public Sauspiel getAktuellesSpiel() {
		return aktuellesSpiel;
	}

	public void setAktuellesSpiel(Sauspiel aktuellesSpiel) {
		this.aktuellesSpiel = aktuellesSpiel;
	}
}
