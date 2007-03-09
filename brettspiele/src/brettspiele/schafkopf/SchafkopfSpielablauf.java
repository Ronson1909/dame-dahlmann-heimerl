package brettspiele.schafkopf;

import java.util.Stack;

import brettspiele.IBrettspielComponent;
import brettspiele.ISpielablauf;
import brettspiele.ISpieler;
import brettspiele.ISpielsituation;
import brettspiele.IZug;
import brettspiele.ZugBeendetEvent;
import brettspiele.halma.HalmaSpielbrett;

public class SchafkopfSpielablauf implements ISpielablauf<IZug> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5087821693112659192L;

	private SchafkopfSpielsituation ss = new SchafkopfSpielsituation();
	private ISpieler<IZug> spieler[] = new ISpieler[4];
    private Stack<IZug> bisherigeZuege = new Stack<IZug>();
    private Stack<IZug> undoneZuege = new Stack<IZug>();
	
	
	public IBrettspielComponent<IZug> createBrettspielComponent() {
		return new SchafkopfSpielbrettComponent();
	}

	public int getFarbeAmZug() {
		return ss.getSpielerAmZug();
	}

	public int getRedoCount() {
		return undoneZuege.size();
	}

	public ISpieler<IZug>[] getSpieler() {
		return spieler;
	}

	public void setSpieler(int pos, ISpieler<IZug> sp) {
		spieler[pos] = sp;
	}

	public ISpieler<IZug> getSpielerAmZug() {
		return spieler[ss.getSpielerAmZug()];
	}

	public ISpielsituation getSpielsituation() {
		return ss;
	}

	public int getUndoCount() {
		return bisherigeZuege.size();
	}

	public void redoZug(int anzahlZuege) {
		// TODO Auto-generated method stub

	}

	public void undoZug(int anzahlZuege) {
		// TODO Auto-generated method stub

	}

	public void zugBeendet(ZugBeendetEvent zbe) {
    	macheZugInt(zbe.getZug());
    	
    	undoneZuege.clear();

       	switch (ss.isSpielBeendet()) {
       	default:
       	}
    	
       	getSpielerAmZug().startGettingNaechstenZug(ss.clone());
	}
	
	private void macheZugInt(IZug z) {
		try {
			if (z instanceof Ausspielvorgang) {
				ss.ausspielen((Ausspielvorgang)z);
			}
			else if (z instanceof Stichbestaetigung) {
				ss.bestaetigeStich((Stichbestaetigung)z);
			}
			else if (z instanceof Spielen) {
				ss.spielenJaNein((Spielen)z);
			}
			else if (z instanceof Spielartwahl) {
				ss.waehleSpielart((Spielartwahl)z);
			}
			else
				throw new Exception("Unbekannter Zug");

			bisherigeZuege.add(z);
		}
		catch (Exception ex) {
			System.out.println("Konnte Zug im Spielablauf nicht ausführen: " + ex.toString());
		}
	}

}
