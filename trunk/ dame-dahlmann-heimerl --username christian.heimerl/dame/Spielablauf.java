package dame;

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
public class Spielablauf {
    public Spielablauf() {
        kis[0]=null;
        kis[1]=null;

        zuruecksetzen();
    }

    private IKI kis[] = new IKI[2];
    private Spielbrett sb;

    public void zuruecksetzen() {
        sb=new Spielbrett();
    }

    public void starten() {
        int aktuelleFarbe = 1;

        do {
            kis[aktuelleFarbe].gibNaechstenZug(sb, aktuelleFarbe+1);
            if (aktuelleFarbe==1)
                aktuelleFarbe=0;
            else
                aktuelleFarbe=2;
        } while (true);
    }
}
