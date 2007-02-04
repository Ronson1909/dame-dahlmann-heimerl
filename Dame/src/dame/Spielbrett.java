package dame;

import java.util.ArrayList;

public class Spielbrett implements Cloneable {
	
	public static final int LEER = 0;
	public static final int SCHWARZ = 1;
	public static final int WEISS = 2;
	public static final int SCHWARZ_D = 3;
	public static final int WEISS_D = 4;
	
	private boolean schwarzAmZug;
	private int[][] spielbrett;
	
	public static void main(String[] args) {
		new Spielbrett();
	}
	/**
	 * Erzeugt ein neues Spielbrett mit der Standard-Aufstellung. 
	 * Schwarz ist unten auf dem Brett und am Zug.
	 */
	public Spielbrett() {
		spielbrett = new int[8][8];
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				if ((y+x)%2 == 0) {
					if (y<3)
						spielbrett[x][y] = SCHWARZ;
					else if (y>4)
						spielbrett[x][y] = WEISS;
					else
						spielbrett[x][y] = LEER;
				}
				else
					spielbrett[x][y] = -1;
			}
		}
		schwarzAmZug = true;
		//gibAus();
	}
	
	/**
	 * Gibt die aktuelle Belegung des gewünschten Feldes zurück.
	 */
	public int gibFeld(int x, int y) {
		return spielbrett[x][y];
	}
	
	/**
	 * Ändert die Ausrichtung des Brettes auf die Sichtweise von Spieler Schwarz
	 */
	public void schwarzAmZug() {
		if (!schwarzAmZug) {
			int temp;
			for (int y=0; y<3; y++) {
				for (int x=0; x<8; x++) {
					temp = spielbrett[x][y];
					spielbrett[x][y] = spielbrett[7-x][7-y];
					spielbrett[7-x][7-y] = temp;
				}
			}
			schwarzAmZug = true;
		}
	}
	
	/**
	 * Ändert die Ausrichtung des Brettes auf die Sichtweise von Spieler Weiss
	 */
	public void weissAmZug() {
		if (schwarzAmZug) {
			int temp;
			for (int y=0; y<3; y++) {
				for (int x=0; x<8; x++) {
					temp = spielbrett[x][y];
					spielbrett[x][y] = spielbrett[7-x][7-y];
					spielbrett[7-x][7-y] = temp;
				}
			}
			schwarzAmZug = false;
		}
	}
	
	/**
	 * Prüft ob der Zug gültig ist.
	 */
	public boolean zugIstGueltig(Zug z) {
		return zugIstGueltig(z, true);
	}
	
	/**
	 * Prüft ob der Zug gültig ist.
	 */
	public boolean zugIstGueltig(Zug z, boolean testeObEigener) {
		int x1 = z.gibStartX();
		int y1 = z.gibStartY();
		int x2 = z.gibEndeX();
		int y2 = z.gibEndeY();
		
		//prüfe: Hat Zug gültige Koordinaten?
		if (!z.hatGueltigeKoordinaten())
			return false;
		
        //prüfe: Ziel ist freies Feld
		if (spielbrett[x2][y2] != LEER)
			return false;
		
		//prüfe: ist eigener Stein falls erster Zug
		//if (testeObEigener && (!((spielbrett[x1][y1] == SCHWARZ || spielbrett[x1][y1] == SCHWARZ_D) && schwarzAmZug) && !((spielbrett[x1][y1] == WEISS || spielbrett[x1][y1] == WEISS_D) && !schwarzAmZug))) //vermutlich falsch
		if (testeObEigener) {
			if (schwarzAmZug) {
				if (spielbrett[x1][y1] != SCHWARZ && spielbrett[x1][y1] != SCHWARZ_D)
					return false;
			}
			else {
				if (spielbrett[x1][y1] != WEISS && spielbrett[x1][y1] != WEISS_D)
					return false;
			}
		}
		
        //wenn keine Dame
		if (spielbrett[x1][y1] == SCHWARZ || spielbrett[x1][y1] == WEISS) {
			//prüfe: richtige Richtung (nach oben)
			if (y2 <= y1)
				return false;

	        //prüfe: Felder liegen nebeneinander
			boolean angrenzendeFelder = false;
			if ((y2 == y1+1) && (x2 == x1+1 || x2 == x1-1))
				angrenzendeFelder = true;
			
	        //prüfe: oder erlaubter Sprung über Gegner
			boolean erlaubterSprung = false;
			if ((y2 == y1+2) && (x2 == x1+2 || x2 == x1-2)) {
				int uebersprungen = spielbrett[(x1+x2)/2][y1+1];
				if (schwarzAmZug) {
					if (uebersprungen == WEISS || uebersprungen == WEISS_D)
						erlaubterSprung = true;
				}
				else { //Weiss am Zug
					if (uebersprungen == SCHWARZ || uebersprungen == SCHWARZ_D)
						erlaubterSprung = true;
				}
			}
				
			if (!angrenzendeFelder && !erlaubterSprung)
				return false;
			
			//prüfe: kein Sprung aber Sprung ist möglich
			
		}
		
		//wenn Dame
		else if (spielbrett[x1][y1] == SCHWARZ_D || spielbrett[x1][y1] == WEISS_D) {
			//prüfe: erlaubter Sprung über Gegner
		}
		
		return false;
	}
	
	/**
	 * Prüft ob eine Zugfolge gültig ist.
	 */
	public boolean zugIstGueltig(ArrayList<Zug> z) {
		//Pruefe ob Zusammenhaengend
		Zug temp = z.get(0);
		int ende_x1 = temp.gibEndeX();
		int ende_y1 = temp.gibEndeY();
		int start_x2, start_y2;
		for (int i=1; i<z.size(); i++) {
			temp = z.get(i);
			start_x2 = temp.gibStartX();
			start_y2 = temp.gibStartY();
			if ((ende_x1 != start_x2) || (ende_y1 != start_y2)) {
				return false;
			}
			ende_x1 = temp.gibEndeX();
			ende_y1 = temp.gibEndeY();
		}
		
		//Pruefe ob alle einzelnen Zuege gueltig
		if (!zugIstGueltig(z.get(0)))
			return false;
		for (int i=1; i<z.size(); i++) {
			if (!zugIstGueltig(z.get(i), false))
				return false;
		}
		
		//Dann ist wohl die Zugfolge gueltig.
		return true;
	}
	
	/**
	 * Führt Zug aus.
	 */
	public void macheZug(Zug z) {
		if (!zugIstGueltig(z)) {
			throw new IllegalArgumentException("Dieser Zug ist nicht gültig!");
		}
		
	}
	
	/**
	 * Führt Zugfolge aus.
	 */
	public void macheZug(ArrayList<Zug> z) {
		
	}
	
	/**
	 * Konsolenausgabe des aktuellen Spielbretts
	 */
	public void gibAus() {
		System.out.println(" - - - - - - - - - - - - - - - - -");
		String linie;
		for (int y=7; y>=0; y--) {
			linie = "| ";
			for (int x=0; x<8; x++) {
				String symbol = "";
				switch (spielbrett[x][y]) {
					case -1 : symbol = " "; break;
					case 0 : symbol = " "; break;
					case 1 : symbol = "X"; break;
					case 2 : symbol = "O"; break;
					case 3 : symbol = "T"; break;
					case 4 : symbol = "D"; break;
				}
					
				linie += symbol + " | ";
			}
			System.out.println(linie);
			System.out.println("- - - - - - - - - - - - - - - - -");
		}
		System.out.println();
	}
	
	
	/**
	 * Methode um die Geschwindigkeit der zwei Transformierverfahren zu vergleichen.
	 */
	public void transformiere() {
		long start1 = System.currentTimeMillis();
		for (int i=0; i<1000000; i++) {
			int[][] temp = new int[8][8];
			for (int y=0; y<8; y++) {
				for (int x=0; x<8; x++) {
					temp[x][y] = spielbrett[7-x][7-y];
				}
			}
			spielbrett = temp;
		}
		long ende1 = System.currentTimeMillis();
		
		long start2 = System.currentTimeMillis();
		for (int i=0; i<1000000; i++) {
			int temp;
			for (int y=0; y<3; y++) {
				for (int x=0; x<8; x++) {
					temp = spielbrett[x][y];
					spielbrett[x][y] = spielbrett[7-x][7-y];
					spielbrett[7-x][7-y] = temp;
				}
			}
		}
		long ende2 = System.currentTimeMillis();
		System.out.println("Dauer Methode 1 = " + (ende1-start1));
		System.out.println("Dauer Methode 2 = " + (ende2-start2));
	}
	
	public Spielbrett clone() {
		return new Spielbrett(spielbrett.clone(), schwarzAmZug);
	}
	
	private Spielbrett(int[][] brett, boolean schwarzAmZug) {
		spielbrett = brett;
		this.schwarzAmZug = schwarzAmZug;
	}
}
