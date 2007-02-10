package dame;

import java.util.ArrayList;

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
    private ArrayList<ArrayList<Zug>> bisherigeZuege = new ArrayList<ArrayList<Zug>>();

	/*
	public static void main(String[] args) {
		DameFenster df = new DameFenster();
		df.setSize(200, 100);
		df.setVisible(true);
		return;

    	Spielablauf sa = new Spielablauf();
    	ArrayList<Zug> za = new ArrayList<Zug>();
    	za.add(new Zug(0,0,1,1));
    	za.add(new Zug(1,1,2,2));
    	sa.bisherigeZuege.add(za);

    	ArrayList<Zug> za2 = new ArrayList<Zug>();
    	za2.add(new Zug(0,0,1,1));
    	za2.add(new Zug(1,1,2,2));
    	
    	sa.bisherigeZuege.add(za2);
    	sa.gibAus();
    }
   	*/
    
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
        aktuelleFarbe = 1;
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

    private int aktuelleFarbe = 1;

    public int getAktuelleFarbe() {
    	return aktuelleFarbe;
    }
    
   /**
    * Startet ein Spiel.
    *
    */
    public void starten() {
        aktuelleFarbe = 1;

        while (kis[aktuelleFarbe] != null) {
           	if (aktuelleFarbe == 1) {
           		sb.schwarzAmZug();
           	}
           	else {
           		sb.weissAmZug();
           	}
           	
           	Spielbrett tmpSB = sb.clone();
           	ArrayList<Zug> zugfolge = kis[aktuelleFarbe].gibNaechstenZug(tmpSB, aktuelleFarbe+1);
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

            if (aktuelleFarbe==1)
                aktuelleFarbe=0;
            else
                aktuelleFarbe=1;

           	if (aktuelleFarbe == 1) {
           		sb.schwarzAmZug();
           	}
           	else {
           		sb.weissAmZug();
           	}
           	
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

       	switch (sb.isSpielBeendet()) {
       	case Spielbrett.WEISS:
       	case Spielbrett.SCHWARZ:
       		return;
       	default:
       	}
    	
		if (kis[aktuelleFarbe] != null) {
        	Spielbrett tmpSB = sb.clone();
        	ArrayList<Zug> zugfolge = kis[aktuelleFarbe].gibNaechstenZug(tmpSB, aktuelleFarbe+1);
           	macheZug(zugfolge);
		}
	}

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
}
