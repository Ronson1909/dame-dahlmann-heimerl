package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import brettspiele.ISpielsituation;

public class SchafkopfSpielsituation implements ISpielsituation {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2968108732794429540L;

	public enum Farben {
		EICHEL,
		GRAS,
		HERZ,
		SCHELLEN
	}

	public enum Bilder {
		AS,
		ZEHN,
		KÖNIG,
		OBER,
		UNTER,
		NEUN,
		ACHT,
		SIEBEN
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
	
	public enum Status {
		WARTE_AUF_SPIELEN_JA_NEIN,
		WARTE_AUF_SPIELARTWAHL,
		WARTE_AUF_AUSSPIELEN,
		WARTE_AUF_STICHBESTAETIGUNG
	}
	
	private Status status;
	
	private int spielerAmZug = 0;
	private ISpielart aktuellesSpiel; // = new Sauspiel(Farben.EICHEL);
	private ArrayList<Spielkarten> kartenDerSpieler[] = new ArrayList[4];
	private ArrayList<Integer> spielerMitSpielabsicht = new ArrayList<Integer>(4);
	private ISpielart[] spielabsichtDerSpieler = new ISpielart[4];
	private ArrayList<Integer> spielerMitSpielabsichtInt; //nur für interne Verwendung
	
	private int stichgewinner;
	private ArrayList<Spielkarten> stich = new ArrayList<Spielkarten>(4);
	private ArrayList<ArrayList<Spielkarten>> sticheDerSpieler[] = new ArrayList[4];
	private int austeilenderSpieler=2;
	private int ausspielenderSpieler=0;
	
	public SchafkopfSpielsituation() {
		teileAus();
	}

	public SchafkopfSpielsituation clone() {
		return this;
	}
	
	public int isSpielBeendet() {
		return -1;
	}
	
	public void teileAus() {
		austeilenderSpieler++;
		if (austeilenderSpieler>3)
			austeilenderSpieler=0;

		ausspielenderSpieler = austeilenderSpieler+1;
		if (ausspielenderSpieler>3)
			ausspielenderSpieler=0;
		spielerAmZug = ausspielenderSpieler;
		
		ArrayList<Spielkarten> stapel = new ArrayList<Spielkarten>(32);
		for (Spielkarten sp : Spielkarten.values())
			stapel.add(sp);
		
		Random rnd = new Random();
		
		for (int sp=0;sp<=3;sp++) {
			sticheDerSpieler[sp]=new ArrayList<ArrayList<Spielkarten>>(8);
			spielabsichtDerSpieler[sp]=null;
			kartenDerSpieler[sp] = new ArrayList<Spielkarten>(8);
			
			for (int i=1;i<=8;i++) {
				kartenDerSpieler[sp].add(stapel.remove(rnd.nextInt(stapel.size())));
			}
			
			//Collections.sort(kartenDerSpieler[sp], Collections.reverseOrder( new Sauspiel(Farben.EICHEL) )); 
		}
		
		stich = new ArrayList<Spielkarten>(4);
		
		aktuellesSpiel = null;
		spielerMitSpielabsicht = new ArrayList<Integer>(4);
		status = Status.WARTE_AUF_SPIELEN_JA_NEIN;
	}

	public List<ISpielart> getMoeglicheSpiele() {
		return getMoeglicheSpiele(spielerAmZug);
	}

