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
	 * @param Die zu wählende Spielart oder null für "weiter".
	 */
	public Spielartwahl(ISpielart gewaehltesSpiel) {
		super();
		this.gewaehltesSpiel = gewaehltesSpiel;
	}

	/**
	 * Gibt das gewählte Spiel zurück.
	 * @return Das gewählte Spiel.
	 */
	public ISpielart getGewaehltesSpiel() {
		return gewaehltesSpiel;
	}

	/**
	 * Setzt das gewählte Spiel.
	 * @param Das gewählte Spiel, das gesetzt werden soll.
	 */
	public void setGewaehltesSpiel(ISpielart gewaehltesSpiel) {
		this.gewaehltesSpiel = gewaehltesSpiel;
	} 
}
