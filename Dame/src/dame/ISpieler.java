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
	 * Damit wird die Zugberechnung unserer KI angesprochen. Übergeben wird eine
	 * Kopie des Spielbretts und die Farbe der KI.
	 * @param sb Kopie des Spielbretts
	 * @param eigeneFarbe Farbe der KI
	 * @return Eine ArrayList mit Zügen, die durchgeführt werden sollen. ArrayList
	 * wegen möglicherweise mehreren Sprüngen.
	 */
    //public java.util.ArrayList<Zug> startGetNaechstenZug(Spielbrett sb, int eigeneFarbe);

}
