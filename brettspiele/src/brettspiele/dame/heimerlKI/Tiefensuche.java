package brettspiele.dame.heimerlKI;

import java.awt.Point;
import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import brettspiele.dame.Spielbrett;
import brettspiele.dame.Zug;

public class Tiefensuche implements ISuchmethode {
	private int searchDepth;
	private IBewerter bew;
	private BewerteteZugFolge besterZug;
	
	/**
	 * 
	 * @param searchDepth Die Suchtiefe in Halbz�gen. Sollte gerade und >0 sein.
	 */
	public Tiefensuche(int searchDepth) {
		this(searchDepth, new StandardBewerter());
	}

	/**
	 * 
	 * @param searchDepth Die Suchtiefe in Halbz�gen. Sollte gerade und >0 sein.
	 * @param bew Das Bewertungsverfahren f�r Z�ge und Stellungen.
	 */
	public Tiefensuche(int searchDepth, IBewerter bew) {
		if (searchDepth<=0)
			throw new IllegalArgumentException();
		if (bew==null)
			throw new IllegalArgumentException();
			
		this.searchDepth=searchDepth;
		this.bew=bew;
	}

	public BewerteteZugFolge calcBestenZug(Spielbrett sb, int searchLevel) {
		try {
			int wert = alphaBeta(sb, searchLevel, Integer.MIN_VALUE+1, Integer.MAX_VALUE-1);
			System.out.println("Beste Stellung mit Wert: " + wert);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return besterZug;
	}
	
	private int alphaBeta( Spielbrett sb, int depth , int alpha , int beta )  throws InstantiationException, IllegalAccessException {
		class SpielbrettUndZug implements Comparable<SpielbrettUndZug> {
			public Spielbrett sb;
			public BewerteteZugFolge zf;
			
			public int compareTo(SpielbrettUndZug o) {
				// TODO Auto-generated method stub
				return zf.compareTo(o.zf);
			}
		}
		
		ArrayList<SpielbrettUndZug> verfZuege = new ArrayList<SpielbrettUndZug>();
		
		{
			ArrayList<Point> steine = sb.getEigeneSteine();
			ArrayList<BewerteteZugFolge> zuege = new ArrayList<BewerteteZugFolge>();
			for (Point stein : steine) {
				zuege.addAll(sb.getErlaubteZugFolgen(stein, BewerteteZugFolge.class));
			}
	
			if (depth==0 && zuege.size()==1) {
				this.besterZug = zuege.get(0);
				return 0;
			}

			if (depth >=searchDepth) {//endPosition (sb) ||
				if (zuege.size()>0) {
					Zug z = zuege.get(0).get(0);
					if (Math.abs(z.gibStartX()-z.gibEndeX())>=2)
						depth-=1;
				}
			}

			if (depth >=searchDepth) //endPosition (sb) ||
				return bew.bewerteSpielbrett(sb);
			
			for (BewerteteZugFolge z : zuege) {
				SpielbrettUndZug suz = new SpielbrettUndZug();
				suz.sb = sb.clone();
				suz.zf = z;
				
				suz.sb.macheZug(z);
				z.setBewertung(bew.bewerteZugfolge(z, sb));
				
				verfZuege.add(suz);
			}
		}

		java.util.Collections.sort(verfZuege);

		
		for (SpielbrettUndZug suz : verfZuege) {
			//sb.macheZugUnchecked(z);
			int value = -alphaBeta (suz.sb, depth + 1, -beta , -alpha );
			//sb.undoZugUnchecked(z);
			
			if( value >= beta )
				return beta ;
			
			if( value > alpha ) {
				if (depth==0) {
					this.besterZug = suz.zf;
				}
				
				alpha = value ;
			}
		}
		
		return alpha ;
	}

	public IBewerter getBewerter() {
		return bew;
	}

	public void setBewerter(IBewerter bew) {
		this.bew = bew;
	}

	public int getSearchDepth() {
		return searchDepth;
	}

	public void setSearchDepth(int searchDepth) {
		this.searchDepth = searchDepth;
	}

	public void configure(Window owner) {
		TiefensucheEinstellungenFenster ef = new TiefensucheEinstellungenFenster(owner, this);
		ef.setVisible(true);
		
		if (ef.getResult() == JOptionPane.OK_OPTION) {
			this.setBewerter(ef.getBewerter());
			this.setSearchDepth(ef.getSuchtiefe());
		}
	}
}