	public List<ISpielart> getMoeglicheSpiele(int sp) {
		ArrayList<ISpielart> moeglSpiele = new ArrayList<ISpielart>();
		
		if (aktuellesSpiel == null && (spielerMitSpielabsicht.indexOf(sp)==0 || spielerMitSpielabsicht.size()==0) ) {
			ArrayList<Spielkarten> eichelKarten = Sauspiel.getKartenDerFarbeStatic(kartenDerSpieler[sp], Farben.EICHEL);
			if ( eichelKarten.size()>0 && !eichelKarten.contains(Spielkarten.As_Eichel) )
				moeglSpiele.add(new Sauspiel(Farben.EICHEL));
	
			ArrayList<Spielkarten> grasKarten = Sauspiel.getKartenDerFarbeStatic(kartenDerSpieler[sp], Farben.GRAS);
			if ( grasKarten.size()>0 && !grasKarten.contains(Spielkarten.As_Gras) )
				moeglSpiele.add(new Sauspiel(Farben.GRAS));
	
			ArrayList<Spielkarten> schellenKarten = Sauspiel.getKartenDerFarbeStatic(kartenDerSpieler[sp], Farben.SCHELLEN);
			if ( schellenKarten.size()>0 && !schellenKarten.contains(Spielkarten.As_Schellen) )
				moeglSpiele.add(new Sauspiel(Farben.SCHELLEN));
		}
		
		if (aktuellesSpiel == null || aktuellesSpiel.getSpielartValue() < Wenz.getSpielartValueStatic()) {
			moeglSpiele.add(new Wenz());
			moeglSpiele.add(new Wenz(Farben.EICHEL));
			moeglSpiele.add(new Wenz(Farben.GRAS));
			moeglSpiele.add(new Wenz(Farben.HERZ));
			moeglSpiele.add(new Wenz(Farben.SCHELLEN));
		}

		if (aktuellesSpiel == null || aktuellesSpiel.getSpielartValue() < Geier.getSpielartValueStatic()) {
			moeglSpiele.add(new Wenz());
			moeglSpiele.add(new Wenz(Farben.EICHEL));
			moeglSpiele.add(new Wenz(Farben.GRAS));
			moeglSpiele.add(new Wenz(Farben.HERZ));
			moeglSpiele.add(new Wenz(Farben.SCHELLEN));
		}

		if (aktuellesSpiel == null || aktuellesSpiel.getSpielartValue() < Solo.getSpielartValueStatic()) {
			//moeglSpiele.add(new Solo());
			moeglSpiele.add(new Solo(Farben.EICHEL));
			moeglSpiele.add(new Solo(Farben.GRAS));
			moeglSpiele.add(new Solo(Farben.HERZ));
			moeglSpiele.add(new Solo(Farben.SCHELLEN));
		}

		return Collections.unmodifiableList(moeglSpiele);
	}

	/**
	 * Gibt die Karten des aktuellen Spielers zurück.
	 * @return Die Karten des aktuellen Spielers als List.
	 */
	public List<Spielkarten> getSpielerkarten() {
		return getSpielerkarten(spielerAmZug);
	}

	/**
	 * Gibt die Karten eines Spielers zurück.
	 * @param sp Der Index des Spielers, dessen Karten ermittelt werden sollen.
	 * @return Die Karten eines Spielers als List.
	 */
	public List<Spielkarten> getSpielerkarten(int sp) {
		return Collections.unmodifiableList(kartenDerSpieler[sp]);
	}

	public void sortSpielerkarten(ISpielart spiel) {
		sortSpielerkarten(spielerAmZug, spiel );
	}

	public void sortSpielerkarten(int sp, ISpielart spiel) {
		Collections.sort(kartenDerSpieler[sp], Collections.reverseOrder(spiel) );
	}

	/**
	 * Gibt den aktuellen Stich zurück.
	 * @return Der aktuelle Stich.
	 */
	public List<Spielkarten> getStich() {
		return Collections.unmodifiableList(stich);
	}

	/**
	 * Gibt die bisherigen Stiche der Spieler zurück.
	 * @param spieler Der Index des Spielers, dessen Stiche zurückgegeben werden sollen.
	 * @return Die Stiche des Spielers als List.
	 */
	public List<ArrayList<Spielkarten>> getSticheDerSpieler(int spieler) {
		return Collections.unmodifiableList(sticheDerSpieler[spieler]);
	}

	/**
	 * Gibt den Index des Spielers zurück, der diese Runde ausgeteilt hat.
	 * Der nachfolgende Spieler spielt beim ersten Stich aus.
	 * @return Der Index des Spielers, der diese Runde ausgeteilt hat.
	 */
	public int getAusteilenderSpieler() {
		return austeilenderSpieler;
	}
	
	/**
	 * Gibt den Index des Spielers zurück, der bei diesem Stich ausgespielt hat. 
	 * @return Der Index des Spielers, der bei diesem Stich ausgespielt hat.
	 */
	public int getAusspielenderSpieler() {
		return ausspielenderSpieler;
	}

	public List<Integer> getSpielerMitSpielabsicht() {
		return Collections.unmodifiableList(spielerMitSpielabsicht);
	}

	public ISpielart[] getSpielabsichtDerSpieler() {
		return spielabsichtDerSpieler;
	}

	public void spielenJaNein(boolean ja) throws Exception {
		if (status != Status.WARTE_AUF_SPIELEN_JA_NEIN)
			throw new Exception("Falscher Status für spielenJaNein!");
		
		if (ja) {
			spielerMitSpielabsicht.add(spielerAmZug);
			assert spielerMitSpielabsicht.size()>0 && spielerMitSpielabsicht.size()<=4;
		}

		if (spielerAmZug==austeilenderSpieler) {
			if (spielerMitSpielabsicht.size()==0) {
				keinerSpielt();
			}
			else {
				status = Status.WARTE_AUF_SPIELARTWAHL;
				spielerMitSpielabsichtInt = (ArrayList<Integer>) spielerMitSpielabsicht.clone();
				spielerAmZug = spielerMitSpielabsichtInt.remove(0);
			}
		}
		else {
			spielerAmZug++;
			if (spielerAmZug>3)
				spielerAmZug=0;
		}
	}
	
