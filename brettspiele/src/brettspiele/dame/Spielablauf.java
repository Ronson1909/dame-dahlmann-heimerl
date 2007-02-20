package brettspiele.dame;

import brettspiele.*;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;

/**
 * <p>Überschrift: </p>
 *
 * <p>Beschreibung: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Organisation: </p>
 *
 * @author unbekannt
 * @version 1.0
 */
public class Spielablauf implements ISpielablauf<ZugFolge> {
	private ISpieler spieler[] = new ISpieler[2];
    private Spielbrett sb;
    private Stack<ZugFolge> bisherigeZuege = new Stack<ZugFolge>();
    private Stack<ZugFolge> undoneZuege = new Stack<ZugFolge>();

	public Spielablauf() {
        sb=new Spielbrett();
		//this(null, null);
	}

	final public void zugBeendet(ZugBeendetEvent<ZugFolge> zbe) {
    	macheZugInt(zbe.getZug());
    	
    	undoneZuege.clear();

       	switch (sb.isSpielBeendet()) {
       	case Spielbrett.WEISS:
       	case Spielbrett.SCHWARZ:
       		return;
       	default:
       	}
    	
       	getSpielerAmZug().startGettingNaechstenZug(sb.clone());
	}


    public IBrettspielComponent createBrettspielComponent() {
    	return new SpielbrettComponent();
    }

    /**
     * Gibt das Spielbrett zurück.
     * @return Das aktuelle Spielbrett.
     */
    public Spielbrett getSpielsituation() {
    	return sb;
    }
    
    public ISpieler[] getSpieler() {
    	return spieler;
    }
    
    /**
     * Gibt das aktuelle, interne Spielbrett zurück, das immer wieder gedreht wird.
     * @return Das aktuelle Spielbrett.
     */
    //public Spielbrett getSpielbrettInt() {
    //	return sb;
    //}

    /**
     * Gibt die aktuelle Farbe zurück.
     * @return Die aktuelle Farbe.
     */
    public int getFarbeAmZug() {
    	return sb.getFarbeAmZug();
    }

    /**
     * Gibt den aktuellen Spieler zurück.
     * @return Der aktuelle Spieler.
     */
    public ISpieler getSpielerAmZug() {
    	return spieler[sb.getFarbeAmZug()-1];
    }

    /**
     * Gibt den schwarzen Spieler zurück.
     * @return Der schwarze Spieler.
     */
    public ISpieler getSpielerSchwarz() {
    	return spieler[0];
    }

    /**
     * Gibt den weißen Spieler zurück.
     * @return Der weiße Spieler.
     */
    public ISpieler getSpielerWeiss() {
    	return spieler[1];
    }

   /**
    * Startet ein Spiel.
    *
    */
    public void starten(ISpieler schwarz, ISpieler weiss) {
//        while (kis[getFarbeAmZug()-1] != null) {
//           	Spielbrett tmpSB = sb.clone();
//           	ArrayList<Zug> zugfolge = kis[getFarbeAmZug()-1].gibNaechstenZug(tmpSB, getFarbeAmZug());
//           	macheZugInt(zugfolge);
//
//           	switch (sb.isSpielBeendet()) {
//           	case Spielbrett.WEISS:
//           	case Spielbrett.SCHWARZ:
//           		return;
//           	default:
//           	}
//        };
    	if (schwarz == null || weiss == null)
    		throw new IllegalArgumentException("Der weiße und schwarze Spieler dürfen nicht null sein!");
    	
		spieler[0]=schwarz;
		spieler[1]=weiss;

		spieler[0].startGettingNaechstenZug(sb.clone());
    }

    /**
     * Führt die Zugfolge durch und archiviert sie außerdem zum Rückgängigmachen. 
     * @param z Die durchzuführende Zugfolge.
     */
	private void macheZugInt(ZugFolge z) {
		try {
			sb.macheZug(z);

			bisherigeZuege.add(z);
		}
		catch (Exception ex) {
			System.out.println("Konnte Zug im Spielablauf nicht ausführen: " + ex.toString());
		}
	}

	/**
	 * Macht die Zugfolge und startet danach die KI (wenn nötig).
     * Archiviert die Zugfolge außerdem zum Rückgängigmachen.
	 * @param z Die durchzuführende Zugfolge.
	 */
//    public void macheZug(ArrayList<Zug> z) {
//	}

    /**
     * Gibt die bisherige Zugfolge auf der Konsole aus.
     *
     */
    public void gibAus() {
        int aktuelleFarbe = 1;
        
    	for (ArrayList<Zug> zugKombi : bisherigeZuege) {
    		if (aktuelleFarbe==1) {
    			System.out.print("Schwarz zog: ");
    		}
    		else {
    			System.out.print("Weiß zog: ");
    		}
    		
    		int ZugZaehler = 1;
    		for (Zug zug : zugKombi) {
    			if (ZugZaehler==1) {
            		System.out.print((char)(zug.gibStartX()+65));
            		System.out.print((zug.gibStartY()+1));
    			}

    			System.out.print("-");
        		System.out.print((char)(zug.gibEndeX()+65));
        		System.out.print((zug.gibEndeY()+1));

    			ZugZaehler++;
    		}
    		System.out.println("");

            if (aktuelleFarbe==1)
                aktuelleFarbe=0;
            else
                aktuelleFarbe=1;
}
    }
    
    /**
     * Macht einen Zug rückgängig. Wenn
     * kein Zug rückgängig gemacht werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void undoZug() {
    	undoZug(1);
    }

    /**
     * Macht mehrere Züge rückgängig. Wenn
     * kein Zug rückgängig gemacht werden kann, wird keine
     * Exception geworfen.
     * @param anzahlZuege Die Anzahl der rückgängig zu machenden Züge. 
     */
    public void undoZug(int anzahlZuege) {
    	if (bisherigeZuege.size()==0)
    		return;
    	
    	for (int i=1;i<=anzahlZuege && bisherigeZuege.size()>0;i++) {
    		ZugFolge zf = bisherigeZuege.pop();
    		sb.undoZug(zf);
    		undoneZuege.add(zf);
    	}
    	
		getSpielerAmZug().startGettingNaechstenZug(sb.clone());
    }

    /**
     * Stellt einen rückgängig gemachten Zug wieder her. Wenn
     * kein Zug wiederhergestellt werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void redoZug() {
    }

    /**
     * Stellt mehrere rückgängig gemachte Züge wieder her. Wenn
     * kein Zug wiederhergestellt werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void redoZug(int anzahlZuege) {
    	if (undoneZuege.size()==0)
    		return;

    	for (int i=1;i<=anzahlZuege && undoneZuege.size()>0;i++) {
    		ZugFolge zf = undoneZuege.pop();
    		
    		macheZugInt(zf);

           	switch (sb.isSpielBeendet()) {
           	case Spielbrett.WEISS:
           	case Spielbrett.SCHWARZ:
           		return;
           	default:
           	}
    	}

		getSpielerAmZug().startGettingNaechstenZug(sb.clone());
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
