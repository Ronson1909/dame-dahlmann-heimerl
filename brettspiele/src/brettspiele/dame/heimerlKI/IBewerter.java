package brettspiele.dame.heimerlKI;

import brettspiele.dame.Spielbrett;
import brettspiele.dame.ZugFolge;

public interface IBewerter {
	public int bewerteSpielbrett(Spielbrett sb);
	public int bewerteZugfolge(ZugFolge zf, Spielbrett sb);
	public void configure(java.awt.Window owner);
}
