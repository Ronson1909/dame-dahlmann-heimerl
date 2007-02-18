package brettspiele.halma;

import java.util.Stack;

import brettspiele.*;
import brettspiele.dame.ZugFolge;

public class HalmaSpielablauf implements ISpielablauf<Zug> {
	private ISpieler spieler[] = new ISpieler[2];
    private HalmaSpielbrett sb;
    private Stack<Zug> bisherigeZuege = new Stack<Zug>();
    private Stack<Zug> undoneZuege = new Stack<Zug>();

	public HalmaSpielablauf() {
        sb=new HalmaSpielbrett(2);
		//this(null, null);
	}

	public void zugBeendet(ZugBeendetEvent<Zug> zbe) {
    	macheZugInt(zbe.getZug());
    	
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

    /**
     * F�hrt den Zug durch und archiviert sie au�erdem zum R�ckg�ngigmachen. 
     * @param z Der durchzuf�hrende Zug.
     */
	private void macheZugInt(Zug z) {
		try {
			sb.macheZug(z);

			bisherigeZuege.add(z);
		}
		catch (Exception ex) {
			System.out.println("Konnte Zug im Spielablauf nicht ausf�hren: " + ex.toString());
		}
	}

    public IBrettspielComponent createBrettspielComponent() {
    	return new HalmaSpielbrettComponent();
    }

    /**
     * Gibt das Spielbrett zur�ck.
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
		return sb.getFarbeAmZug();
	}

	public ISpieler getSpielerAmZug() {
		// TODO Auto-generated method stub
		return spieler[getFarbeAmZug()-1];
	}

	public void redoZug(int anzahlZuege) {
		// TODO Auto-generated method stub
		
	}

	public void undoZug(int anzahlZuege) {
		// TODO Auto-generated method stub
		
	}
	
    /**
     * Gibt die Anzahl der Z�ge im Undo-Puffer zur�ck.
     * @return Die Anzahl der Z�ge im Undo-Puffer.
     */
    public int getUndoCount() {
    	return bisherigeZuege.size();
    }

    /**
     * Gibt die Anzahl der Z�ge im Redo-Puffer zur�ck.
     * @return Die Anzahl der Z�ge im Redo-Puffer.
     */
    public int getRedoCount() {
    	return undoneZuege.size();
    }
}
