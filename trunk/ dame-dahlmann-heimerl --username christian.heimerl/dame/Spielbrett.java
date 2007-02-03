package dame;

public class Spielbrett {
	
	private final int LEER = 0;
	private final int SCHWARZ = 1;
	private final int WEISS = 2;
	private final int DAMES = 3;
	private final int DAMEW = 4;
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
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if ((x+y)%2 == 0)
					if (x<3)
						spielbrett[x][y] = SCHWARZ;
					else if (x>4)
						spielbrett[x][y] = WEISS;
				else
					spielbrett[x][y] = LEER;
			}
		}
		schwarzAmZug = true;
		gibAus();
	}
	
	/**
	 * Ändert die Ausrichtung des Brettes auf die Sichtweise von Spieler Schwarz
	 */
	public void schwarzAmZug() {
		if (!schwarzAmZug) {
			int temp;
			for (int x=0; x<3; x++) {
				for (int y=0; y<8; y++) {
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
			for (int x=0; x<3; x++) {
				for (int y=0; y<8; y++) {
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
		int x1 = z.gibStartX();
		int y1 = z.gibStartY();
		int x2 = z.gibEndeX();
		int y2 = z.gibEndeY();
		
	}
	
	public void macheZug() {
		
	}
	
	public int[][] spielbrettKopie() {
		return spielbrett.clone();
	}
	
	/**
	 * Konsolenausgabe des aktuellen Spielbretts
	 */
	public void gibAus() {
		System.out.println("  - - - - - - - - - - - - - - -");
		String linie;
		for (int i=7; i>=0; i--) {
			linie = "| ";
			for (int j=0; j<8; j++) {
				String symbol = "";
				switch (spielbrett[i][j]) {
					case 0 : symbol = " "; break;
					case 1 : symbol = "X"; break;
					case 2 : symbol = "O"; break;
					case 3 : symbol = "T"; break;
					case 4 : symbol = "D"; break;
				}
					
				linie += symbol + " | ";
			}
			System.out.println(linie);
			System.out.println("  - - - - - - - - - - - - - - -");
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
			for (int x=0; x<8; x++) {
				for (int y=0; y<8; y++) {
					temp[x][y] = spielbrett[7-x][7-y];
				}
			}
			spielbrett = temp;
		}
		long ende1 = System.currentTimeMillis();
		
		long start2 = System.currentTimeMillis();
		for (int i=0; i<1000000; i++) {
			int temp;
			for (int x=0; x<3; x++) {
				for (int y=0; y<8; y++) {
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
	
}
