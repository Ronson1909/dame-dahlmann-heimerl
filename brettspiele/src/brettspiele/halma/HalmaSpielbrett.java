package brettspiele.halma;

import brettspiele.ISpielsituation;

public class HalmaSpielbrett implements ISpielsituation {
	public static final int LEER = 0;
	public static final int ROT = 1;
	public static final int BLAU = 2;
	public static final int GRUEN = 3;

	private int[][] spielbrett;
	private int[][] TEMP_spielbrett;

	public int isSpielBeendet() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getFeld(int x, int y) {
		return spielbrett[x][y];
	}
}
