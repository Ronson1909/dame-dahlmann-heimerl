package brettspiele;

import java.io.Serializable;

public interface ISpielsituation extends Serializable, Cloneable {
	public int isSpielBeendet();
	public ISpielsituation clone();
}
