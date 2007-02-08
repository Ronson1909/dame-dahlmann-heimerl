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
	
	private int[][] TEMP_spielbrett;
	
	private int eigenerStein;// = schwarzAmZug ? SCHWARZ : WEISS;
	private int eigeneDame;// = schwarzAmZug ? SCHWARZ_D : WEISS_D;
	private int gegnerStein;// = schwarzAmZug ? WEISS : SCHWARZ;
	private int gegnerDame;// = schwarzAmZug ? WEISS_D : SCHWARZ_D;
	
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
		eigenerStein = SCHWARZ;
		eigeneDame = SCHWARZ_D;
		gegnerStein = WEISS;
		gegnerDame = WEISS_D;
		//gibAus();
		//System.out.println("Sprung ist m�glich ? " + sprungIstMoeglich());
		//System.out.println(zugIstGueltig(new Zug(1,5,0,4)));
	}
	
	/**
	 * Erzeugt ein neues Spielbrett mit der �bergebenen Aufstellung und dem �bergebenen aktuellen Spieler. 
	 * Diese Methode sollte nur von clone() aufgerufen werden!
	 */
	private Spielbrett(int[][] brett, boolean schwarzAmZug) {
		spielbrett = brett;
		this.schwarzAmZug = schwarzAmZug;
		eigenerStein = schwarzAmZug ? SCHWARZ : WEISS;
		eigeneDame = schwarzAmZug ? SCHWARZ_D : WEISS_D;
		gegnerStein = schwarzAmZug ? WEISS : SCHWARZ;
		gegnerDame = schwarzAmZug ? WEISS_D : SCHWARZ_D;
	}
	
	public Spielbrett clone() {
		return new Spielbrett(spielbrett.clone(), schwarzAmZug);
	}
	
	/**
	 * Gibt die aktuelle Belegung des gew�nschten Feldes zur�ck.
	 */
	public int gibFeld(int x, int y) {
		return spielbrett[x][y];
	}
	
	/**
	 * �ndert die Ausrichtung des Brettes auf die Sichtweise von Spieler Schwarz
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
			eigenerStein = SCHWARZ;
			eigeneDame = SCHWARZ_D;
			gegnerStein = WEISS;
			gegnerDame = WEISS_D;
		}
	}
	
	/**
	 * �ndert die Ausrichtung des Brettes auf die Sichtweise von Spieler Weiss
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
			eigenerStein = WEISS;
			eigeneDame = WEISS_D;
			gegnerStein = SCHWARZ;
			gegnerDame = SCHWARZ_D;
		}
	}
	
	/**
	 * Pr�ft ob f�r den aktuellen Spieler �berhaupt ein Sprung m�glich ist.
	 */
	public boolean sprungIstMoeglich() {

		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				if ((y+x)%2 != 0) continue; //Nur schwarze Felder betrachten
				
				//falls normeler Stein auf betrachtetem Feld
				if (spielbrett[x][y] == eigenerStein) {
					if ( koordinatenGueltig(x-2,y+2) && 
							spielbrett[x-2][y+2] == LEER && 
							(spielbrett[x-1][y+1] == gegnerStein || 
									spielbrett[x-1][y+1] == gegnerDame) ) {
						return true;
					}
					if ( koordinatenGueltig(x+2,y+2) && 
							spielbrett[x+2][y+2] == LEER && 
							(spielbrett[x+1][y+1] == gegnerStein || 
									spielbrett[x+1][y+1] == gegnerDame) ) {
						return true;
					}
				}
				//falls Dame auf betrachtetem Feld
				else if (spielbrett[x][y] == eigeneDame) {
					int testX, testY;
					
					//Teste nach unten links
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX-1, testY-1) && spielbrett[testX-1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX--;
						testY--;
					}
					if ( koordinatenGueltig(testX-2,testY-2) && 
							spielbrett[testX-2][testY-2] == LEER && 
							(spielbrett[testX-1][testY-1] == gegnerStein || 
									spielbrett[testX-1][testY-1] == gegnerDame) ) {
						return true;
					}
					
					//Teste nach unten rechts
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX+1, testY-1) && spielbrett[testX+1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX++;
						testY--;
					}
					if ( koordinatenGueltig(testX+2,testY-2) && 
							spielbrett[testX+2][testY-2] == LEER && 
							(spielbrett[testX+1][testY-1] == gegnerStein || 
									spielbrett[testX+1][testY-1] == gegnerDame) ) {
						return true;
					}
					
					//Teste nach oben rechts
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX+1, testY+1) && spielbrett[testX+1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX++;
						testY++;
					}
					if ( koordinatenGueltig(testX+2,testY+2) && 
							spielbrett[testX+2][testY+2] == LEER && 
							(spielbrett[testX+1][testY+1] == gegnerStein || 
									spielbrett[testX+1][testY+1] == gegnerDame) ) {
						return true;
					}
					
					//Teste nach oben links
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX-1, testY+1) && spielbrett[testX-1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX--;
						testY--;
					}
					if ( koordinatenGueltig(testX-2,testY+2) && 
							spielbrett[testX-2][testY+2] == LEER && 
							(spielbrett[testX-1][testY+1] == gegnerStein || 
									spielbrett[testX-1][testY+1] == gegnerDame) ) {
						return true;
					}
				} //end-if Dame auf betrachtetem Feld
			} //end-for x
		} //end-for y
		return false;
	}
	
	/**
	 * Pr�ft ob f�r den aktuellen Spieler ausgehend vom Feld mit den �bergebenen Koordinaten (x,y) und gespieltem Stein (normal/Dame) noch ein weiterer Sprung m�glich ist.
	 */
	private boolean weitererSprungIstMoeglich(int x, int y, boolean sprungMitDame) {
				
		//falls mit normelem Stein gesprungen
		if (!sprungMitDame) {
			if ( koordinatenGueltig(x-2,y+2) && 
					spielbrett[x-2][y+2] == LEER && 
					(spielbrett[x-1][y+1] == gegnerStein || 
							spielbrett[x-1][y+1] == gegnerDame) ) {
				return true;
			}
			if ( koordinatenGueltig(x+2,y+2) && 
					spielbrett[x+2][y+2] == LEER && 
					(spielbrett[x+1][y+1] == gegnerStein || 
							spielbrett[x+1][y+1] == gegnerDame) ) {
				return true;
			}
		}
		//falls mit Dame gesprungen //Ausschlie�en dass vorherige Spr�nge der Zugfolge wieder als M�glichkeiten gefunden werden: Arbeiten auf TEMP_spielbrett
		else {
			int testX, testY;
			
			//Teste nach unten links
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX-1, testY-1) && TEMP_spielbrett[testX-1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX--;
				testY--;
			}
			if ( koordinatenGueltig(testX-2,testY-2) && 
					TEMP_spielbrett[testX-2][testY-2] == LEER && 
					(TEMP_spielbrett[testX-1][testY-1] == gegnerStein || 
							TEMP_spielbrett[testX-1][testY-1] == gegnerDame) ) {
				return true;
			}
			
			//Teste nach unten rechts
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX+1, testY-1) && TEMP_spielbrett[testX+1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX++;
				testY--;
			}
			if ( koordinatenGueltig(testX+2,testY-2) && 
					TEMP_spielbrett[testX+2][testY-2] == LEER && 
					(TEMP_spielbrett[testX+1][testY-1] == gegnerStein || 
							TEMP_spielbrett[testX+1][testY-1] == gegnerDame) ) {
				return true;
			}
			
			//Teste nach oben rechts
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX+1, testY+1) && TEMP_spielbrett[testX+1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX++;
				testY++;
			}
			if ( koordinatenGueltig(testX+2,testY+2) && 
					TEMP_spielbrett[testX+2][testY+2] == LEER && 
					(TEMP_spielbrett[testX+1][testY+1] == gegnerStein || 
							TEMP_spielbrett[testX+1][testY+1] == gegnerDame) ) {
				return true;
			}
			
			//Teste nach oben links
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX-1, testY+1) && TEMP_spielbrett[testX-1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX--;
				testY--;
			}
			if ( koordinatenGueltig(testX-2,testY+2) && 
					TEMP_spielbrett[testX-2][testY+2] == LEER && 
					(TEMP_spielbrett[testX-1][testY+1] == gegnerStein || 
							TEMP_spielbrett[testX-1][testY+1] == gegnerDame) ) {
				return true;
			}
		} //end-if Dame auf betrachtetem Feld
		return false;
	}
	
	/**
	 * Pr�ft ob der Zug g�ltig ist.
	 */
	private boolean zugIstGueltig(Zug z) {
		return zugIstGueltig(z, true, true, false);
	}
	
	/**
	 * Pr�ft ob eine Zugfolge g�ltig ist.
	 */
	private boolean zugIstGueltig(ArrayList<Zug> z) {
		if (z.size() <= 1) return false;
		
		//Pruefe ob Zusammenhaengend
		Zug temp = z.get(0);
		boolean zugMitDame = (spielbrett[temp.gibStartX()][temp.gibStartY()] == eigeneDame);
		
		if (zugMitDame)
			TEMP_spielbrett = spielbrett.clone(); //Um die �bersprungenen Steine f�r die Tests entfernen zu k�nnen
		
		int ende_x1 = temp.gibEndeX();
		int ende_y1 = temp.gibEndeY();
		int start_x2, start_y2;
		for (int i=1; i<z.size(); i++) { //Z�ge durchgehen
			temp = z.get(i);
			start_x2 = temp.gibStartX();
			start_y2 = temp.gibStartY();
			if ((ende_x1 != start_x2) || (ende_y1 != start_y2)) { //Pr�fen ob Ende des vorherigen = Start des aktuellen
				return false;
			}
			ende_x1 = temp.gibEndeX();
			ende_y1 = temp.gibEndeY();
		}
		
		//Pr�fe ob alle einzelnen Z�ge g�ltig
		if (!zugIstGueltig(z.get(0), true, false, zugMitDame)) //Beim ersten Zug auch pr�fen ob Ausgangspunkt eigener Stein
			return false;
		for (int i=1; i<z.size()-1; i++) {
			if (!zugIstGueltig(z.get(i), false, false, zugMitDame)) //Bei den mittleren Z�gen Korrektheit ohne Sonderf�lle pr�fen
				return false;
		}
		if (!zugIstGueltig(z.get(z.size()-1), false, true, zugMitDame)) //Beim letzten Sprung pr�fen, ob weiterer Sprung m�glich w�re
			return false;
		
		//Dann ist wohl die Zugfolge gueltig.
		return true;
	}
	
	/**
	 * Pr�ft ob der Zug g�ltig ist.
	 */
	private boolean zugIstGueltig(Zug z, boolean ersterZug, boolean letzterZug, boolean zugMitDame) {
		boolean zugFolge = !(ersterZug && letzterZug); //Wenn zugleich erster und letzter Zug, dann keine Zugfolge
		int x1 = z.gibStartX();
		int y1 = z.gibStartY();
		int x2 = z.gibEndeX();
		int y2 = z.gibEndeY();
		
		//pr�fe: Hat Zug g�ltige Koordinaten?
		if (!z.hatGueltigeKoordinaten())
			return false;
		
        //pr�fe: Ziel ist freies Feld
		if (spielbrett[x2][y2] != LEER)
			return false;
		
		//pr�fe: ist eigener Stein falls erster Zug
		if (ersterZug) { //alt.: if(!zugFolge) {
			zugMitDame = (spielbrett[x1][y1] == eigeneDame); //Beim ersten Zug wird bei einem einzelnen Zug nicht �bergeben, ob mit einer Dame gezogen wurde.
			if (spielbrett[x1][y1] != eigenerStein && spielbrett[x1][y1] != eigeneDame)
				return false;
		}
		//
        //wenn keine Dame
		if (!zugMitDame) {
			//pr�fe: richtige Richtung (nach oben)
			if (y2 <= y1)
				return false;

	        //pr�fe: Felder liegen nebeneinander
			boolean angrenzendeFelder = false;
			if ((y2 == y1+1) && (x2 == x1+1 || x2 == x1-1))
				angrenzendeFelder = true;
			
	        //pr�fe: oder erlaubter Sprung �ber Gegner
			boolean erlaubterSprung = false;
			if ((y2 == y1+2) && (x2 == x1+2 || x2 == x1-2)) {
				int uebersprungen = spielbrett[(x1+x2)/2][y1+1];
				if (uebersprungen == gegnerStein || uebersprungen == gegnerDame)
					erlaubterSprung = true;
			}
			
			//pr�fe: Felder liegen nicht nebeneinander und gesprungen wurde auch nicht
			if (!angrenzendeFelder && !erlaubterSprung)
				return false;
			
			//pr�fe: Zugfolge darf nur aus Spr�ngen bestehen
			if (zugFolge && !erlaubterSprung) //Sollte gleichbedeutend sein mit if (zugFolge && angrenzendeFelder)
				return false;
			
			//pr�fe: Es wurde gesprungen, aber nicht alle m�glichen Spr�nge wurden ausgef�hrt.
			if (letzterZug && erlaubterSprung) {
				if (weitererSprungIstMoeglich(x2, y2, false))
					return false;
			}
			
			//pr�fe: kein Sprung aber Sprung ist m�glich (Zugfolge ist schon ausgeschlossen)
			if (!erlaubterSprung) { //identisch mit if (angrenzendeFelder) {
				if (sprungIstMoeglich())
					return false;
			}
			
			return true;
		} //end-if keine Dame
		
		//wenn Dame
		else if (zugMitDame) {
			//pr�fe: erlaubter Sprung �ber Gegner
			//...
			
			//pr�fe: Zielfeld auf einer der Diagonalen vom Startfeld aus
			if (Math.abs(x2-x1) != Math.abs(y2-y1))
				return false;
			
			//pr�fe: Alle Felder zwischen Startfeld und einem Feld vor Zielfeld leer
			//x- und ySchritt von Ursprungsposition aus gesehen!
			int xSchritt = (int) Math.signum(x2-x1); //(x2 > x1) ? +1 : -1;
			int ySchritt = (int) Math.signum(y2-y1); //(y2 > y1) ? +1 : -1;
			int testX=x1+xSchritt, testY=y1+ySchritt;
			while (testX < x2-xSchritt && testY < y2-ySchritt) {
				if (spielbrett[testX][testY] != LEER)
					return false;
				testX += xSchritt;
				testY += ySchritt;
			}
			if (testX != x2-xSchritt || testY != y2-ySchritt)
				System.out.println("ProgrammierFEHLER!!!");
			
			boolean sprung = (spielbrett[testX][testY] == LEER) ? false : true;
				
			//pr�fe: oder erlaubter Sprung �ber Gegner => Nur ein Feld vor Zielfeld mit gegnerischem Stein belegt, sonst alle leer
			boolean erlaubterSprung = false;
			if (spielbrett[testX][testY] == gegnerStein || spielbrett[testX][testY] == gegnerDame)
				erlaubterSprung = true;

			//pr�fe: Sprung, aber inkorrekt
			if (sprung && !erlaubterSprung)
				return false;
			
			//pr�fe: Zugfolge darf nur aus Spr�ngen bestehen
			if (zugFolge && !sprung)
				return false;
			
			//pr�fe: kein Sprung aber Sprung ist m�glich (Zugfolge ist schon ausgeschlossen)
			if (!sprung) {
				if (sprungIstMoeglich())
					return false;
			}
			
			 //auf dem tempor�ren Brett den �bersprungenen Stein entfernen
			if (zugFolge) {
				//x- und yKorrektur vom Zielfeld aus gesehen um �bersprungen Stein zu erreichen
				int xKorrektur = (int) Math.signum(x1-x2); //(x2 > x1) ? -1 : +1;
				int yKorrektur = (int) Math.signum(y1-y2); //(y2 > y1) ? -1 : +1;
				
				if (xKorrektur != -1*xSchritt || yKorrektur != -1*ySchritt)
					System.out.println("PROGRAMMIERFEHLER!");
					//falls das nie auftritt k�nnen die zwei int-Werte hier dr�ber weg.
				
				//TEMP_spielbrett[x2+xKorrektur][y2+yKorrektur] = LEER;
				//TEMP_spielbrett[x2-xSchritt][y2-ySchritt] = LEER;
				TEMP_spielbrett[testX][testY] = LEER;
			}
			
			//pr�fe: Es wurde gesprungen, aber nicht alle m�glichen Spr�nge wurden ausgef�hrt. (Wird anhand TEMP_spielbrett gepr�ft).
			if (letzterZug && erlaubterSprung) {
				if (weitererSprungIstMoeglich(x2, y2, true))
					return false;
			}
			
			return true;
		}
		
		else { //Sollte nie erreicht werden!
			System.out.println("Irgendwo in zugIstGueltig() muss ein FEHLER sein!");
			return false;
		}
	}
	
	/**
	 * F�hrt Zug aus.
	 */
	public void macheZug(Zug z) {
		if (!zugIstGueltig(z)) {
			throw new IllegalArgumentException("Dieser Zug ist nicht g�ltig!");
		}
		int x1 = z.gibStartX();
		int y1 = z.gibStartY();
		int x2 = z.gibEndeX();
		int y2 = z.gibEndeY();
		
		if (y2 == 7) //Oberste Zeile -> Umwandelung zur Dame
			spielbrett[x2][y2] = eigeneDame;
		else
			spielbrett[x2][y2] = spielbrett[x1][y1];
		
		spielbrett[x1][y1] = LEER;
		//x- und yKorrektur vom Zielfeld aus gesehen um �bersprungen Stein zu erreichen
		int xKorrektur = (int) Math.signum(x1-x2); //(x2 > x1) ? -1 : +1;
		int yKorrektur = (int) Math.signum(y1-y2); //(y2 > y1) ? -1 : +1;
		spielbrett[x2+xKorrektur][y2+yKorrektur] = LEER;
	}
	
	/**
	 * F�hrt Zugfolge aus.
	 */
	public void macheZug(ArrayList<Zug> z) {
		if (!zugIstGueltig(z)) {
			throw new IllegalArgumentException("Dieser Zug ist nicht g�ltig!");
		}
		
	}
	
	/**
	 * Pr�ft ob die Koordinaten g�ltig sind
	 */
	private boolean koordinatenGueltig(int x, int y) {
        //Feldbegrenzungen
        if (y<0 || y>7)
            return false;

        //Feldbegrenzungen
        if (x<0 || x>7)
            return false;

        //wei�e Felder sind ung�ltig
        if (x%2 != y%2)
            return false;

        return true;
    }
	
	/**
	 * Konsolenausgabe des aktuellen Spielbretts
	 */
	private void gibAus() {
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
//	private void transformiere() {
//		long start1 = System.currentTimeMillis();
//		for (int i=0; i<1000000; i++) {
//			int[][] temp = new int[8][8];
//			for (int y=0; y<8; y++) {
//				for (int x=0; x<8; x++) {
//					temp[x][y] = spielbrett[7-x][7-y];
//				}
//			}
//			spielbrett = temp;
//		}
//		long ende1 = System.currentTimeMillis();
//		
//		long start2 = System.currentTimeMillis();
//		for (int i=0; i<1000000; i++) {
//			int temp;
//			for (int y=0; y<3; y++) {
//				for (int x=0; x<8; x++) {
//					temp = spielbrett[x][y];
//					spielbrett[x][y] = spielbrett[7-x][7-y];
//					spielbrett[7-x][7-y] = temp;
//				}
//			}
//		}
//		long ende2 = System.currentTimeMillis();
//		System.out.println("Dauer Methode 1 = " + (ende1-start1));
//		System.out.println("Dauer Methode 2 = " + (ende2-start2));
//	}
}
