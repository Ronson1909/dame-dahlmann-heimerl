package brettspiele;

/**
 * Interface, das eine JComponent, die eine Spielsituation anzeigen soll,
 * implementieren muss.
 * @author Christian
 *
 */
public interface IBrettspielComponent<Z extends IZug> {
	/**
	 * Setzt den lokalen Spieler, der Z�ge ausf�hren darf.
	 * @param lokalerSpieler Der lokale Spieler, der Z�ge ausf�hren darf.
	 */
	public void setLokalerSpieler(ISpieler lokalerSpieler);
	
	/**
	 * Gibt den lokalen Spieler zur�ck, der Z�ge ausf�hren darf.
	 * @return Der lokale Spieler, der Z�ge ausf�hren darf.
	 */
	public ISpieler getLokalerSpieler();

	/**
	 * Gibt die angezeigte Spielsituation zur�ck.
	 * @return Die angezeigte Spielsituation.
	 */
	public ISpielsituation getSpielsituation();

	/**
	 * Setzt die anzuzeigende Spielsituation.
	 * @param wert Die neue Spielsituation.
	 */
	public void setSpielsituation(ISpielsituation wert);

	public void addZugBeendetListener(ZugBeendetListener<Z> zbl);
	public void removeZugBeendetListener(ZugBeendetListener<Z> zbl);
	public void clearZugBeendetListeners();
	
	public void repaint();
}
