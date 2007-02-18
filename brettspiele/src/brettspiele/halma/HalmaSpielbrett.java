package brettspiele.halma;

import java.util.ArrayList;
import java.util.Stack;

import brettspiele.ISpielsituation;

public class HalmaSpielbrett implements ISpielsituation {
	public static final int LEER = 0;
	public static final int ROT = 1;
	public static final int BLAU = 2;
	public static final int GRUEN = 3;

	private int[][] spielbrett;
	private int[][] TEMP_spielbrett;

	private int eigeneFarbe;
	private int spielerzahl;

	public HalmaSpielbrett(int spielerzahl) {
		this.spielerzahl=spielerzahl;
		spielbrett = new int[25][17];

		if (spielerzahl==2) {
			for (int y=0;y<=4;y++) {
				for (int x=12-y ; x <= 12 + y ; x+=2) {
					spielbrett[x][16-y] = BLAU;
				}
			}
		}
		else if (spielerzahl!=3) {
			throw new IllegalArgumentException();
		}

		for (int y=0;y<=4;y++) {
			for (int x=12-y ; x <= 12 + y ; x+=2) {
				spielbrett[x][y] = ROT;
			}
		}
		
		eigeneFarbe=ROT;
	}

	/**
	 *  Gibt die Farbe des Spielers zurück, der gerade am Zug ist.
	 *  @return Die Farbe, die am Zug ist.
	 */
	public int getFarbeAmZug() {
		return eigeneFarbe;
	}

	public int isSpielBeendet() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getFeld(int x, int y) {
		return spielbrett[x][y];
	}

	public void undoZugUnchecked(Zug z) {
		spielbrett[z.getStartX()][z.getStartY()]=spielbrett[z.getEndeX()][z.getEndeY()];
		spielbrett[z.getEndeX()][z.getEndeY()]=LEER;

		eigeneFarbe-=1;
		if (eigeneFarbe<=0)
			eigeneFarbe=spielerzahl;
	}

	public void macheZug(Zug z) {
		if (!isGueltigerZug(z))
			throw new IllegalArgumentException();
		
		macheZugUnchecked(z);
	}

	public void macheZugUnchecked(Zug z) {
		spielbrett[z.getEndeX()][z.getEndeY()]=spielbrett[z.getStartX()][z.getStartY()];
		spielbrett[z.getStartX()][z.getStartY()]=LEER;

		eigeneFarbe+=1;
		if (eigeneFarbe>spielerzahl)
			eigeneFarbe=ROT;
	}

	public ArrayList<Zug> getGueltigeZuege() {
		ArrayList<Zug> zuege = new ArrayList<Zug>();
		
		for (int y=0;y<=16;y++) {
			for (int x=y%2;x<=24;x+=2) {
				if (spielbrett[x][y]==eigeneFarbe) {
					getGueltigeZuege(x, y, zuege);
				}
			}
	    }
		
		return zuege;
	}

	public ArrayList<Zug> getGueltigeZuege(int x, int y) {
		ArrayList<Zug> zuege = new ArrayList<Zug>();
		getGueltigeZuege(x,y,zuege);
		return zuege;
	}

	public void getGueltigeZuege(final int srcX, final int srcY, ArrayList<Zug> zuege) {
		//Sprünge
		Stack<Zug> foundZuege = new Stack<Zug>(); 
		foundZuege.add(new Zug(srcX, srcY, srcX, srcY));

		while (foundZuege.size()>0) {
			Zug src = foundZuege.pop();
			int x=src.getEndeX(), y=src.getEndeY();
			
			for (int i=-2;i<=2;i+=4) {
				if (x+i<=24 && x+i>=0 && spielbrett[x+i][y]!=LEER && spielbrett[x+2*i][y]==LEER) {
					Zug z = new Zug(src.getStartX(), src.getStartY(), x+2*i, y);
					if (!zuege.contains(z)) {
						zuege.add(z);
						foundZuege.add(z);
					}
				}
			}

			for (int i=-1;i<=1;i+=2) {
				for (int j=-1;j<=1;j+=2) { 
					if (x+2*i<=24 && x+2*i>=0 && y+2*j<=16 && y+2*j>=0 && spielbrett[x+i][y+j]!=LEER && spielbrett[x+2*i][y+2*j]==LEER) {
						Zug z = new Zug(src.getStartX(), src.getStartY(), x+2*i, y+2*j);
						if (!zuege.contains(z)) {
							zuege.add(z);
							foundZuege.add(z);
						}
					}
				}
			}
		}

		//Züge
		for (int i=-2;i<=2;i+=4)
			if (srcX+i<=24 && srcX+i>=0 && spielbrett[srcX+i][srcY]==LEER)
				zuege.add(new Zug(srcX, srcY, srcX+i, srcY));

		for (int i=-1;i<=1;i+=2)
			for (int j=-1;j<=1;j+=2) 
				if (srcX+i<=24 && srcX+i>=0 && srcY+j<=16 && srcY+j>=0 && spielbrett[srcX+i][srcY+j]==LEER)
					zuege.add(new Zug(srcX, srcY, srcX+i, srcY+j));
	}

	public boolean isGueltigerZug(Zug z) {
		if (!z.hatGueltigeKoordinaten())
			return false;
		
		ArrayList<Zug> zuege = getGueltigeZuege(z.getStartX(), z.getStartY());
		if (!zuege.contains(z))
			return false;
		else
			return true;
	}
	
	public static boolean isGueltigeKoordinate(int x, int y) {
		if (y<0 || y>16)
			return false;

		if (y%2 != x%2)
			return false;
		
		if (y<=3 && (x<12-y || x>12+y))
			return false;
		
		if (y>=13 && (x<y-4 || x>28-y))
			return false;

		if (y<13 && y>=8 && (x<12-y || x>12+y))
			return false;

		if (y>4 && y<8 && (x<y-4 || x>21-y+8))
			return false;
		
		return true;
	}
}
