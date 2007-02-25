package brettspiele.halma;

import java.util.ArrayList;
import java.util.Stack;

import brettspiele.ISpielsituation;

public class HalmaSpielbrett implements ISpielsituation, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5541904529708814963L;

	public HalmaSpielbrett clone() {
		HalmaSpielbrett sb = new HalmaSpielbrett();

		for (int y=0;y<=16;y++) {
			for (int x=y%2;x<=24;x+=2) {
				sb.spielbrett[x][y]=this.spielbrett[x][y];
			}
	    }

		sb.eigeneFarbe=this.eigeneFarbe;
		sb.spielerzahl=this.spielerzahl;
		
		return sb;
	}

	public HalmaSpielbrett getTransformed(int fromSpieler, int spielerzahl) {
		if (fromSpieler==ROT)
			return this;
		else if (fromSpieler==BLAU) {
			if (spielerzahl==2) {
				HalmaSpielbrett sb = new HalmaSpielbrett();

				for (int y=0;y<=16;y++) {
					for (int x=y%2;x<=24;x+=2) {
						sb.spielbrett[24-x][16-y]=this.spielbrett[x][y];
					}
			    }

				sb.eigeneFarbe=this.eigeneFarbe;
				sb.spielerzahl=this.spielerzahl;
				
				return sb;
			}
			else if (spielerzahl==3) {
				return this;
			}
			else
				throw new IllegalArgumentException();
		}
		else if (fromSpieler==GRUEN) {
			if (spielerzahl!=3)
				throw new IllegalArgumentException();
			else {
				return this;
			}
		}
		else
			throw new IllegalArgumentException();
	}

	public static final int LEER = 0;
	public static final int ROT = 1;
	public static final int BLAU = 2;
	public static final int GRUEN = 3;

	private int[][] spielbrett;

	private int eigeneFarbe;
	private int spielerzahl;

	private HalmaSpielbrett() {
		spielbrett = new int[25][17];
	}

	public HalmaSpielbrett(int spielerzahl) {
		this();
		
		this.spielerzahl=spielerzahl;

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
		else {
			for (int y=8;y<=12;y++) {
				for (int x=12-y ; x <= y-4 ; x+=2) {
					spielbrett[x][16-y] = BLAU;
				}
			}

			for (int y=8;y<=12;y++) {
				for (int x=28-y ; x <= 12 + y ; x+=2) {
					spielbrett[x][16-y] = GRUEN;
				}
			}
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

	/**
	 * Gibt die Anzahl an Spielern zurück
	 * @return Die Anzahl an Spielern.
	 */
	public int getSpielerzahl() {
		return spielerzahl;
	}

	public int isSpielBeendet() {
		if (spielerzahl==2) {
			boolean blauFertig=true;
			boolean rotFertig=true;
			
			forBlau: for (int y=0;y<=4;y++) {
				for (int x=12-y ; x <= 12 + y ; x+=2) {
					if (spielbrett[x][y] != BLAU) {
						blauFertig = false;
						break forBlau;
					}
				}
			}

			forRot: for (int y=0;y<=4;y++) {
				for (int x=12-y ; x <= 12 + y ; x+=2) {
					if (spielbrett[x][16-y] != ROT) {
						rotFertig = false;
						break forRot;
					}
				}
			}
			
			if (blauFertig)
				return BLAU;
			else if (rotFertig)
				return ROT;
			else
				return -1;
		}
		else if (spielerzahl==3) {
			//TODO isSpielBeendet
			return -1;
		}
		else
			throw new IllegalArgumentException();
	}

	public int getFeld(int x, int y) {
		return spielbrett[x][y];
	}

	public void undoZug(Zug z) {
		undoZugUnchecked(z);
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

	public <Z extends Zug> ArrayList<Z> getGueltigeZuege(Class<? extends Z> cl) throws InstantiationException, IllegalAccessException {
		ArrayList<Z> zuege = new ArrayList<Z>();
		
		for (int y=0;y<=16;y++) {
			for (int x=y%2;x<=24;x+=2) {
				if (spielbrett[x][y]==eigeneFarbe) {
					getGueltigeZuege(x, y, zuege, cl);
				}
			}
	    }
		
		return zuege;
	}

	public ArrayList<Zug> getGueltigeZuege() {
		try {
			return getGueltigeZuege(Zug.class);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public <Z extends Zug> ArrayList<Z> getGueltigeZuege(int x, int y, Class<? extends Z> cl) throws InstantiationException, IllegalAccessException {
		ArrayList<Z> zuege = new ArrayList<Z>();
		getGueltigeZuege(x, y, zuege, cl);
		return zuege;
	}

	public ArrayList<Zug> getGueltigeZuege(int x, int y) {
		try {
			return getGueltigeZuege(x, y, Zug.class);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public <Z extends Zug> void getGueltigeZuege(final int srcX, final int srcY, ArrayList<Z> zuege, Class<? extends Z> cl) throws InstantiationException, IllegalAccessException {
		//Sprünge
		Stack<Z> foundZuege = new Stack<Z>();
		Z z = cl.newInstance();
		z.setCoordinates(srcX, srcY, srcX, srcY);
		foundZuege.add(z); //new Zug(srcX, srcY, srcX, srcY));

		while (foundZuege.size()>0) {
			Zug src = foundZuege.pop();
			int x=src.getEndeX(), y=src.getEndeY();
			
			for (int i=-2;i<=2;i+=4) {
				if (isGueltigeKoordinate(x+2*i, y) && spielbrett[x+i][y]!=LEER && spielbrett[x+2*i][y]==LEER && !(x+2*i==srcX && y==srcY)) {
					z = cl.newInstance();
					z.setCoordinates(srcX, srcY, x+2*i, y);
					if (!zuege.contains(z)) {
						zuege.add(z);
						foundZuege.add(z);
					}
				}
			}

			for (int i=-1;i<=1;i+=2) {
				for (int j=-1;j<=1;j+=2) { 
					if (isGueltigeKoordinate(x+2*i, y+2*j) && spielbrett[x+i][y+j]!=LEER && spielbrett[x+2*i][y+2*j]==LEER && !(x+2*i==srcX && y+2*j==srcY)) {
						z = cl.newInstance();
						z.setCoordinates(srcX, srcY, x+2*i, y+2*j);
						if (!zuege.contains(z)) {
							zuege.add(z);
							foundZuege.add(z);
						}
					}
				}
			}
		}

		//Züge
		for (int i=-2;i<=2;i+=4) {
			if (isGueltigeKoordinate(srcX+i, srcY) && spielbrett[srcX+i][srcY]==LEER) {
				z = cl.newInstance();
				z.setCoordinates(srcX, srcY, srcX+i, srcY);
				zuege.add(z);
			}
		}

		for (int i=-1;i<=1;i+=2) {
			for (int j=-1;j<=1;j+=2) { 
				if (isGueltigeKoordinate(srcX+i, srcY+j) && spielbrett[srcX+i][srcY+j]==LEER) {
					z = cl.newInstance();
					z.setCoordinates(srcX, srcY, srcX+i, srcY+j);
					zuege.add(z);
				}
			}
		}
	}

	public void getGueltigeZuege(final int srcX, final int srcY, ArrayList<Zug> zuege) {
		try {
			getGueltigeZuege(srcX, srcY, zuege, Zug.class);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isGueltigerZug(Zug z) {
		if (!z.hatGueltigeKoordinaten())
			return false;
		
		if (spielbrett[z.getStartX()][z.getStartY()] != eigeneFarbe)
			return false;
		
		ArrayList<Zug> zuege;
		zuege = getGueltigeZuege(z.getStartX(), z.getStartY());
		if (!zuege.contains(z))
			return false;
		else
			return true;
	}
	
	public static boolean isGueltigeKoordinate(int x, int y) {
		if (y<0 || y>16 || x<0 || x>24)
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

	public int[] transformKoordinate(int x, int y) {
		return transformKoordinate(x, y, eigeneFarbe, spielerzahl);
	}

	public static int[] transformKoordinate(int x, int y, int fromSpieler, int spielerzahl) {
    	if (fromSpieler==HalmaSpielbrett.ROT) {
			return new int[] {x, y};
    	}
    	else if (fromSpieler==HalmaSpielbrett.BLAU) {
    		if (spielerzahl==2) {
    			return new int[] {24-x, 16-y};
    		}
    		else if (spielerzahl==3) {
    			//Systemursprung des blauen Spielers (bei (0,12)) in den Nullpunkt (0,0) schieben
    			//und System drehen
    	        //dann Systemursprung in (12,0) schieben
    			return new int[] {(-x / 2 - 3 / 2 * (y - 12)) + 12,
    								x / 2 - (y - 12) / 2};
    		}
    		else
        		throw new IllegalArgumentException();
    	}
    	else if (fromSpieler==HalmaSpielbrett.GRUEN) {
			//Systemursprung des grünen Spielers (bei (0,12)) in den Nullpunkt (0,0) schieben
			//und System drehen
	        //dann Systemursprung in (12,0) schieben
			return new int[] {(-(x-24) / 2 + 3 / 2 * (y - 12)) + 12,
					(x-24) / 2 - (y - 12) / 2};
    	}
    	else
    		throw new IllegalArgumentException();
	}
}
