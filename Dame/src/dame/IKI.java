package dame;

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
public interface IKI {
	/**
	 * Damit wird die Zugberechnung unserer KI angesprochen. �bergeben wird eine
	 * Kopie des Spielbretts und die Farbe der KI.
	 * @param sb Kopie des Spielbretts
	 * @param eigeneFarbe Farbe der KI
	 * @return Eine ArrayList mit Z�gen, die durchgef�hrt werden sollen. ArrayList
	 * wegen m�glicherweise mehreren Spr�ngen.
	 */
    public java.util.ArrayList<Zug> gibNaechstenZug(Spielbrett sb, int eigeneFarbe);
}
