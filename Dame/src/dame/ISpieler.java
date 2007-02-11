package dame;

import java.io.Serializable;
import java.util.ArrayList;

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

	//public void init(int eigeneFarbe);
	
	//public void setEigeneFarbe(int eigeneFarbe);
	//public int getEigeneFarbe();
}

class ZugBeendetEvent extends java.util.EventObject {
	private ArrayList<Zug> zugfolge;
	
	public ZugBeendetEvent(Object source, ArrayList<Zug> zugfolge) {
		super(source);
		this.zugfolge=zugfolge;
	}
	
	public ArrayList<Zug> getZugfolge() {
		return zugfolge;
	}
}

interface ZugBeendetListener extends java.util.EventListener {
	public void zugBeendet(ZugBeendetEvent zbe);
}


class ZugBeendetAdapter implements ZugBeendetListener {
	public void zugBeendet(ZugBeendetEvent zbe) {
		
	}
}
