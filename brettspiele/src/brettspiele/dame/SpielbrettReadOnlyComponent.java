package brettspiele.dame;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;

import brettspiele.ISpielsituation;

public class SpielbrettReadOnlyComponent extends JComponent implements ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4208846500614425379L;

	public SpielbrettReadOnlyComponent() throws HeadlessException {
		addComponentListener(this);
	}

	//Das Spielbrett das gezeichnet wird
	protected Spielbrett sb;

	public Spielbrett getSpielsituation() {
		return sb;
	}

	public void setSpielsituation(ISpielsituation wert) {
		setSpielsituation((Spielbrett)wert);
	}

	public void setSpielsituation(Spielbrett wert) {
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
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//java.awt.Rectangle clip = g.getClipBounds();
		//g.clearRect(clip.x, clip.y, clip.width, clip.height);
		
		//Felder mit passender Farbe zeichnen
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				//if (g.hitClip(x*Feldbreite, y*Feldbreite, Feldbreite, Feldbreite)) {
					//oben links weiß
					if ((x+y) % 2 ==0)
						g.setColor(java.awt.Color.white);
					else
						g.setColor(java.awt.Color.black);
						
					g.fillRect(leftBorder + feldbreite*x, topBorder + y*feldbreite, feldbreite, feldbreite);
				//}
			}
		}

		if (sb == null)
			return;
		
		//Steine zeichnen
		for (int y=0;y<8;y++) {
			for (int x=(y+1)%2;x<8;x+=2) {
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
		final int kreisX=(int)(leftBorder + feldbreite*x + feldbreite * (100-SteinPercentage) / 200);
		final int kreisY=(int)(topBorder + feldbreite*y + feldbreite * (100-SteinPercentage) / 200);
		
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
		final int ZweiterKreisOffset = (int)(feldbreite * ZweiterKreisOffsetPercentage / 100);
		final int kreisD=(int)(feldbreite * SteinPercentage / 100);

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
	//protected final int getFeldbreite() {
	//}
	
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
		pnt.x = (int)Math.floor((double)(xControl-leftBorder)/feldbreite);
		pnt.y = (int)Math.floor((double)(yControl-topBorder)/feldbreite);
		
		if (pnt.y>7 || pnt.y<0 || pnt.x>7 || pnt.x<0) {
			pnt.x=-1;
			pnt.y=-1;
		}
			
		return pnt;
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	protected int feldbreite;
	public void componentResized(ComponentEvent e) {
		feldbreite = Math.min(this.getHeight()-topBorder-bottomBorder, this.getWidth()-leftBorder-rightBorder) / 8;
    	repaint();
	}

	public void componentShown(ComponentEvent e) {
	}
	
	@Override
	public Dimension getPreferredSize() {
		Dimension d = this.getSize();
		int seitenlänge = Math.min(d.height, d.width)/8;
		seitenlänge*=8;
		d.setSize(seitenlänge,seitenlänge);
		return d;
	}
	
	@Override
	public boolean isPreferredSizeSet() {
		return true;
	}
}
