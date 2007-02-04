package dame;

import java.util.ArrayList;

/**
 * <p>‹berschrift: </p>
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
public class Spielablauf {
    private IKI kis[] = new IKI[2];
    private Spielbrett sb;
    private ArrayList<ArrayList<Zug>> bisherigeZuege = new ArrayList<ArrayList<Zug>>();

	public static void main(String[] args) {
		DameFenster df = new DameFenster();
		df.setSize(200, 100);
		df.setVisible(true);
		return;

		/*
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
    	*/
    }
    
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
    * Startet ein Spiel.
    *
    */
    public void starten() {
        int aktuelleFarbe = 1;

        do {
        	Spielbrett tmpSB = sb.clone();
        	ArrayList<Zug> zuege = kis[aktuelleFarbe].gibNaechstenZug(tmpSB, aktuelleFarbe+1);
            //sb.macheZug(zuege);
            bisherigeZuege.add(zuege);
            
            if (aktuelleFarbe==1)
                aktuelleFarbe=0;
            else
                aktuelleFarbe=1;
        } while (true);
    }
    
    public void gibAus() {
        int aktuelleFarbe = 1;
        
    	for (ArrayList<Zug> zugKombi : bisherigeZuege) {
    		if (aktuelleFarbe==1) {
    			System.out.print("Schwarz zog: ");
    		}
    		else {
    			System.out.print("Weiﬂ zog: ");
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
