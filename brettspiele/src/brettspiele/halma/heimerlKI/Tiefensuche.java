package brettspiele.halma.heimerlKI;

import java.util.ArrayList;
import java.util.Random;
import brettspiele.halma.HalmaSpielbrett;

public class Tiefensuche implements ISuchmethode {
	private int searchDepth;
	private ArrayList<BewerteterZug> besteZuege = new ArrayList<BewerteterZug>();
	
	/**
	 * 
	 * @param searchDepth Sollte ungerade und >=0 sein.
	 */
	public Tiefensuche(int searchDepth) {
		this.searchDepth=searchDepth;
	}

	public BewerteterZug getBestenZug(HalmaSpielbrett sb, int searchLevel) {
		besteZuege.clear();

		try {
			//Strategien
			HalmaSpielbrett sbTmp = sb.getTransformed(sb.getFarbeAmZug(), sb.getSpielerzahl());
			
			int maxY = 4, xBeiMaxY = 12;
			for (int y=5;y<=16;y++) {
				for (int x=y%2;x<=24;x+=2) {
					if (sbTmp.getFeld(x, y)==sb.getFarbeAmZug()) {
						maxY=y;
						xBeiMaxY=x;
						break;
					}
				}
		    }
			
			//Startstrategie
			if (maxY==4) {
				int start[]=null, ende[]=null;
				
				Random rnd = new Random();
				switch (rnd.nextInt(4)) {
				case 0:
					start = sb.transformKoordinate(8, 4);
					ende = sb.transformKoordinate(9, 5);
					break;
				case 1:
					start = sb.transformKoordinate(12, 4);
					ende = sb.transformKoordinate(11, 5);
					break;
				case 2:
					start = sb.transformKoordinate(12, 4);
					ende = sb.transformKoordinate(13, 5);
					break;
				case 3:
					start = sb.transformKoordinate(16, 4);
					ende = sb.transformKoordinate(15, 5);
					break;
				}
				
				BewerteterZug z = new BewerteterZug();
				z.setCoordinates(start[0], start[1], ende[0], ende[1]);
				assert sb.isGueltigerZug(z);
				besteZuege.add(z);
			}
			//2. Zug
			else if (maxY==5) {
				int start[]=null, ende[]=null;
				
				if (xBeiMaxY==9) {
					start = sb.transformKoordinate(10, 2);
					ende = sb.transformKoordinate(10, 6);
				}
				else if (xBeiMaxY==15) {
					start = sb.transformKoordinate(14, 2);
					ende = sb.transformKoordinate(14, 6);
				}
				else {
					Random rnd = new Random();
					switch (rnd.nextInt(2)) {
					case 0:
						start = sb.transformKoordinate(10, 2);
						ende = sb.transformKoordinate(2*xBeiMaxY-12, 6);
						break;
					case 1:
						start = sb.transformKoordinate(14, 2);
						ende = sb.transformKoordinate(2*xBeiMaxY-12, 6);
						break;
					}
				}
				
				BewerteterZug z = new BewerteterZug();
				z.setCoordinates(start[0], start[1], ende[0], ende[1]);
				assert sb.isGueltigerZug(z);
				besteZuege.add(z);
			}
			//3. Zug
			else if (maxY==6) {
				int wert = alphaBeta(sb, xBeiMaxY, maxY, 0, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1);
				System.out.println("Beste Stellung mit Wert: " + wert);
//				int start[]=null, ende[]=null;
//
//				start = sb.transformKoordinate(xBeiMaxY, maxY);
//				
//				Random rnd = new Random();
//				switch (rnd.nextInt(2)) {
//				case 0:
//					ende = sb.transformKoordinate(xBeiMaxY+1, maxY+1);
//					break;
//				case 1:
//					ende = sb.transformKoordinate(xBeiMaxY-1, maxY+1);
//					break;
//				}
//				
//				BewerteterZug z = new BewerteterZug();
//				z.setCoordinates(start[0], start[1], ende[0], ende[1]);
//				assert sb.isGueltigerZug(z);
//				besteZ�ge.add(z);
			}
			else {
				int wert = alphaBeta(sb, 0, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1);
				System.out.println("Beste Stellung mit Wert: " + wert);
			}
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Random rnd = new Random();
		return besteZuege.get(rnd.nextInt(besteZuege.size()));
	}

	private int alphaBeta(HalmaSpielbrett sb, int depth, int alpha, int beta)  throws InstantiationException, IllegalAccessException {
		if (depth >=searchDepth) //endPosition (sb) || 
			return HeimerlKI.bewerte(sb);

		ArrayList<BewerteterZug> zuege = sb.getGueltigeZuege(BewerteterZug.class);
		for (BewerteterZug z : zuege) {
			HeimerlKI.bewerte(z, sb);
		}
		java.util.Collections.sort(zuege);

		for (BewerteterZug z : zuege) {
			sb.macheZugUnchecked(z);
			int value = -alphaBeta (sb, depth + 1, -beta , -alpha );
			sb.undoZugUnchecked(z);
			if( value >= beta )
				return beta ;
			
			if( value > alpha ) {
				if (depth==0) {
					besteZuege.add(z);
				}
				
				alpha = value ;
			}
		}
		
		return alpha ;
	}

	private int alphaBeta(HalmaSpielbrett sb, int x, int y, int depth, int alpha, int beta) throws InstantiationException, IllegalAccessException {
		if (depth >=searchDepth) //endPosition (sb) || 
			return HeimerlKI.bewerte(sb);

		ArrayList<BewerteterZug> zuege = sb.getGueltigeZuege(x, y, BewerteterZug.class);
		for (BewerteterZug z : zuege) {
			HeimerlKI.bewerte(z, sb);
		}
		java.util.Collections.sort(zuege);

		for (BewerteterZug z : zuege) {
			sb.macheZugUnchecked(z);
			int value = -alphaBeta (sb, depth + 1, -beta , -alpha );
			sb.undoZugUnchecked(z);
			if( value >= beta )
				return beta ;
			
			if( value > alpha ) {
				if (depth==0) {
					besteZuege.add(z);
				}
				
				alpha = value ;
			}
		}
		
		return alpha ;
	}
}
