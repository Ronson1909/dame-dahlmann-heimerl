package brettspiele;

/**
 * Interface, das eine JComponent, die eine Spielsituation anzeigen soll,
 * implementieren muss.
 * @author Christian
 *
 */
public interface IBrettspielComponent<Z extends IZug> {
	/**
	 * Setzt den lokalen Spieler, der Züge ausführen darf.
	 * @param lokalerSpieler Der lokale Spieler, der Züge ausführen darf.
	 */
	public void setLokalerSpieler(ISpieler<? extends Z> lokalerSpieler);
	
	/**
	 * Gibt den lokalen Spieler zurück, der Züge ausführen darf.
	 * @return Der lokale Spieler, der Züge ausführen darf.
	 */
	public ISpieler<? extends Z> getLokalerSpieler();

	/**
	 * Gibt die angezeigte Spielsituation zurück.
	 * @return Die angezeigte Spielsituation.
	 */
	public ISpielsituation getSpielsituation();

	/**
	 * Setzt die anzuzeigende Spielsituation.
	 * @param wert Die neue Spielsituation.
	 */
	public void setSpielsituation(ISpielsituation wert);

	public void addZugBeendetListener(ZugBeendetListener<? extends Z> zbl);
	public void removeZugBeendetListener(ZugBeendetListener<? extends Z> zbl);
	public void clearZugBeendetListeners();
	
	public void repaint();
}
