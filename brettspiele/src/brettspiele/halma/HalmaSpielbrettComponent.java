package brettspiele.halma;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;

import brettspiele.*;

public class HalmaSpielbrettComponent extends JComponent implements	IBrettspielComponent<Zug>, ComponentListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 153730936058352848L;
	protected final int leftBorder = 0; 
	protected final int rightBorder = 0; 
	protected final int topBorder = 0; 
	protected final int bottomBorder = 0; 

	public HalmaSpielbrettComponent() {
		super();
		
		// Damit auch ohne Listener die process... Methoden funktionieren. 
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
		addComponentListener(this);
	}

	protected ISpieler<? extends Zug> lokalerSpieler;
	
	public void setLokalerSpieler(ISpieler<? extends Zug> lokalerSpieler) {
		this.lokalerSpieler = lokalerSpieler;
	}

	public ISpieler<? extends Zug> getLokalerSpieler() {
		return lokalerSpieler;
	}

	private ArrayList<ZugBeendetListener<? extends Zug>> zbls = new ArrayList<ZugBeendetListener<? extends Zug>>();
	public void addZugBeendetListener(ZugBeendetListener<? extends Zug> zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}

	public void removeZugBeendetListener(ZugBeendetListener<? extends Zug> zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}

	public void clearZugBeendetListeners() {
		zbls.clear();
	}

	protected void beendeZug(Zug z) {
		ZugBeendetEvent<Zug> zbe = new ZugBeendetEvent<Zug>(this, lokalerSpieler, z);
		
		for (ZugBeendetListener<Zug> zbl : (Iterable<ZugBeendetListener<Zug>>)zbls.clone()) {
			zbl.zugBeendet(zbe);
		}
	}

	//Das Spielbrett das gezeichnet wird
	protected HalmaSpielbrett sb;

	public HalmaSpielbrett getSpielsituation() {
		return sb;
	}

	public void setSpielsituation(ISpielsituation wert) {
		setSpielsituation((HalmaSpielbrett)wert);
	}

	public void setSpielsituation(HalmaSpielbrett wert) {
		sb=wert;
		this.repaint();
	}
	
	private enum UpDownEnum {
		Up,
		Down
	}

	private int rasterWidth;
	private int rasterHeight;
	private int figureRadX;
	private int figureRadY;

	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//Linien von oben links nach unten rechts schwarz
		g.setColor(Color.BLACK);
		
	    for (int i=0;i<=3;i++)
	      g.drawLine((6 + i) * rasterWidth, (8 - i) * rasterHeight, (11 + 2 * i) * rasterWidth, 13 * rasterHeight);

	    for (int i=0;i<=2;i++)
	      g.drawLine((11 + 2 * i) * rasterWidth, 5 * rasterHeight, (18 + i) * rasterWidth, (12 - i) * rasterHeight);

	      //Linien von oben rechts nach unten links schwarz
	    for (int i=0;i<=3;i++)
	      g.drawLine((20 - i) * rasterWidth, (8 - i) * rasterHeight, (15 - 2 * i) * rasterWidth, 13 * rasterHeight);

	    for (int i=0;i<=2;i++)
	      g.drawLine((15 - 2 * i) * rasterWidth, 5 * rasterHeight, (8 - i) * rasterWidth, (12 - i) * rasterHeight);

	      //Waagrechte Linien
	    for (int i=0;i<=3;i++)
	      g.drawLine((8 - i) * rasterWidth, (6 + i) * rasterHeight, (18 + i) * rasterWidth, (6 + i) * rasterHeight);

	    for (int i=0;i<=2;i++)
	      g.drawLine((6 + i) * rasterWidth, (10 + i) * rasterHeight, (20 - i) * rasterWidth, (10 + i) * rasterHeight);

	    paintArrow(g, UpDownEnum.Up, Color.red, 13, 1);
	    paintArrow(g, UpDownEnum.Up, Color.blue, 5, 9);
	    paintArrow(g, UpDownEnum.Up, new Color(0,150,0), 21, 9);
	    paintArrow(g, UpDownEnum.Down, Color.red, 13, 17);
	    paintArrow(g, UpDownEnum.Down, new Color(0,150,0), 5, 9);
	    paintArrow(g, UpDownEnum.Down, Color.blue, 21, 9);
		
	    if (sb!=null)
	    	paintFiguren(g);
	}

	private void paintArrow(Graphics g, UpDownEnum ud, Color col, int x, int y) {
		g.setColor(col);

		int UpDown = 1; 
		if (ud==UpDownEnum.Down)
			UpDown=-1;
		
		for (int i=0;i<=3;i++) {
			g.drawLine((x - i) * rasterWidth, (y + i * UpDown) * rasterHeight, (x + 4 - 2 * i) * rasterWidth, (y + 4 * UpDown) * rasterHeight);
			g.drawLine((x + i) * rasterWidth, (y + i * UpDown) * rasterHeight, (x - 4 + 2 * i) * rasterWidth, (y + 4 * UpDown) * rasterHeight);
			g.drawLine((x - (i + 1)) * rasterWidth, (y + (i + 1) * UpDown) * rasterHeight, (x + (i + 1)) * rasterWidth, (y + (i + 1) * UpDown) * rasterHeight);
		}
	}
	
	private void paintFiguren(Graphics g) {
		for (int y=0;y<=16;y++) {
			for (int x=y%2;x<=24;x+=2) {
				switch (sb.getFeld(x, y)) {
				case HalmaSpielbrett.ROT:
					g.setColor(Color.RED);
					break;
				case HalmaSpielbrett.BLAU:
					g.setColor(Color.BLUE);
					break;
				case HalmaSpielbrett.GRUEN:
					g.setColor(Color.GREEN);
					break;
				default:
					continue;
				}

				if (clickedCoord.x==x && clickedCoord.y==y) {
					g.setColor(Color.GRAY);
					g.fillOval( mouseCoord.x - figureRadX, mouseCoord.y - figureRadY, 2 * figureRadX, 2 * figureRadY);
				}
				else
					g.fillOval( (x + 1) * rasterWidth - figureRadX, (y+1) * rasterHeight - figureRadY, 2 * figureRadX, 2 * figureRadY);
			}
	    }
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
    	rasterWidth = this.getWidth() / 26;
    	rasterHeight = this.getHeight() / 18;

    	figureRadX = this.getWidth() / 26 / 4;
    	figureRadY = this.getHeight() / 18 / 4;
    	repaint();
	}

	public void componentShown(ComponentEvent e) {
	}
	
	private java.awt.Point clickedCoord=new java.awt.Point(-1,-1);
	private java.awt.Point mouseCoord=new java.awt.Point();
	
	protected void processMouseEvent(MouseEvent e) {
		if (sb == null)
			return;
		
		if (e.getID()==MouseEvent.MOUSE_PRESSED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//Left Button Down
				clickedCoord = convertControlCoordsToFieldCoords(e.getX(), e.getY());
				
				if (clickedCoord.x!=-1 && clickedCoord.y!=-1) {
					if (sb.getFeld(clickedCoord.x, clickedCoord.y) == lokalerSpieler.getEigeneFarbe()) {
						System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);

						mouseCoord = new java.awt.Point(e.getX(), e.getY());
						this.repaint();
					}
					else {
						System.out.println("Clicked empty or opponent's field " + clickedCoord.x + "," + clickedCoord.y);
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
				//Left Button Up
				
				//wurde vorher richtig geklickt?
				if (clickedCoord.x != -1 && clickedCoord.y != -1) {
					java.awt.Point dest = convertControlCoordsToFieldCoords(e.getX(), e.getY());

					Zug z = new Zug(clickedCoord.x, clickedCoord.y, dest.x, dest.y);

					//Zug pr�fen
					if (sb.isGueltigerZug(z)) {
						beendeZug(z);
					}
				}
				
				clickedCoord.x=-1;
				clickedCoord.y=-1;
				
				this.repaint();
			}
		}

		super.processMouseEvent(e);
	}
	
	protected void processMouseMotionEvent(MouseEvent e) {
		if (clickedCoord.x != -1 && clickedCoord.y != -1) {
			Rectangle clip = new Rectangle(0,0,-1,-1);
			
			Point pnt = mouseCoord;
			pnt.x-=figureRadX;
			pnt.y-=figureRadY;
			clip.add(pnt);
			clip.add(pnt.x + figureRadX*2, pnt.y + figureRadY*2);

			mouseCoord = e.getPoint();

			pnt = (Point)mouseCoord.clone();
			pnt.x-=figureRadX;
			pnt.y-=figureRadY;
			clip.add(pnt);
			clip.add(pnt.x + figureRadX*2, pnt.y + figureRadY*2);
			
			this.repaint(clip.x, clip.y, clip.width, clip.height);
		}
		
		super.processMouseMotionEvent(e);
	}
	
	/**
	 * Konvertiert Grafikkoordinaten in Spielbrettkoordinaten bzw. (-1,-1),
	 * wenn es au�erhalb ist.
	 * @param xControl Die Grafik-X-Koordinate. 
	 * @param yControl Die Grafik-X-Koordinate.
	 * @return Die Spielbrettkoordinaten in einem java.awt.Point.
	 */
	protected final java.awt.Point convertControlCoordsToFieldCoords(int xControl, int yControl) {
		java.awt.Point pnt = new java.awt.Point();
		
		//cast zu double und Math.floor wegen Rundungsproblematik
		pnt.y = (int)Math.floor((double)(yControl-topBorder)/rasterHeight+0.5) - 1;
		pnt.x = (int)Math.floor(((double)(xControl-leftBorder)/rasterWidth+pnt.y%2)/2)*2 - pnt.y%2;
		
		if (!HalmaSpielbrett.isGueltigeKoordinate(pnt.x, pnt.y)) {
			pnt.x=-1;
			pnt.y=-1;
		}
			
		return pnt;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = this.getSize();
		int seitenlaenge = Math.min(d.height, d.width);
		//seitenl�nge*=8;
		d.setSize(seitenlaenge,seitenlaenge);
		return d;
	}
	
	@Override
	public boolean isPreferredSizeSet() {
		return true;
	}
}
