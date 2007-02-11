package dame;

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
public class Spielablauf implements java.io.Serializable {
    private IKI kis[] = new IKI[2];
    private Spielbrett sb;
    private Stack<ArrayList<Zug>> bisherigeZuege = new Stack<ArrayList<Zug>>();
    private Stack<ArrayList<Zug>> undoneZuege = new Stack<ArrayList<Zug>>();

	public Spielablauf() {
    	//Die KI's laden.
        kis[0]=null;
        kis[1]=null;

        zuruecksetzen();
    }

    /**
     * Generiert ein leeres Brett etc.
     */
    public void zuruecksetzen() {
        sb=new Spielbrett();
    }

    /**
     * Gibt das Spielbrett zurück.
     * @return Das aktuelle Spielbrett.
     */
    public Spielbrett getSpielbrett() {
    	return sb;
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
    * Startet ein Spiel.
    *
    */
    public void starten() {
        while (kis[getFarbeAmZug()-1] != null) {
           	Spielbrett tmpSB = sb.clone();
           	ArrayList<Zug> zugfolge = kis[getFarbeAmZug()-1].gibNaechstenZug(tmpSB, getFarbeAmZug());
           	macheZugInt(zugfolge);

           	switch (sb.isSpielBeendet()) {
           	case Spielbrett.WEISS:
           	case Spielbrett.SCHWARZ:
           		return;
           	default:
           	}
        };
    }

    /**
     * Macht die Zugfolge ohne danach die KI (wenn überhaupt nötig) zu starten.
     * Archiviert die Zugfolge außerdem zum Rückgängigmachen. 
     * @param z Die durchzuführende Zugfolge.
     */
	private void macheZugInt(ArrayList<Zug> z) {
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
    public void macheZug(ArrayList<Zug> z) {
    	macheZugInt(z);
    	
    	undoneZuege.clear();

       	switch (sb.isSpielBeendet()) {
       	case Spielbrett.WEISS:
       	case Spielbrett.SCHWARZ:
       		return;
       	default:
       	}
    	
		if (kis[getFarbeAmZug()-1] != null) {
        	Spielbrett tmpSB = sb.clone();
        	ArrayList<Zug> zugfolge = kis[getFarbeAmZug()-1].gibNaechstenZug(tmpSB, getFarbeAmZug());
           	macheZug(zugfolge);
		}
	}

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
     * Macht einen rückgängig Zug rückgängig. Wenn
     * kein Zug rückgängig gemacht werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void undoZug() {
    	if (bisherigeZuege.size()>0) {
    		ArrayList<Zug> zf = bisherigeZuege.pop();
    		
    		sb.undoZug(zf);
    		
    		undoneZuege.add(zf);
    	}
    }

    /**
     * Stellt einen rückgängig gemachten Zug wieder her. Wenn
     * kein Zug wiederhergestellt werden kann, wird keine
     * Exception geworfen.
     *
     */
    public void redoZug() {
    	if (undoneZuege.size()>0) {
    		ArrayList<Zug> zf = undoneZuege.pop();
    		
    		macheZugInt(zf);
    	}
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
