package brettspiele.dame.heimerlKI;

import brettspiele.dame.Spielbrett;

public interface ISuchmethode {
	/**
	 * 
	 * @param sb
	 * @param searchLevel
	 * @return
	 */
	public BewerteteZugFolge calcBestenZug(Spielbrett sb, int searchLevel);
	
	public void configure(java.awt.Window owner);
}
