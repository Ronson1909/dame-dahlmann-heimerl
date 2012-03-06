package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public interface ISpielart extends Comparator<Spielkarten>, Comparable<ISpielart> {
	public Spielkarten getHoechsteKarte(List<Spielkarten> stich);
	public void pruefeAufGueltigeKarte(SchafkopfSpielsituation sss, Spielkarten zuSpielendeKarte);
	public boolean isTrumpf(Spielkarten karte);
	public ArrayList<Spielkarten> getTruempfe(Collection<Spielkarten> hand);
	public int getSpielartValue();
}
