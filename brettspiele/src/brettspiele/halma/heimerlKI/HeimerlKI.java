package brettspiele.halma.heimerlKI;

import brettspiele.*;
import brettspiele.halma.*;

public class HeimerlKI extends AbstractKI {
	/**
	 * 
	 */
	private static final long serialVersionUID = -118437803426844007L;

	public HeimerlKI(int eigeneFarbe, ZugBeendetListener<Zug> zbl) {
		super(eigeneFarbe, zbl);
	}
	
	@Override
	public void run() {
		ISuchmethode best = new Tiefensuche(2);
		//ISuchmethode best = new Breitensuche(5);
		
		BewerteterZug zf = best.getBestenZug(sb,0);
		beendeZug(zf);
	}

	static void bewerte(BewerteterZug z, HalmaSpielbrett sb) {
		int bewertung=0;
		Zug tmp = z.transform(sb.getFarbeAmZug(), sb.getSpielerzahl());

		int dY = tmp.getEndeY()-tmp.getStartY();
		bewertung = dY;//(int)(Math.signum(dY)*10*(Math.pow(Math.abs(dY), 1.8)));
		
		//Malus für zu weit außen
		int dX = tmp.getEndeX()-tmp.getStartX();
		if (dX>0 && tmp.getEndeX()>17)
			bewertung-=(tmp.getEndeX()-17);
		else if (dX<0 && tmp.getEndeX()<7)
			bewertung-=(7-tmp.getEndeX());
		
		z.setBewertung(bewertung);
	}

	static int bewerte(HalmaSpielbrett sb) {
		int bewertung=0;

		HalmaSpielbrett[] sbT = new HalmaSpielbrett[sb.getSpielerzahl()];

		int thinkingFarbe = sb.getFarbeAmZug();
		int farbe = thinkingFarbe;
		int i = 0;
		do {
			sbT[i] = sb.getTransformed(farbe, sb.getSpielerzahl());
			
			farbe++;
			i++;
			if (farbe > sb.getSpielerzahl())
				farbe = HalmaSpielbrett.ROT;
			
		} while (farbe != thinkingFarbe);
		
		for (int y=0;y<=16;y++) {
			for (int x=y%2;x<=24;x+=2) {
				if (sbT[0].getFeld(x, y)==thinkingFarbe) {
					bewertung+=y;
					bewertung-=Math.max(8-x, 0);
					bewertung-=Math.max(x-16, 0);
				}
			}
	    }

		for (i=1;i<sb.getSpielerzahl();i++) {
			for (int y=0;y<=16;y++) {
				for (int x=y%2;x<=24;x+=2) {
					int tmp = sbT[i].getFeld(x, y);
					if (tmp!=HalmaSpielbrett.LEER && tmp!=thinkingFarbe) {
						bewertung-=y;
						bewertung+=Math.max(8-x, 0);
						bewertung+=Math.max(x-16, 0);
					}
				}
		    }
		}
		
		return bewertung;
	}
}
