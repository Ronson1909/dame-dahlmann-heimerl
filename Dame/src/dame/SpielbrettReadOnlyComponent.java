package dame;

import java.awt.Graphics;
import java.awt.HeadlessException;
import javax.swing.JComponent;

public class SpielbrettReadOnlyComponent extends JComponent {
	public SpielbrettReadOnlyComponent() throws HeadlessException {

	}

	//Das Spielbrett das gezeichnet wird (kann temporär verändert werden)
	protected Spielbrett sb;

	/**
	 * Gibt das angezeigte Spielbrett zurück.
	 * @return Das angezeigte Spielbrett.
	 */
	public Spielbrett getSpielbrett() {
		return sb;
	}

	/**
	 * Setzt das anzuzeigende Spielbrett.
	 * @param wert Das neue Spielbrett.
	 */
	public void setSpielbrett(Spielbrett wert) {
		sb=wert;
		this.repaint();
	}

	protected final float SteinPercentage = 75;
	protected final int ZweiterKreisOffsetPercentage = 10;
	protected final int leftBorder = 0; 
	protected final int rightBorder = 0; 
	protected final int topBorder = 0; 
	protected final int bottomBorder = 0; 

	@Override
	public void paint(Graphics g) {
		java.awt.Rectangle clip = g.getClipBounds();
		
		g.clearRect(clip.x, clip.y, clip.width, clip.height);
		
		final int Feldbreite = getFeldbreite(); 
		
		//Felder mit passender Farbe zeichnen
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				if ((x+y) % 2 ==0)
					g.setColor(java.awt.Color.white);
				else
					g.setColor(java.awt.Color.black);
					
				g.fillRect(leftBorder + Feldbreite*x, topBorder + y*Feldbreite, Feldbreite, Feldbreite);
			}
		}

		if (sb == null)
			return;
		
		//Steine zeichnen
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				if (x % 2 == y % 2)
					continue;
				
				zeichneFigur(g, x, y);
			}
		}
		
		//super.paint(g);
	}

	/**
	 * Zeichnet den Stein der angegebenen (auf die Anzeige bezogenen) Spielbrettkoordinaten.
	 * @param g Das Graphikobjekt.
	 * @param x Der auf die Anzeige bezogene Spielbrett-X-Wert (zwischen 0 und 7).
	 * @param y Der auf die Anzeige bezogene Spielbrett-Y-Wert (zwischen 0 und 7, 0 oben).
	 */
	protected void zeichneFigur(Graphics g, int x, int y) {
		final int Feldbreite = getFeldbreite(); 
		final int kreisX=(int)(leftBorder + Feldbreite*x + Feldbreite * (100-SteinPercentage) / 200);
		final int kreisY=(int)(topBorder + Feldbreite*y + Feldbreite * (100-SteinPercentage) / 200);
		zeichneFigur(g, sb.getFeld(x,7-y), kreisX, kreisY);
	}
	
	/**
	 * Zeichnet einen Stein an der entsprechenden Stelle mit den Farben, die figur vorgibt.
	 * @param g Das Graphikobjekt.
	 * @param figur Der Stein (also normal oder Dame und welche Farbe).
	 * @param kreisX Die X-Koordinate des Zentrums des Kreises der Bodenfläche.
	 * @param kreisY Die Y-Koordinate des Zentrums des Kreises der Bodenfläche.
	 */
	protected void zeichneFigur(Graphics g, int figur, int kreisX, int kreisY) {
		switch (figur) {
		case Spielbrett.SCHWARZ:
		case Spielbrett.SCHWARZ_D:
			zeichneFigur(g, figur, kreisX, kreisY, java.awt.Color.white, java.awt.Color.black);
			break;
		case Spielbrett.WEISS:
		case Spielbrett.WEISS_D:
			zeichneFigur(g, figur, kreisX, kreisY, java.awt.Color.black, java.awt.Color.white);
			break;
		}
	}

	/**
	 * Zeichnet einen Stein an der entsprechenden Stelle mit den entsprechenden Farben.
	 * @param g Das Graphikobjekt.
	 * @param figur Der Stein (also normal oder Dame und welche Farbe, die aber nicht wichtig ist).
	 * @param kreisX Die X-Koordinate des Zentrums des Kreises der Bodenfläche.
	 * @param kreisY Die Y-Koordinate des Zentrums des Kreises der Bodenfläche.
	 * @param foreground Vordergrundfarbe für Rand.
	 * @param background Hintergrundfarbe für Füllung.
	 */
	protected void zeichneFigur(Graphics g, int figur, int kreisX, int kreisY, java.awt.Color foreground, java.awt.Color background) {
		final int Feldbreite = getFeldbreite(); 
		final int ZweiterKreisOffset = (int)(Feldbreite * ZweiterKreisOffsetPercentage / 100);
		final int kreisD=(int)(Feldbreite * SteinPercentage / 100);

		//Zeichne den ersten Stein
		switch (figur) {
		case Spielbrett.SCHWARZ:
		case Spielbrett.SCHWARZ_D:
		case Spielbrett.WEISS:
		case Spielbrett.WEISS_D:
			g.setColor(background);
			g.fillOval(kreisX, kreisY, kreisD, kreisD);
			g.setColor(foreground);
			g.drawOval(kreisX, kreisY, kreisD, kreisD);

			g.setColor(background);
			g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
			g.setColor(foreground);
			g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
		}

		//Zum Zeichnen der Damen nochmal einen Doppelkreis drüber malen
		switch (figur) {
		case Spielbrett.SCHWARZ_D:
		case Spielbrett.WEISS_D:
			kreisX-=ZweiterKreisOffset;
			kreisY-=ZweiterKreisOffset;

			g.setColor(background);
			g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
			g.setColor(foreground);
			g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
			break;
		}
	}

	/**
	 * Berechnet gemäß der Größe der Komponente die Seitenlänge eines Feldquadrats in Pixeln.
	 * @return Die Seiten eines Feldquadrats in Pixeln.
	 */
	protected final int getFeldbreite() {
		return Math.min(this.getHeight()-topBorder-bottomBorder, this.getWidth()-leftBorder-rightBorder) / 8;
	}
	
	/**
	 * Konvertiert Grafikkoordinaten in Spielbrettkoordinaten bzw. (-1,-1),
	 * wenn es außerhalb ist.
	 * @param xControl Die Grafik-X-Koordinate. 
	 * @param yControl Die Grafik-X-Koordinate.
	 * @return Die Spielbrettkoordinaten in einem java.awt.Point.
	 */
	protected final java.awt.Point convertControlCoordsToFieldCoords(int xControl, int yControl) {
		java.awt.Point pnt = new java.awt.Point();
		
		//cast zu double und Math.floor wegen Rundungsproblematik
		pnt.x = (int)Math.floor((double)(xControl-leftBorder)/getFeldbreite());
		pnt.y = (int)Math.floor((double)(yControl-topBorder)/getFeldbreite());
		
		if (pnt.y>7 || pnt.y<0 || pnt.x>7 || pnt.x<0) {
			pnt.x=-1;
			pnt.y=-1;
		}
			
		return pnt;
	}
}
