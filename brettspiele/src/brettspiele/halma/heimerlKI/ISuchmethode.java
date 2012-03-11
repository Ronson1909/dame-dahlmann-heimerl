package brettspiele.halma.heimerlKI;

import brettspiele.halma.*;

public interface ISuchmethode {
	/**
	 * 
	 * @param sb
	 * @param searchLevel
	 * @return
	 */
	public BewerteterZug getBestenZug(HalmaSpielbrett sb, int searchLevel);
}
