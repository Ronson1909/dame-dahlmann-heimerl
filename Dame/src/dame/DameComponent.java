package dame;

//import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DameComponent extends javax.swing.JList {
	public DameComponent() throws HeadlessException {
		// Damit auch ohne Listener die process... Methoden funktionieren. 
		this.enableEvents(java.awt.AWTEvent.MOUSE_EVENT_MASK | java.awt.AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	/*
	public DameFenster(GraphicsConfiguration arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public DameFenster(String arg0) throws HeadlessException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public DameFenster(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}
	 */
	
	//Das Spielbrett das gezeichnet wird (kann temporär verändert werden)
	private Spielbrett sb;// = new Spielbrett();

	//Das Spielbrett das verwaltet wird.
	//private Spielbrett sbmain;
	
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
		//sbmain=wert;
		sb=wert;//sbmain.clone();
		this.repaint();
	}

	private final float SteinPercentage = 75;
	private final int ZweiterKreisOffsetPercentage = 10;
	private final int leftBorder = 0; 
	private final int rightBorder = 0; 
	private final int topBorder = 0; 
	private final int bottomBorder = 0; 

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		//g.clearRect(0, 0, this.getWidth(), this.getHeight());
		java.awt.Rectangle clip = g.getClipBounds();
		
		g.clearRect(clip.x, clip.y, clip.width, clip.height);
		if (sb == null)
			return;
		
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

		//Steine zeichnen
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				int kreisX=(int)(leftBorder + Feldbreite*x + Feldbreite * (100-SteinPercentage) / 200);
				int kreisY=(int)(topBorder + Feldbreite*y + Feldbreite * (100-SteinPercentage) / 200);

				if (tempZugfolge != null && 
				    tempZugfolge.size()>0 && 
					tempZugfolge.get(0).gibStartX()==x && 
					tempZugfolge.get(0).gibStartY()==7-y) {

					int tmpX=tempZugfolge.get(tempZugfolge.size()-1).gibEndeX();
					int tmpY=7-tempZugfolge.get(tempZugfolge.size()-1).gibEndeY();

					if (clickedCoord.x==tmpX && clickedCoord.y==tmpY) {
						//Wenn das Stein gerade gezogen (Drag'n'Drop)
						//wird, dann diesen Stein nicht normal zeichnen,
						//sondern unten grau.
					}
					else {
						kreisX=(int)(leftBorder + Feldbreite*tmpX + Feldbreite * (100-SteinPercentage) / 200);
						kreisY=(int)(topBorder + Feldbreite*tmpY + Feldbreite * (100-SteinPercentage) / 200);
						
						zeichneFigur(g, sb.getFeld(x,7-y), kreisX, kreisY, java.awt.Color.white, java.awt.Color.gray );
					}
				}
				else {
					if (clickedCoord.x==x && clickedCoord.y==y) {
						//Wenn das Stein gerade gezogen (Drag'n'Drop)
						//wird, dann diesen Stein nicht normal zeichnen,
						//sondern unten grau.
					}
					else
						zeichneFigur(g, sb.getFeld(x,7-y), kreisX, kreisY);
				}
			}
		}
		
		//Wenn das Stein gerade gezogen (Drag'n'Drop)
		//wird, dann diesen Stein grau zeichnen.
		if (clickedCoord.x!=-1 && clickedCoord.y!=-1) {
			int feld=0;
			if (tempZugfolge != null && 
				tempZugfolge.size()>0) {

				feld=sb.getFeld(tempZugfolge.get(0).gibStartX(), tempZugfolge.get(0).gibStartY());				
			}
			else {
				feld=sb.getFeld(clickedCoord.x, 7-clickedCoord.y);				
			}
			
			int ZweiterKreisOffset = (int)(Feldbreite * ZweiterKreisOffsetPercentage / 100);
			if (feld==sb.getEigeneDame()) {
				ZweiterKreisOffset*=2;
			}

			int paintX = (int) (mouseCoord.x-Feldbreite*SteinPercentage/200+ZweiterKreisOffset);
			int paintY = (int) (mouseCoord.y-Feldbreite*SteinPercentage/200+ZweiterKreisOffset);
			zeichneFigur(g, feld, paintX, paintY, java.awt.Color.white, java.awt.Color.gray);
		}
		
	
		//g.setFont(java.awt.Font.getFont(java.awt.Font.SANS_SERIF));
		//g.setColor(java.awt.Color.red);
		//g.drawString(clickedX + "," + clickedY,50,50);
		
		//super.paint(g);
	}
	
	/**
	 * Zeichnet einen Stein an der entsprechenden Stelle mit den Farben, die figur vorgibt.
	 * @param g Das Graphikobjekt.
	 * @param figur Der Stein (also normal oder Dame und welche Farbe).
	 * @param kreisX Die X-Koordinate des Zentrums des Kreises der Bodenfläche.
	 * @param kreisY Die Y-Koordinate des Zentrums des Kreises der Bodenfläche.
	 */
	private void zeichneFigur(Graphics g, int figur, int kreisX, int kreisY) {
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
	private void zeichneFigur(Graphics g, int figur, int kreisX, int kreisY, java.awt.Color foreground, java.awt.Color background) {
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

	private java.awt.Point clickedCoord=new java.awt.Point(-1,-1);
	private java.awt.Point mouseCoord=new java.awt.Point();
	
	private ArrayList<Zug> tempZugfolge = new ArrayList<Zug>();
	
	protected void processMouseEvent(MouseEvent e) {
		if (sb == null)
			return;
		
		if (e.getID()==MouseEvent.MOUSE_PRESSED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//MouseDown
				clickedCoord = convertControlCoordsToFieldCoords(e.getX(), e.getY());
				
				if (clickedCoord.x!=-1 && clickedCoord.y!=-1) {
					if (sb.isEigener(clickedCoord.x, 7-clickedCoord.y)) {
						System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);

						mouseCoord = new java.awt.Point(e.getX(), e.getY());
						this.repaint();
					}
					else if (tempZugfolge != null && 
							 tempZugfolge.size()>0 && 
							 tempZugfolge.get(tempZugfolge.size()-1).gibEndeX()==clickedCoord.x && 
							 tempZugfolge.get(tempZugfolge.size()-1).gibEndeY()==7-clickedCoord.y) {

						System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);

						mouseCoord = new java.awt.Point(e.getX(), e.getY());
						this.repaint();
					}
					else {
						System.out.println("Clicked empty field " + clickedCoord.x + "," + clickedCoord.y);
						clickedCoord.x=-1;
						clickedCoord.y=-1;
					}
				}
				else {
					System.out.println("Clicked invalid field");
				}
			}
		}
		else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//MouseUp
				if (clickedCoord.x != -1 && clickedCoord.y != -1) {
					java.awt.Point dest = convertControlCoordsToFieldCoords(e.getX(), e.getY());

					Zug z = new Zug(clickedCoord.x, 7-clickedCoord.y, dest.x, 7-dest.y);

					if (tempZugfolge.size()==0) {
						ArrayList<Zug> zugfolge = new ArrayList<Zug>();
						zugfolge.add(z);

						if (sb.isSprung(z)) {
							if (sb.zugIstGueltig(zugfolge, true)) {
								beendeZug(zugfolge);
							}
							else if (sb.zugIstGueltig(zugfolge, false)) {
								tempZugfolge.add(z);
							}
						}
						else if (sb.isZug(z)) {
							if (sb.zugIstGueltig(zugfolge, true)) {
								beendeZug(zugfolge);
							}
						}
					}
					else {
						tempZugfolge.add(z);
						
						if (sb.zugIstGueltig(tempZugfolge, true)) {
							//committen
							beendeZug(tempZugfolge);
							tempZugfolge.clear();
						}
						else if (sb.zugIstGueltig(tempZugfolge, false)) {
							
						}
						else {
							//ungültiger Teilsprung, diesen Teilsprung doch nicht committen
							tempZugfolge.remove(tempZugfolge.size()-1);
						}
					}
				}
					
	
				clickedCoord.x=-1;
				clickedCoord.y=-1;
				
				//final int Feldbreite = getFeldbreite();
				this.repaint();//mouseCoord.x-Feldbreite, mouseCoord.y-Feldbreite, Feldbreite*2, Feldbreite*2);
			}
		}

		super.processMouseEvent(e);
	}
	
	public class ZugBeendetEvent extends java.util.EventObject {
		private ArrayList<Zug> zugfolge;
		
		private ZugBeendetEvent(Object source, ArrayList<Zug> zugfolge) {
			super(source);
			this.zugfolge=zugfolge;
		}
		
		public ArrayList<Zug> getZugfolge() {
			return zugfolge;
		}
	}
	
	public interface ZugBeendetListener extends java.util.EventListener {
		public void zugBeendet(ZugBeendetEvent zbe);
	}

	
	public class ZugBeendetAdapter implements ZugBeendetListener {
		public void zugBeendet(ZugBeendetEvent zbe) {
			
		}
	}

	private ArrayList<ZugBeendetListener> zbls = new ArrayList<ZugBeendetListener>();
	public void addZugBeendetListener(ZugBeendetListener zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}

	public void removeZugBeendetListener(ZugBeendetListener zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}
	
	protected void beendeZug(ArrayList<Zug> zugfolge) {
		ZugBeendetEvent zbe = new ZugBeendetEvent(this, zugfolge);
		
		for (ZugBeendetListener zbl : zbls) {
			zbl.zugBeendet(zbe);
		}
	}
	
	protected void processMouseMotionEvent(MouseEvent e) {
		if (clickedCoord.x != -1 && clickedCoord.y != -1) {
			mouseCoord = new java.awt.Point(e.getX(), e.getY());

			//final int Feldbreite = getFeldbreite();
			this.repaint();//mouseCoord.x-Feldbreite, mouseCoord.y-Feldbreite, Feldbreite*2, Feldbreite*2);
		}
		
		super.processMouseMotionEvent(e);
	}

	/**
	 * Berechnet gemäß der Größe der Komponente die Seitenlänge eines Feldquadrats in Pixeln.
	 * @return Die Seiten eines Feldquadrats in Pixeln.
	 */
	private int getFeldbreite() {
		return Math.min(this.getHeight()-topBorder-bottomBorder, this.getWidth()-leftBorder-rightBorder) / 8;
	}
	
	/**
	 * Konvertiert Grafikkoordinaten in Spielbrettkoordinaten bzw. (-1,-1),
	 * wenn es außerhalb ist.
	 * @param xControl Die Grafik-X-Koordinate. 
	 * @param yControl Die Grafik-X-Koordinate.
	 * @return Die Spielbrettkoordinaten in einem java.awt.Point.
	 */
	private java.awt.Point convertControlCoordsToFieldCoords(int xControl, int yControl) {
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