	public void spielenJaNein(Spielen zug) throws Exception {
		spielenJaNein(zug.isSpielen());
	}

	public void waehleSpielart(Spielartwahl zug) throws Exception {
		if (status != Status.WARTE_AUF_SPIELARTWAHL)
			throw new Exception("Falscher Status für die Spielartwahl!");
		
		if (zug.getGewaehltesSpiel() != null) {
			aktuellesSpiel = zug.getGewaehltesSpiel();
			spielabsichtDerSpieler[spielerAmZug] = aktuellesSpiel;
		}
		else if (aktuellesSpiel == null) {
			throw new IllegalArgumentException("Der erste spielende Spieler muss etwas spielen!");
		}
		else if (aktuellesSpiel instanceof Sauspiel) {
			throw new IllegalArgumentException("Man muss ein Solo spielen!");
		}

		if (spielerMitSpielabsichtInt.size()>0) {
			spielerAmZug = spielerMitSpielabsichtInt.remove(0);
		}
		else {
			if (aktuellesSpiel == null) {
				teileAus();
				return;
			}
			status = Status.WARTE_AUF_AUSSPIELEN;
			spielerAmZug = ausspielenderSpieler;
		}
	}

	private void keinerSpielt() {
		if (true) {
			teileAus();
		}
		//TODO: Ramsch
	}
	
	/**
	 * Spielt eine Karte aus.
	 * @param zug Die Information über eine auszuspielende Karte.
	 */
	public void ausspielen(Ausspielvorgang zug) throws Exception {
		if (status != Status.WARTE_AUF_AUSSPIELEN)
			throw new Exception("Falscher Status für den Ausspielvorgang!");
		
		ArrayList<Spielkarten> eigeneKarten = kartenDerSpieler[spielerAmZug];
		Spielkarten zuSpielendeKarte = zug.getAusgespielteKarte();
		
		if (!eigeneKarten.contains(zuSpielendeKarte)) {
			throw new IllegalArgumentException("Der aktuelle Spieler hat diese Karte nicht!");
		}
		
		try {
			aktuellesSpiel.prüfeAufGültigeKarte(this, zuSpielendeKarte);
			
			if (stich.size()==4) {
				stich = new ArrayList<Spielkarten>(4);
			}
			
			stich.add(zuSpielendeKarte);
			eigeneKarten.remove(zuSpielendeKarte);

			if (stich.size()==4) {
				System.out.println("Stich mit " + getPunkte(stich) + " Punkten.");
				
				Spielkarten hk = aktuellesSpiel.getHoechsteKarte(stich);
				stichgewinner = (ausspielenderSpieler + stich.indexOf(hk)) % 4;
				sticheDerSpieler[stichgewinner].add(stich);
				
				status = Status.WARTE_AUF_STICHBESTAETIGUNG;
			}

			spielerAmZug++;
				
			if (spielerAmZug>3)
				spielerAmZug=0;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void bestaetigeStich(Stichbestaetigung sb) throws Exception {
		if (status != Status.WARTE_AUF_STICHBESTAETIGUNG)
			throw new Exception("Falscher Status für Stichbestätigung!");

		spielerAmZug++;
		
		if (spielerAmZug>3)
			spielerAmZug=0;

		if (spielerAmZug==ausspielenderSpieler) {
			if (getSpielerkarten().size()==0) {
				System.out.println("Spiel beendet:");
				for (int i=0;i<4;i++) {
					int punkte = 0;
					
					for (ArrayList<Spielkarten> stich : sticheDerSpieler[i]) {
						punkte += getPunkte(stich);
					}
					System.out.println("  Spieler " + i + " hat " + punkte + " Punkte.");
				}
				//TODO: aktuellesSpiel.auswerten(this);
				
				teileAus();
			}
			else {
				status = Status.WARTE_AUF_AUSSPIELEN;
				stich = new ArrayList<Spielkarten>(4);
				ausspielenderSpieler = stichgewinner;
				spielerAmZug = stichgewinner;
			}
		}
		else {
		}
	}
	
	/**
	 * Gibt die Farbe einer Spielkarte zurück.
	 * @param sp Die Spielkarte, deren Farbe bestimmt werden soll.
	 * @return Der Farbwert der Spielkarte aus der Enumeration Farben.
	 */
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
			return Farben.EICHEL;
			
		case Sieben_Gras: 
		case Acht_Gras:
		case Neun_Gras: 
		case Zehn_Gras:
		case Unter_Gras: 
		case Ober_Gras:
		case König_Gras: 
		case As_Gras:
			return Farben.GRAS;
			
		case Sieben_Herz: 
		case Acht_Herz:
		case Neun_Herz: 
		case Zehn_Herz:
		case Unter_Herz: 
		case Ober_Herz:
		case König_Herz: 
		case As_Herz:
			return Farben.HERZ;
			
		case Sieben_Schellen: 
		case Acht_Schellen:
		case Neun_Schellen: 
		case Zehn_Schellen:
		case Unter_Schellen: 
		case Ober_Schellen:
		case König_Schellen: 
		case As_Schellen:
			return Farben.SCHELLEN;
		}
		
		throw new IllegalArgumentException();
	}

