package dame;

import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JOptionPane;

/**
 * <p>�berschrift: </p>
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
public class Spielablauf implements java.io.Serializable, ZugBeendetListener {
	private ISpieler spieler[] = new ISpieler[2];
    private Spielbrett sb;
    private Stack<ArrayList<Zug>> bisherigeZuege = new Stack<ArrayList<Zug>>();
    private Stack<ArrayList<Zug>> undoneZuege = new Stack<ArrayList<Zug>>();

	public Spielablauf() {
        sb=new Spielbrett();
		//this(null, null);
	}

	//ist ein Listener
	public void zugBeendet(ZugBeendetEvent zbe) {
    	macheZugInt(zbe.getZugfolge());
    	
    	undoneZuege.clear();

       	switch (sb.isSpielBeendet()) {
       	case Spielbrett.WEISS:
       	case Spielbrett.SCHWARZ:
       		return;
       	default:
       	}
    	
       	getSpielerAmZug().startGettingNaechstenZug(sb);
	}

    /**
     * Generiert ein leeres Brett etc.
     */
//    public void zuruecksetzen() {
//    }

    /**
     * Gibt das Spielbrett zur�ck.
     * @return Das aktuelle Spielbrett.
     */
    public Spielbrett getSpielbrett() {
    	return sb;
    }

    /**
     * Gibt das aktuelle, interne Spielbrett zur�ck, das immer wieder gedreht wird.
     * @return Das aktuelle Spielbrett.
     */
    //public Spielbrett getSpielbrettInt() {
    //	return sb;
    //}

    /**
     * Gibt die aktuelle Farbe zur�ck.
     * @return Die aktuelle Farbe.
     */
    public int getFarbeAmZug() {
    	return sb.getFarbeAmZug();
    }

    /**
     * Gibt den aktuellen Spieler zur�ck.
     * @return Der aktuelle Spieler.
     */
    public ISpieler getSpielerAmZug() {
    	return spieler[sb.getFarbeAmZug()-1];
    }

    /**
     * Gibt den schwarzen Spieler zur�ck.
     * @return Der schwarze Spieler.
     */
    public ISpieler getSpielerSchwarz() {
    	return spieler[0];
    }

    /**
     * Gibt den wei�en Spieler zur�ck.
     * @return Der wei�e Spieler.
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
    		throw new IllegalArgumentException("Der wei�e und schwarze Spieler d�rfen nicht null sein!");
    	
		spieler[0]=schwarz;
		spieler[1]=weiss;

		spieler[0].startGettingNaechstenZug(sb);
    }

    /**
     * Macht die Zugfolge ohne danach die KI (wenn �berhaupt n�tig) zu starten.
     * Archiviert die Zugfolge au�erdem zum R�ckg�ngigmachen. 
     * @param z Die durchzuf�hrende Zugfolge.
     */
	private void macheZugInt(ArrayList<Zug> z) {
		try {
			sb.macheZug(z);

			bisherigeZuege.add(z);
		}
		catch (Exception ex) {
			System.out.println("Konnte Zug im Spielablauf nicht ausf�hren: " + ex.toString());
		}
	}

	/**
	 * Macht die Zugfolge und startet danach die KI (wenn n�tig).
     * Archiviert die Zugfolge au�erdem zum R�ckg�ngigmachen.
	 * @param z Die durchzuf�hrende Zugfolge.
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
    			System.out.print("Wei� zog: ");
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
     * Macht einen r�ckg�ngig Zug r�ckg�ngig. Wenn
     * kein Zug r�ckg�ngig gemacht werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void undoZug() {
    	if (bisherigeZuege.size()>0) {
    		ArrayList<Zug> zf = bisherigeZuege.pop();
    		sb.undoZug(zf);
    		undoneZuege.add(zf);
    		
    		getSpielerAmZug().startGettingNaechstenZug(sb);
    	}
    }

    /**
     * Stellt einen r�ckg�ngig gemachten Zug wieder her. Wenn
     * kein Zug wiederhergestellt werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void redoZug() {
    	if (undoneZuege.size()>0) {
    		ArrayList<Zug> zf = undoneZuege.pop();
    		
    		macheZugInt(zf);

           	switch (sb.isSpielBeendet()) {
           	case Spielbrett.WEISS:
           	case Spielbrett.SCHWARZ:
           		return;
           	default:
           	}

    		getSpielerAmZug().startGettingNaechstenZug(sb);
    	}
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
