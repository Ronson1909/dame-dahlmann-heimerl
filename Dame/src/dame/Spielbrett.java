package dame;

import java.util.ArrayList;
import java.io.*;
import java.util.HashMap;

public class Spielbrett implements Cloneable, Serializable {
	
	public static final int LEER = 0;
	public static final int SCHWARZ = 1;
	public static final int WEISS = 2;
	public static final int SCHWARZ_D = 3;
	public static final int WEISS_D = 4;
	
	private boolean schwarzAmZug;
	private int[][] spielbrett;
	private int[][] spielbrett_intern;
	
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
		schwarzAmZug = true;
		eigenerStein = SCHWARZ;
		eigeneDame = SCHWARZ_D;
		gegnerStein = WEISS;
		gegnerDame = WEISS_D;
		
		spielbrett = new int[8][8];
		spielbrett_intern = new int[8][8];
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				if ((y+x)%2 == 0) {
					if (y<3)
						sb_set(x, y, SCHWARZ);
					else if (y>4)
						sb_set(x, y, WEISS);
					else
						sb_set(x, y, LEER);
				}
				else
					sb_set(x, y, -1);
			}
		}
		
		
		boolean konsolenspiel = false;
		if (konsolenspiel) {
			gibAus();
			HashMap<String, Integer> zahlen = new HashMap<String, Integer>();
			zahlen.put("A", 1); zahlen.put("B", 2); zahlen.put("C", 3); zahlen.put("D", 4);
			zahlen.put("E", 5); zahlen.put("F", 6); zahlen.put("G", 7); zahlen.put("H", 8);
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
				String s;
				String[] folge, koord;
				int x1, y1, x2, y2;
				while((s = in.readLine()) != null && !s.equals("exit")) {//&& s.length() != 0)
					
					folge = s.split(",");
					if (folge.length == 1) {
						koord = folge[0].split("-");
						x1 = zahlen.get(String.valueOf(koord[0].charAt(0))) -1;
						y1 = Integer.parseInt(String.valueOf(koord[0].charAt(1))) -1;
						x2 = zahlen.get(String.valueOf(koord[1].charAt(0))) -1;
						y2 = Integer.parseInt(String.valueOf(koord[1].charAt(1))) -1;
						
						System.out.println(x1 +" "+ y1 +" "+ x2 +" "+ y2);
						/*if (!schwarzAmZug) { //Rücktransformation
							x1 = 7-x1;
							y1 = 7-y1;
							x2 = 7-x2;
							y2 = 7-y2;
							System.out.println(x1 +" "+ y1 +" "+ x2 +" "+ y2);
						}*/
						
						//System.out.println(x1 +" "+ y1 +" "+ x2 +" "+ y2);
						Zug z = new Zug(x1, y1, x2, y2);
						try {
							macheZug(z);
						} catch (IllegalArgumentException e) { System.out.println(e.getMessage()+"\n"); continue; }
					}
					else {	
						ArrayList<Zug> z = new ArrayList<Zug>();
						for (int i=0; i<folge.length; i++) {
							koord = folge[i].split("-");
							x1 = zahlen.get(String.valueOf(koord[0].charAt(0))) -1;
							y1 = Integer.parseInt(String.valueOf(koord[0].charAt(1))) -1;
							x2 = zahlen.get(String.valueOf(koord[1].charAt(0))) -1;
							y2 = Integer.parseInt(String.valueOf(koord[1].charAt(1))) -1;
							
							System.out.println(x1 +" "+ y1 +" "+ x2 +" "+ y2);
							/*if (!schwarzAmZug) { //Rücktransformation
								x1 = 7-x1;
								y1 = 7-y1;
								x2 = 7-x2;
								y2 = 7-y2;
								System.out.println(x1 +" "+ y1 +" "+ x2 +" "+ y2);
							}*/
							
							//System.out.println(x1 +" "+ y1 +" "+ x2 +" "+ y2);
							z.add(new Zug(x1, y1, x2, y2));
						}
						try {
							macheZug(z);
						} catch (IllegalArgumentException e) { System.out.println(e.getMessage()+"\n"); continue; }
					}
					andererSpielerAmZug();
					gibAus();
				}
			} catch (IOException e) {
				// TODO Automatisch erstellter Catch-Block
				e.printStackTrace();
			}
		}//end-if Konsolenspiel
		
	}
	
	/**
	 * Erzeugt ein neues Spielbrett mit der übergebenen Aufstellung und dem übergebenen aktuellen Spieler. 
	 * Diese Methode sollte nur von clone() aufgerufen werden!
	 */
	private Spielbrett(int[][] brett, int[][] brett_intern, boolean schwarzAmZug) {
		spielbrett = brett;
		spielbrett_intern = brett_intern;
		this.schwarzAmZug = schwarzAmZug;
		eigenerStein = schwarzAmZug ? SCHWARZ : WEISS;
		eigeneDame = schwarzAmZug ? SCHWARZ_D : WEISS_D;
		gegnerStein = schwarzAmZug ? WEISS : SCHWARZ;
		gegnerDame = schwarzAmZug ? WEISS_D : SCHWARZ_D;
	}
	
	public Spielbrett clone() {
		return new Spielbrett(spielbrettKopie(spielbrett), spielbrettKopie(spielbrett_intern), schwarzAmZug);
	}
	
	/**
	 * Gibt eine Kopie des übergebenen Spielbretts zurück
	 */
	private int[][] spielbrettKopie(int[][] sb) {
		int[][] temp = new int[8][8];
		for (int i=0; i<8; i++)
			temp[i] = sb[i].clone();
		return temp;
	}
	
	/**
	 * Dreht das spielbrett_intern
	 */
	private void dreheSpielbrett_intern() {
		int temp;
		for (int y=0; y<=3; y++) {
			for (int x=0; x<8; x++) {
				temp = spielbrett_intern[x][y];
				spielbrett_intern[x][y] = spielbrett_intern[7-x][7-y];
				spielbrett_intern[7-x][7-y] = temp;
			}
		}
	}
	
	/**
	 * Setzt auf Feld (x,y) des INTERNEN Brettes und dem entsprechenden Feld auf dem spielbrett den Wert wert.
	 */
	private void sb_set(int x, int y, int wert) {
		spielbrett_intern[x][y] = wert;
		if (schwarzAmZug)
			spielbrett[x][y] = wert;
		else
			spielbrett[7-x][7-y] = wert;
	}
	
	/**
	 * Transformiert eine Koordinate eines Zuges falls nötig
	 */
	private int transform(int k) {
		if (!schwarzAmZug)
			k = 7-k;
		return k;
	}
	
	//############################################################

	/**
	 * Gibt die aktuelle Belegung des gewünschten Feldes zurück.
	 */
	public int getFeld(int x, int y) {
		return spielbrett[x][y];
	}
	
	/**
	 * Ändert die Ausrichtung des Brettes auf die Sichtweise des anderen Spielers
	 */
	public void andererSpielerAmZug() {
		if (schwarzAmZug)
			weissAmZug();
		else
			schwarzAmZug();
	}
	
	/**
	 * Ändert die Ausrichtung des Brettes auf die Sichtweise von Spieler Schwarz
	 */
	public void schwarzAmZug() {
		if (!schwarzAmZug) {
			/*int temp;
			for (int y=0; y<=3; y++) {
				for (int x=0; x<8; x++) {
					temp = spielbrett[x][y];
					spielbrett[x][y] = spielbrett[7-x][7-y];
					spielbrett[7-x][7-y] = temp;
				}
			}*/
			schwarzAmZug = true;
			eigenerStein = SCHWARZ;
			eigeneDame = SCHWARZ_D;
			gegnerStein = WEISS;
			gegnerDame = WEISS_D;
			dreheSpielbrett_intern();
		}
	}
	
	/**
	 * Ändert die Ausrichtung des Brettes auf die Sichtweise von Spieler Weiss
	 */
	public void weissAmZug() {
		if (schwarzAmZug) {
			/*int temp;
			for (int y=0; y<=3; y++) {
				for (int x=0; x<8; x++) {
					temp = spielbrett[x][y];
					spielbrett[x][y] = spielbrett[7-x][7-y];
					spielbrett[7-x][7-y] = temp;
				}
			}*/
			schwarzAmZug = false;
			eigenerStein = WEISS;
			eigeneDame = WEISS_D;
			gegnerStein = SCHWARZ;
			gegnerDame = SCHWARZ_D;
			dreheSpielbrett_intern();
		}
	}
	
	//############################################################
	
	/**
	 * Prüft ob für den aktuellen Spieler, ausgehend von Feld x,y ein Sprung in Richtung der anderen Parameter möglich ist.
	 */
	private boolean pruefeSprung(int x, int y, int xRichtung, int yRichtung, boolean aufTempBrett) {
		int xSollLeer = x+2*xRichtung;
		int ySollLeer = y+2*yRichtung;
		int xSollGegner = x+xRichtung;
		int ySollGegner = y+yRichtung;
		if (!aufTempBrett) //so wird es aus sprungIstMoeglich aufgerufen
			return ( koordinatenGueltig(xSollLeer,ySollLeer) && 
					spielbrett_intern[xSollLeer][ySollLeer] == LEER && 
					(spielbrett_intern[xSollGegner][ySollGegner] == gegnerStein || 
							spielbrett_intern[xSollGegner][ySollGegner] == gegnerDame) );
		else //so wird es aus weitererSprungIstMoeglich aufgerufen: Getestet wir auf dem TEMP_spielbrett
			return ( koordinatenGueltig(xSollLeer,ySollLeer) && 
					TEMP_spielbrett[xSollLeer][ySollLeer] == LEER && 
					(TEMP_spielbrett[xSollGegner][ySollGegner] == gegnerStein || 
							TEMP_spielbrett[xSollGegner][ySollGegner] == gegnerDame) );
	}
	
	/**
	 * Prüft ob für den aktuellen Spieler überhaupt ein Sprung möglich ist.
	 */
	public boolean sprungIstMoeglich() {

		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				if ((y+x)%2 != 0) continue; //Nur schwarze Felder betrachten
				
				//falls normeler Stein auf betrachtetem Feld
				if (spielbrett_intern[x][y] == eigenerStein) {
					if (pruefeSprung(x, y, -1, +1, false))
						return true;
					if (pruefeSprung(x, y, +1, +1, false))
						return true;
				}
				//falls Dame auf betrachtetem Feld
				else if (spielbrett_intern[x][y] == eigeneDame) {
					int testX, testY;
					
					//Teste nach unten links
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX-1, testY-1) && spielbrett_intern[testX-1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX--;
						testY--;
					}
					if (pruefeSprung(testX, testY, -1, -1, false))
						return true;
					
					//Teste nach unten rechts
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX+1, testY-1) && spielbrett_intern[testX+1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX++;
						testY--;
					}
					if (pruefeSprung(testX, testY, +1, -1, false))
						return true;
					
					//Teste nach oben rechts
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX+1, testY+1) && spielbrett_intern[testX+1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX++;
						testY++;
					}
					if (pruefeSprung(testX, testY, +1, +1, false))
						return true;
					
					//Teste nach oben links
					testX = x;
					testY = y;
					while (koordinatenGueltig(testX-1, testY+1) && spielbrett_intern[testX-1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
						testX--;
						testY++;
					}
					if (pruefeSprung(testX, testY, -1, +1, false))
						return true;
				} //end-if Dame auf betrachtetem Feld
			} //end-for x
		} //end-for y
		return false;
	}
	
	/**
	 * Prüft ob für den aktuellen Spieler ausgehend vom Feld mit den übergebenen Koordinaten (x,y) und gespieltem Stein (normal/Dame) noch ein weiterer Sprung möglich ist.
	 */
	private boolean weitererSprungIstMoeglich(int x, int y, boolean sprungMitDame) {
				
		//falls mit normelem Stein gesprungen
		if (!sprungMitDame) {
			if (pruefeSprung(x, y, -1, +1, false))
				return true;
			if (pruefeSprung(x, y, +1, +1, false))
				return true;
		}
		//falls mit Dame gesprungen //Ausschließen dass vorherige Sprünge der Zugfolge wieder als Möglichkeiten gefunden werden: Arbeiten auf TEMP_spielbrett
		else {
			int testX, testY;
			
			//Teste nach unten links
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX-1, testY-1) && TEMP_spielbrett[testX-1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX--;
				testY--;
			}
			if (pruefeSprung(testX, testY, -1, -1, true))
				return true;
			
			//Teste nach unten rechts
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX+1, testY-1) && TEMP_spielbrett[testX+1][testY-1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX++;
				testY--;
			}
			if (pruefeSprung(testX, testY, +1, -1, true))
				return true;
			
			//Teste nach oben rechts
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX+1, testY+1) && TEMP_spielbrett[testX+1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX++;
				testY++;
			}
			if (pruefeSprung(testX, testY, +1, +1, true))
				return true;
			
			//Teste nach oben links
			testX = x;
			testY = y;
			while (koordinatenGueltig(testX-1, testY+1) && TEMP_spielbrett[testX-1][testY+1] == LEER) { //Zeiger auf letztes freies Feld in der Diagonalen vor nicht-leerem Feld bzw. Spielfeldrand
				testX--;
				testY++;
			}
			if (pruefeSprung(testX, testY, -1, +1, true))
				return true;
		} //end-if Dame auf betrachtetem Feld
		return false;
	}
	
	//############################################################
	
	/**
	 * Prüft ob der Zug z ein korrekter (erster) Zug und kein Sprung ist.
	 */
	public boolean isZug(Zug z) {
		int x1 = transform(z.gibStartX());
		int y1 = transform(z.gibStartY());
		int x2 = transform(z.gibEndeX());
		int y2 = transform(z.gibEndeY());
		boolean zugMitDame = (spielbrett_intern[x1][y1] == eigeneDame);

		//Teste alles mögliche
		if (!kgV_checkObZugOderSprung(z, x1, y1, x2, y2, zugMitDame))
			return false;
		
        //wenn keine Dame
		if (!zugMitDame) {
	        //prüfe: Felder liegen nebeneinander
			if (!zugVonStein(x1, y1, x2, y2))
				return false;
			
			if (sprungIstMoeglich())
				return false;
		} //end-if keine Dame
		
		else { //wenn Dame
			//x- und ySchritt von Ursprungsposition aus gesehen!
			int xSchritt = (int) Math.signum(x2-x1); //(x2 > x1) ? +1 : -1;
			int ySchritt = (int) Math.signum(y2-y1); //(y2 > y1) ? +1 : -1;
			int testX = x2-xSchritt;
			int testY = y2-ySchritt;
			
			//prüfe: Feld vor Zielfeld muss Frei sein, da kein Sprung
			if (!isLeer_intern(testX, testY))
				return false;
				
			if (sprungIstMoeglich())
				return false;
		}
		
		return true;
	}
	
	/**
	 * Prüft ob Zug z ein korrekter (erster) Sprung ist.
	 */
	public boolean isSprung(Zug z) {
		//return zugIstGueltig(z, true, false, false);
		int x1 = transform(z.gibStartX());
		int y1 = transform(z.gibStartY());
		int x2 = transform(z.gibEndeX());
		int y2 = transform(z.gibEndeY());
		boolean zugMitDame = (spielbrett_intern[x1][y1] == eigeneDame);
		
		//Teste alles mögliche
		if (!kgV_checkObZugOderSprung(z, x1, y1, x2, y2, zugMitDame))
			return false;
		
        //wenn keine Dame
		if (!zugMitDame) {			
	        //prüfe: erlaubter Sprung über Gegner
			if (!sprungVonStein(x1, y1, x2, y2))
				return false;
		} //end-if keine Dame
		
		else { //wenn Dame
			//x- und ySchritt von Ursprungsposition aus gesehen!
			int xSchritt = (int) Math.signum(x2-x1); //(x2 > x1) ? +1 : -1;
			int ySchritt = (int) Math.signum(y2-y1); //(y2 > y1) ? +1 : -1;
			int testX = x2-xSchritt;
			int testY = y2-ySchritt;
							
			//prüfe: Ein Feld vor Zielfeld mit gegnerischem Stein belegt
			if (!isGegner_intern(testX, testY))
				return false;
		}
		
		return true;
	}
	
	//############################################################
	
	/**
	 * Prüft ob eine Zugfolge gültig ist, vorausgesetzt sie ist schon komplett.
	 */
	public boolean zugIstGueltig(ArrayList<Zug> z) {
		return zugIstGueltig(z, true);
	}
	
	/**
	 * Prüft ob eine Zugfolge gültig ist, in Abhängigkeit davon ob sie schon komplett ist.
	 */
	public boolean zugIstGueltig(ArrayList<Zug> z, boolean zugfolgeKomplett) {
		if (z.size() == 0) return false;
		
		Zug temp = z.get(0);
		boolean zugMitDame = (spielbrett_intern[transform(temp.gibStartX())][transform(temp.gibStartY())] == eigeneDame);
		
		if (zugMitDame)// && zugfolgeKomplett) //
			TEMP_spielbrett = spielbrettKopie(spielbrett_intern); //Um die übersprungenen Steine für die Tests entfernen zu können
		
		if (z.size() == 1) {
			if (zugfolgeKomplett)
				return zugIstGueltig(z.get(0));
			else
				return zugIstGueltig(z.get(0), true, false, false);
		}

		//Pruefe ob Zusammenhaengend
		int ende_x1 = temp.gibEndeX();
		int ende_y1 = temp.gibEndeY();
		int start_x2, start_y2;
		for (int i=1; i<z.size(); i++) { //Züge durchgehen
			temp = z.get(i);
			start_x2 = temp.gibStartX();
			start_y2 = temp.gibStartY();
			if ((ende_x1 != start_x2) || (ende_y1 != start_y2)) { //Prüfen ob Ende des vorherigen = Start des aktuellen
				return false;
			}
			ende_x1 = temp.gibEndeX();
			ende_y1 = temp.gibEndeY();
		}
			
		//Prüfe ob alle einzelnen Züge gültig
		if (!zugIstGueltig(z.get(0), true, false, zugMitDame)) //Beim ersten Zug auch prüfen ob Ausgangspunkt eigener Stein
			return false;
		for (int i=1; i<z.size()-1; i++) {
			if (!zugIstGueltig(z.get(i), false, false, zugMitDame)) //Bei den mittleren Zügen Korrektheit ohne Sonderfälle prüfen
				return false;
		}
		if (!zugIstGueltig(z.get(z.size()-1), false, zugfolgeKomplett, zugMitDame)) //Beim letzten Sprung prüfen, ob weiterer Sprung möglich wäre
			return false;
		
		//Dann ist die Zugfolge wohl gueltig.
		return true;
	}
	
	/**
	 * Prüft ob der Zug gültig ist.
	 */
	private boolean zugIstGueltig(Zug z) {
		return zugIstGueltig(z, true, true, false);
	}
	
	/**
	 * Prüft ob der Zug gültig ist.
	 */
	private boolean zugIstGueltig(Zug z, boolean ersterZug, boolean letzterZug, boolean zugMitDame) {
		boolean zugFolge = !(ersterZug && letzterZug); //Wenn zugleich erster und letzter Zug, dann keine Zugfolge
		int x1 = transform(z.gibStartX());
		int y1 = transform(z.gibStartY());
		int x2 = transform(z.gibEndeX());
		int y2 = transform(z.gibEndeY());
		
		//prüfe: Hat Zug gültige Koordinaten?
		if (!z.hatGueltigeKoordinaten())
			return false;
		
        //prüfe: Ziel ist freies Feld
		if (!isLeer_intern(x2, y2))
			return false;
		
		//prüfe: ist eigener Stein falls erster Zug
		if (ersterZug) { //alt.: if(!zugFolge) {
			zugMitDame = (spielbrett_intern[x1][y1] == eigeneDame); //Beim ersten Zug wird bei einem einzelnen Zug nicht übergeben, ob mit einer Dame gezogen wurde.
			if (!isEigener_intern(x1, y1))
				return false;
		}
		
		if (!zugMitDame) { //wenn keine Dame
			//prüfe: richtige Richtung (nach oben)
			if (y2 <= y1)
				return false;

	        //prüfe: Felder liegen nebeneinander
			boolean angrenzendeFelder = zugVonStein(x1, y1, x2, y2);
			
	        //prüfe: oder erlaubter Sprung über Gegner
			boolean erlaubterSprung = sprungVonStein(x1, y1, x2, y2);
			
			//prüfe: Felder liegen nicht nebeneinander und gesprungen wurde auch nicht
			if (!angrenzendeFelder && !erlaubterSprung)
				return false;
			
			//prüfe: Zugfolge darf nur aus Sprüngen bestehen
			if (zugFolge && !erlaubterSprung) //Sollte gleichbedeutend sein mit if (zugFolge && angrenzendeFelder)
				return false;
			
			//prüfe: Es wurde gesprungen, aber nicht alle möglichen Sprünge wurden ausgeführt.
			if (letzterZug && erlaubterSprung && weitererSprungIstMoeglich(x2, y2, false))
				return false;
			
			//prüfe: kein Sprung aber Sprung ist möglich (Zugfolge ist schon ausgeschlossen)
			if (angrenzendeFelder && sprungIstMoeglich())
				return false;
			
			return true;
		} //end-if keine Dame
		
		else { //wenn Dame
			//prüfe: Zielfeld auf einer der Diagonalen vom Startfeld aus
			if (!vonDameAusfuehrbar(x1, y1, x2, y2))
				return false;
			
			//prüfe: Alle Felder zwischen Startfeld und einem Feld vor Zielfeld leer
			if (!zwischenraumFrei(x1, y1, x2, y2))
				return false;
			//x- und ySchritt von Ursprungsposition aus gesehen!
			int xSchritt = (int) Math.signum(x2-x1); //(x2 > x1) ? +1 : -1;
			int ySchritt = (int) Math.signum(y2-y1); //(y2 > y1) ? +1 : -1;
			int testX = x2-xSchritt;
			int testY = y2-ySchritt;
			
			boolean normalerZug = isLeer_intern(testX, testY);
				
			//prüfe: oder erlaubter Sprung über Gegner => Nur ein Feld vor Zielfeld mit gegnerischem Stein belegt, sonst alle leer
			boolean erlaubterSprung = isGegner_intern(testX, testY);

			//prüfe: Sprung, aber inkorrekt
			if (!normalerZug && !erlaubterSprung)
				return false;
			
			//prüfe: Zugfolge darf nur aus Sprüngen bestehen
			if (zugFolge && !erlaubterSprung)
				return false;
			
			//prüfe: kein Sprung aber Sprung ist möglich (Zugfolge ist schon ausgeschlossen)
			if (normalerZug && sprungIstMoeglich())
				return false;
			
			 //auf dem temporären Brett den übersprungenen Stein entfernen
			if (erlaubterSprung) {
				//x- und yKorrektur vom Zielfeld aus gesehen um übersprungen Stein zu erreichen
				int xKorrektur = (int) Math.signum(x1-x2); //(x2 > x1) ? -1 : +1;
				int yKorrektur = (int) Math.signum(y1-y2); //(y2 > y1) ? -1 : +1;
				
				if (xKorrektur != -1*xSchritt || yKorrektur != -1*ySchritt)
					System.out.println("PROGRAMMIERFEHLER!");
					//falls das nie auftritt können die zwei int-Werte hier drüber weg.
				
				//TEMP_spielbrett[x2+xKorrektur][y2+yKorrektur] = LEER;
				//TEMP_spielbrett[x2-xSchritt][y2-ySchritt] = LEER;
				TEMP_spielbrett[testX][testY] = LEER;
			}
			
			//prüfe: Es wurde gesprungen, aber nicht alle möglichen Sprünge wurden ausgeführt. (Wird anhand TEMP_spielbrett geprüft).
			if (letzterZug && erlaubterSprung) {
				if (weitererSprungIstMoeglich(x2, y2, true))
					return false;
			}
			
			return true;
		}
	}
	
	//############################################################
	
	
	/**
	 * Führt Zug aus.
	 */
	public void macheZug(Zug z) {
		if (!zugIstGueltig(z)) {
			throw new IllegalArgumentException("Dieser Zug ist nicht gültig!");
		}
		int x1 = transform(z.gibStartX());
		int y1 = transform(z.gibStartY());
		int x2 = transform(z.gibEndeX());
		int y2 = transform(z.gibEndeY());
		
		if (y2 == 7) //Oberste Zeile -> Umwandelung zur Dame
			sb_set(x2, y2, eigeneDame);
		else
			sb_set(x2, y2, spielbrett_intern[x1][y1]);
		
		sb_set(x1, y1, LEER);
		//x- und yKorrektur vom Zielfeld aus gesehen um übersprungen Stein zu erreichen
		int xKorrektur = (int) Math.signum(x1-x2); //(x2 > x1) ? -1 : +1;
		int yKorrektur = (int) Math.signum(y1-y2); //(y2 > y1) ? -1 : +1;
		sb_set(x2+xKorrektur, y2+yKorrektur, LEER);
	}
	
	/**
	 * Führt Zugfolge aus.
	 */
	public void macheZug(ArrayList<Zug> z) {
		if (!zugIstGueltig(z, true)) {
			throw new IllegalArgumentException("Dieser Zug ist nicht gültig!");
		}
		int x1, y1, x2, y2, startStein = 0;
		for (int i=0; i<z.size(); i++) {
			x1 = transform(z.get(i).gibStartX());
			y1 = transform(z.get(i).gibStartY());
			x2 = transform(z.get(i).gibEndeX());
			y2 = transform(z.get(i).gibEndeY());
			if (i==0) //Nur im ersten Durchlauf wird festgehalten, mit welchem Stein gesprungen wird.
				startStein = spielbrett_intern[x1][y1];
			
			if (y2 == 7) //Oberste Zeile -> Umwandelung zur Dame
				sb_set(x2, y2, eigeneDame);
			else
				sb_set(x2, y2, startStein);
			
			sb_set(x1, y1, LEER);
			//x- und yKorrektur vom Zielfeld aus gesehen um übersprungen Stein zu erreichen
			int xKorrektur = (int) Math.signum(x1-x2); //(x2 > x1) ? -1 : +1;
			int yKorrektur = (int) Math.signum(y1-y2); //(y2 > y1) ? -1 : +1;
			sb_set(x2+xKorrektur, y2+yKorrektur, LEER);	
		}
	}
	
	//############################################################
	
	
	/**
	 * Prüft ob die Koordinaten gültig sind
	 */
	public static boolean koordinatenGueltig(int x, int y) {
        //Feldbegrenzungen
        if (y<0 || y>7)
            return false;

        //Feldbegrenzungen
        if (x<0 || x>7)
            return false;

        //weiße Felder sind ungültig
        if (x%2 != y%2)
            return false;

        return true;
    }
	
	//#####
	
	/**
	 * Prüft ob das Spiel beendet ist.
	 */
	public int isSpielBeendet() {
		int schwarz = 0;
		int weiss = 0;
		int akt;
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				akt = spielbrett[x][y];
				if (akt == SCHWARZ || akt == SCHWARZ_D)
					schwarz++;
				if (akt == WEISS || akt == WEISS_D)
					weiss++;
			}
		}
		if (schwarz == 0)
			return WEISS;
		if (weiss == 0)
			return SCHWARZ;
		else
			return -1;
	}
	
	/**
	 * Gibt id der Steine des aktuellen Spielers zurück.
	 */
	public int getEigenerStein() {
		return eigenerStein;
	}
	
	
	/**
	 * Gibt id der Damen des aktuellen Spielers zurück.
	 */
	public int getEigeneDame() {
		return eigeneDame;
	}
	
	
	/**
	 * Gibt id der Steine des aktuellen Gegenspielers zurück.
	 */
	public int getGegnerStein() {
		return gegnerStein;
	}
	
	
	/**
	 * Gibt id der Damen des aktuellen Gegenspielers zurück.
	 */
	public int getGegnerDame() {
		return gegnerDame;
	}
	
	/**
	 * Prüft ob Feld leer ist
	 */
	public boolean isLeer(int x, int y) {
		return (spielbrett[x][y] == LEER);
	}
	
	/**
	 * Prüft ob Feld mit Stein des aktuellen Spielers besetzt ist.
	 */
	public boolean isEigener(int x, int y) {
		return (spielbrett[x][y] == eigenerStein || spielbrett[x][y] == eigeneDame);
	}
	
	/**
	 * Prüft ob Feld mit Stein des aktuellen Gegenspielers besetzt ist.
	 */
	public boolean isGegner(int x, int y) {
		return (spielbrett[x][y] == gegnerStein || spielbrett[x][y] == gegnerDame);
	}
	
	//#####
	
	/**
	 * Prüft ob Feld leer ist
	 */
	private boolean isLeer_intern(int x, int y) {
		return (spielbrett_intern[x][y] == LEER);
	}
	
	/**
	 * Prüft ob Feld mit Stein des aktuellen Spielers besetzt ist.
	 */
	private boolean isEigener_intern(int x, int y) {
		return (spielbrett_intern[x][y] == eigenerStein || spielbrett_intern[x][y] == eigeneDame);
	}
	
	/**
	 * Prüft ob Feld mit Stein des aktuellen Gegenspielers besetzt ist.
	 */
	private boolean isGegner_intern(int x, int y) {
		return (spielbrett_intern[x][y] == gegnerStein || spielbrett_intern[x][y] == gegnerDame);
	}
	
	/**
	 * Prüft ob der durch die Koordinaten angegebene Zug (kein Sprung!) durch nicht-Dame ausführbar ist.
	 */
	private boolean zugVonStein(int x1, int y1, int x2, int y2) {
		return ((y2 == y1+1) && (x2 == x1+1 || x2 == x1-1)) ? true : false;
	}
	
	/**
	 * Prüft ob der durch die Koordinaten angegebene Sprung durch nicht-Dame korrekt ist.
	 */
	private boolean sprungVonStein(int x1, int y1, int x2, int y2) {
		if ((y2 == y1+2) && (x2 == x1+2 || x2 == x1-2)) {
			int uebersprungen = spielbrett_intern[(x1+x2)/2][y1+1];
			if (uebersprungen == gegnerStein || uebersprungen == gegnerDame)
				return true;
		}
		return false;
	}
	
	/**
	 * Prüft ob der durch die Koordinaten angegebene Zug (auch Sprung) durch Dame ausführbar ist.
	 */
	private boolean vonDameAusfuehrbar(int x1, int y1, int x2, int y2) {
		return (Math.abs(x2-x1) == Math.abs(y2-y1));
	}
	
	/**
	 * Prüft ob alle Felder zwischen Startfeld und einem Feld vor Zielfeld leer
	 */
	private boolean zwischenraumFrei(int x1, int y1, int x2, int y2) {
		//x- und ySchritt von Ursprungsposition aus gesehen!
		int xSchritt = (int) Math.signum(x2-x1); //(x2 > x1) ? +1 : -1;
		int ySchritt = (int) Math.signum(y2-y1); //(y2 > y1) ? +1 : -1;
		int testX=x1+xSchritt, testY=y1+ySchritt;
		while (xSchritt*testX < xSchritt*(x2-xSchritt) && ySchritt*testY < ySchritt*(y2-ySchritt)) {
			if (spielbrett_intern[testX][testY] != LEER)
				return false;
			testX += xSchritt;
			testY += ySchritt;
		}
		if (testX != x2-xSchritt || testY != y2-ySchritt)
			System.out.println("ProgrammierFEHLER!!!");
		return true;
	}
	
	private boolean kgV_checkObZugOderSprung(Zug z, int x1, int y1, int x2, int y2, boolean zugMitDame) {
		//prüfe: Hat Zug gültige Koordinaten?
		if (!z.hatGueltigeKoordinaten())
			return false;
		
        //prüfe: Ziel ist freies Feld
		if (!isLeer_intern(x2, y2))
			return false;
		
		//prüfe: ist eigener Stein
		if (!isEigener_intern(x1, y1))
			return false;
		
		//wenn keine Dame
		if (!zugMitDame) {
			//prüfe: richtige Richtung (nach oben)
			if (y2 <= y1)
				return false;
		}
		else { //wenn Dame
			//prüfe: Zielfeld auf einer der Diagonalen vom Startfeld aus
			if (!vonDameAusfuehrbar(x1, y1, x2, y2))
				return false;
			
			//prüfe: Alle Felder zwischen Startfeld und einem Feld vor Zielfeld leer
			if (!zwischenraumFrei(x1, y1, x2, y2))
				return false;
		}
		return true;
	}
	
	//############################################################
	
	
	/**
	 * Konsolenausgabe des aktuellen Spielbretts
	 */
	private void gibAus() { //boolean zurueckgedreht) {
		//ACHTUNG: spielbrett.clone() funktioniert NICHT, 
		//da nur Referenzen zu den Arrays in der zweiten Dimension kopiert werden. 
		//clone funktioniert nur bei eindimensionalen Arrays mit Standard-Datentypen.
		/*int[][] tempsb = spielbrettKopie();
		
		if (zurueckgedreht) {
			if (!schwarzAmZug) {
				int temp;
				for (int y=0; y<=3; y++) {
					for (int x=0; x<8; x++) {
						temp = tempsb[x][y];
						tempsb[x][y] = tempsb[7-x][7-y];
						tempsb[7-x][7-y] = temp;
					}
				}
			}*/
			System.out.println("     A   B   C   D   E   F   G   H  ");
			System.out.println("   - - - - - - - - - - - - - - - - -");
			String linie;
			for (int y=8; y>=1; y--) {
				linie = y + "  | ";
				for (int x=1; x<=8; x++) {
					String symbol = "";
					switch (spielbrett[x-1][y-1]) {
						case -1 : symbol = " "; break;
						case 0 : symbol = " "; break;
						case 1 : symbol = "X"; break;
						case 2 : symbol = "O"; break;
						case 3 : symbol = "T"; break;
						case 4 : symbol = "D"; break;
					}
						
					linie += symbol + " | ";
				}
				linie += " " + y;
				System.out.println(linie);
				System.out.println("   - - - - - - - - - - - - - - - - -");
			}
			System.out.println("     A   B   C   D   E   F   G   H  ");
			System.out.println();
		//}
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
