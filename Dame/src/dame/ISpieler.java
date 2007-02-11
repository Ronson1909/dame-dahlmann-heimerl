package dame;

import java.io.Serializable;

/*
 *     public enum Spielertyp {
    	LokalerMensch,
    	KI,
    	Remote
    }

 */

public interface ISpieler extends Serializable {
	/**
	 * Damit wird die Zugberechnung unserer KI angesprochen. �bergeben wird eine
	 * Kopie des Spielbretts und die Farbe der KI.
	 * @param sb Kopie des Spielbretts
	 * @param eigeneFarbe Farbe der KI
	 * @return Eine ArrayList mit Z�gen, die durchgef�hrt werden sollen. ArrayList
	 * wegen m�glicherweise mehreren Spr�ngen.
	 */
    //public java.util.ArrayList<Zug> startGetNaechstenZug(Spielbrett sb, int eigeneFarbe);

}
