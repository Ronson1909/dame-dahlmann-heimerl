package brettspiele.schafkopf;

import brettspiele.IZug;

public class Spielartwahl implements IZug {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1692341853623834099L;

	private ISpielart gewaehltesSpiel = null;

	/**
	 * Erstellt einen neuen Spielartwahl-Zug.
	 * @param Die zu w�hlende Spielart oder null f�r "weiter".
	 */
	public Spielartwahl(ISpielart gewaehltesSpiel) {
		super();
		this.gewaehltesSpiel = gewaehltesSpiel;
	}

	/**
	 * Gibt das gew�hlte Spiel zur�ck.
	 * @return Das gew�hlte Spiel.
	 */
	public ISpielart getGewaehltesSpiel() {
		return gewaehltesSpiel;
	}

	/**
	 * Setzt das gew�hlte Spiel.
	 * @param Das gew�hlte Spiel, das gesetzt werden soll.
	 */
	public void setGewaehltesSpiel(ISpielart gewaehltesSpiel) {
		this.gewaehltesSpiel = gewaehltesSpiel;
	} 
}
