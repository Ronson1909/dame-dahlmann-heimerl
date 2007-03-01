package brettspiele.schafkopf;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JComponent;

import brettspiele.IBrettspielComponent;
import brettspiele.IZug;
import brettspiele.ISpieler;
import brettspiele.ISpielsituation;
import brettspiele.ZugBeendetEvent;
import brettspiele.ZugBeendetListener;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

public class SchafkopfSpielbrettComponent extends JComponent implements
		IBrettspielComponent<IZug> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2238170873364751237L;

	private Image[] kartenbilder = new Image[32];
	private int kartenhoehe = 0;
	private int kartenbreite = 0;
	private final double ueberlappung = 3.0/6.0; 
	private final double abstand = 1-ueberlappung; 
	
	public SchafkopfSpielbrettComponent() {
		Toolkit toolkit = this.getToolkit(); //Toolkit.getDefaultToolkit();
		MediaTracker mediaTracker = new MediaTracker(this);
		for (int i=1;i<=32;i++) {
			kartenbilder[i-1] = toolkit.getImage(ClassLoader.getSystemResource("brettspiele/schafkopf/images/" + i + ".png"));
			mediaTracker.addImage(kartenbilder[i-1], i-1);
		}

		try {
			mediaTracker.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		kartenhoehe = kartenbilder[0].getHeight(this);
		kartenbreite = kartenbilder[0].getWidth(this);

		// Damit auch ohne Listener die process... Methoden funktionieren. 
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK);// | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	protected ISpieler<? extends IZug> lokalerSpieler;
	
	public void setLokalerSpieler(ISpieler<? extends IZug> lokalerSpieler) {
		this.lokalerSpieler = lokalerSpieler;
	}

	public ISpieler<? extends IZug> getLokalerSpieler() {
		return lokalerSpieler;
	}

	private ArrayList<ZugBeendetListener<? extends IZug>> zbls = new ArrayList<ZugBeendetListener<? extends IZug>>();
	public void addZugBeendetListener(ZugBeendetListener<? extends IZug> zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}

	public void removeZugBeendetListener(ZugBeendetListener<? extends IZug> zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}

	public void clearZugBeendetListeners() {
		zbls.clear();
	}

	protected void beendeZug(IZug z) {
		ZugBeendetEvent<IZug> zbe = new ZugBeendetEvent<IZug>(this, lokalerSpieler, z);
		
		for (ZugBeendetListener<IZug> zbl : (Iterable<ZugBeendetListener<IZug>>)zbls.clone()) {
			zbl.zugBeendet(zbe);
		}
	}

	//Das Spielbrett das gezeichnet wird
	protected SchafkopfSpielsituation ss;

	public SchafkopfSpielsituation getSpielsituation() {
		return ss;
	}

	public void setSpielsituation(ISpielsituation wert) {
		setSpielsituation((SchafkopfSpielsituation)wert);
	}

	public void setSpielsituation(SchafkopfSpielsituation wert) {
		ss=wert;
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		Rectangle clip = g2d.getClipBounds();
		g2d.setColor(new Color(0,100,0));
		g2d.fillRect(clip.x, clip.y, clip.width, clip.height);
		//g2d.clearRect(this.getBounds());// clip.x, clip.y, clip.width, clip.height);

		if (ss == null)
			return;
		
		for (int sp=0;sp<=3;sp++) {
			Spielkarten[] kartenDesSpielers = ss.getKartenDesSpielers(sp);

			int startX=0;
			int startY=0;
			int rot=0;
			
			switch (sp) {
			case 0:
				startX=kartenhoehe;
				startY=this.getHeight()-kartenhoehe;
				rot=0;
				break;
			case 1:
				startX=kartenhoehe;
				startY=-kartenhoehe;
				rot=90;
				break;
			case 2:
				startX=-(this.getWidth()-kartenhoehe);
				startY=-kartenhoehe;
				rot=180;
				break;
			case 3:
				startX=-(this.getHeight()-kartenhoehe);
				startY=this.getWidth()-kartenhoehe;
				rot=270;
				break;
			}

			int dX = (int) (kartenbreite * abstand);
			for (int i=kartenDesSpielers.length-1; i>=0; i--) {
				AffineTransform affineTransform = new AffineTransform();
				affineTransform.rotate(Math.toRadians(rot), 0, 0);//kartenbreite/2, kartenhoehe/2); 
				affineTransform.translate(i*dX, 0);
				affineTransform.translate(startX, startY);

				g2d.drawImage(kartenbilder[kartenDesSpielers[i].ordinal()], affineTransform, this); 
			}
		}

		int rot=ss.getAusspielenderSpieler()*90;
		for (int i=0;i<ss.getStich().length;i++) {
			Spielkarten karte = ss.getStich()[i];
			
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.translate(this.getWidth()/2-kartenbreite/2, this.getHeight()/2-kartenhoehe/2);
			affineTransform.rotate(Math.toRadians(rot), kartenbreite/2, kartenhoehe/2);//kartenbreite/2, kartenhoehe/2); 
			affineTransform.translate(0, kartenhoehe/2);

			g2d.drawImage(kartenbilder[karte.ordinal()], affineTransform, this); 

			rot += 90;
		}
	}

	protected void processMouseEvent(MouseEvent e) {
		if (ss == null)
			return;
		
		if (e.getID()==MouseEvent.MOUSE_CLICKED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//Left Button Click
				Point clickedCoord = convertControlCoordsToFieldCoords(e.getX(), e.getY());
				
				if (clickedCoord.x != -1 && clickedCoord.y != -1) {
					if (clickedCoord.x < ss.getEigeneKarten().length) {
						System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);
						beendeZug(new Ausspielvorgang(ss.getEigeneKarten()[clickedCoord.x]));
					}
					else {
						System.out.println("Clicked invalid (own) card!");
					}
				}
				else {
					System.out.println("Clicked invalid (other) card!");
				}
			}
		}

		super.processMouseEvent(e);
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
		pnt.x = xControl-kartenhoehe;
		pnt.y = yControl-(this.getHeight()-kartenhoehe);

		if (pnt.x<0 || pnt.x > kartenbreite + 7*abstand*kartenbreite || pnt.y<0 || pnt.y>kartenhoehe) {
			pnt.x=-1;
			pnt.y=-1;
		}
		else {
			pnt.x = Math.max(0, (int) ((pnt.x-kartenbreite*ueberlappung)/(kartenbreite*abstand)));
		}
			
		return pnt;
	}
	
	@Override
	public Dimension getPreferredSize() {
		int handbreite = (int) (kartenbreite + 7*abstand*kartenbreite);
		
		return new Dimension(handbreite+2*kartenhoehe,handbreite+2*kartenhoehe);
	}
	
	@Override
	public boolean isPreferredSizeSet() {
		return true;
	}
}