	/**
	 * Berechnet die Punktzahl eines Stiches.
	 * @param stich Der Stich, der berechnet werden soll.
	 * @return Die Punktzahl des Stiches.
	 */
	public static int getPunkte(Collection<Spielkarten> stich) {
		int punkte = 0;
		
		for (Spielkarten karte : stich)
			punkte += getPunkte(karte);
		
		return punkte;
	}

	/**
	 * Gibt die Punktzahl einer Spielkarte zurück.
	 * @param sp Die Spielkarte, dessen Punktzahl zurückgegeben werden soll.
	 * @return Die Punktzahl der Spielkarte.
	 */
	public static int getPunkte(Spielkarten sp) {
		switch (getBild(sp)) {
		case AS:
			return 11;
		case ZEHN:
			return 10;
		case KÖNIG:
			return 4;
		case OBER:
			return 3;
		case UNTER:
			return 2;
		}
		
		return 0;
	}

	/**
	 * Gibt für eine Spielkarte das entsprechende Bild/Symbol/Zahl zurück.
	 * @param sp Die Spielkarte, für die das Bild zurückgegeben werden soll.
	 * @return Das Bild der Spielkarte aus der Enumeration Bilder. 
	 */
	public static Bilder getBild(Spielkarten sp) {
		switch(sp) {
		case Sieben_Eichel: 
		case Sieben_Gras:
		case Sieben_Herz: 
		case Sieben_Schellen: 
			return Bilder.SIEBEN;
			
		case Acht_Eichel:
		case Acht_Gras:
		case Acht_Herz:
		case Acht_Schellen:
			return Bilder.ACHT;
			
		case Neun_Eichel: 
		case Neun_Gras: 
		case Neun_Herz: 
		case Neun_Schellen: 
			return Bilder.NEUN;

		case Zehn_Eichel:
		case Zehn_Gras:
		case Zehn_Herz:
		case Zehn_Schellen:
			return Bilder.ZEHN;

		case Unter_Eichel: 
		case Unter_Gras: 
		case Unter_Herz: 
		case Unter_Schellen: 
			return Bilder.UNTER;

		case Ober_Eichel:
		case Ober_Gras:
		case Ober_Herz:
		case Ober_Schellen:
			return Bilder.OBER;

		case König_Eichel: 
		case König_Gras: 
		case König_Herz: 
		case König_Schellen: 
			return Bilder.KÖNIG;

		case As_Eichel:
		case As_Gras:
		case As_Herz:
		case As_Schellen:
			return Bilder.AS;
		}
		
		throw new IllegalArgumentException();
	}

	/**
	 * Gibt den Index des Spielers zurück, der am Zug ist.
	 * @return Der Index des Spielers, der am Zug ist.
	 */
	public int getSpielerAmZug() {
		return spielerAmZug;
	}

	/**
	 * Gibt das aktuell gespielte Spiel zurück.
	 * @return Das Spiel, das aktuell gespielt wird.
	 */
	public ISpielart getAktuellesSpiel() {
		return aktuellesSpiel;
	}

	/**
	 * Setzt das zu spielende Spiel fest.
	 * @return Das Spiel, das gespielt werden soll.
	 */
	public void setAktuellesSpiel(Sauspiel aktuellesSpiel) {
		this.aktuellesSpiel = aktuellesSpiel;
	}

	/**
	 * Gibt den aktuellen Spielstatus zurück.
	 * @return Der aktuellen Spielstatus.
	 */
	public Status getStatus() {
		return status;
	}
}
