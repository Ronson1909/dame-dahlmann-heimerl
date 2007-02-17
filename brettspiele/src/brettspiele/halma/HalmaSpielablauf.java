package brettspiele.halma;

import java.util.Stack;

import brettspiele.*;

public class HalmaSpielablauf implements ISpielablauf {
	private ISpieler spieler[] = new ISpieler[2];
    private HalmaSpielbrett sb;
    private Stack<IZug> bisherigeZuege = new Stack<IZug>();
    private Stack<IZug> undoneZuege = new Stack<IZug>();

	public HalmaSpielablauf() {
        sb=new HalmaSpielbrett();
		//this(null, null);
	}

	public void zugBeendet(ZugBeendetEvent zbe) {
    	//macheZugInt(zbe.getZug());
    	
    	undoneZuege.clear();

       	switch (sb.isSpielBeendet()) {
       	case HalmaSpielbrett.BLAU:
       	case HalmaSpielbrett.ROT:
       	case HalmaSpielbrett.GRUEN:
       		return;
       	default:
       	}
    	
       	getSpielerAmZug().startGettingNaechstenZug(sb);
	}


    public IBrettspielComponent createBrettspielComponent() {
    	return new HalmaSpielbrettComponent();
    }

    /**
     * Gibt das Spielbrett zurück.
     * @return Das aktuelle Spielbrett.
     */
    public HalmaSpielbrett getSpielsituation() {
    	return sb;
    }
    
    public ISpieler[] getSpieler() {
    	return spieler;
    }

    public void setSpieler(int i, ISpieler sp) {
    	spieler[i] = sp;
    }

	public int getFarbeAmZug() {
		// TODO Auto-generated method stub
		return HalmaSpielbrett.BLAU;
	}

	public ISpieler getSpielerAmZug() {
		// TODO Auto-generated method stub
		return spieler[0];
	}

	public void redoZug(int anzahlZuege) {
		// TODO Auto-generated method stub
		
	}

	public void undoZug(int anzahlZuege) {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Gibt die Anzahl der Züge im Undo-Puffer zurück.
     * @return Die Anzahl der Züge im Undo-Puffer.
     */
    public int getUndoCount() {
    	return bisherigeZuege.size();
    }

    /**
     * Gibt die Anzahl der Züge im Redo-Puffer zurück.
     * @return Die Anzahl der Züge im Redo-Puffer.
     */
    public int getRedoCount() {
    	return undoneZuege.size();
    }
}
