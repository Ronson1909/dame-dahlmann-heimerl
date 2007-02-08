package dame;

//import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.MouseEvent;

public class DameComponent extends javax.swing.JList {
	public DameComponent() throws HeadlessException {
		// TODO Auto-generated constructor stub
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
	
	private Spielbrett sb = new Spielbrett();
	
	public Spielbrett getSpielbrett() {
		return sb;
	}

	public void setSpielbrett(Spielbrett wert) {
		sb=wert;
		this.repaint();
	}

	private final float SteinPercentage = 75;
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
		
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				if ((x+y) % 2 ==0)
					g.setColor(java.awt.Color.white);
				else
					g.setColor(java.awt.Color.black);
					
				g.fillRect(leftBorder + Feldbreite*x, topBorder + y*Feldbreite, Feldbreite, Feldbreite);

				int kreisX=(int)(leftBorder + Feldbreite*x + Feldbreite * (100-SteinPercentage) / 200);
				int kreisY=(int)(topBorder + Feldbreite*y + Feldbreite * (100-SteinPercentage) / 200);

				if (clickedCoord.x==x && clickedCoord.y==7-y) {
					
				}
				else {
					zeichneFigur(g, sb.gibFeld(x,7-y), kreisX, kreisY);
				}
			}
		}
		
		if (clickedCoord.x!=-1 && clickedCoord.y!=-1) {
			zeichneFigur(g, sb.gibFeld(clickedCoord.x,7-clickedCoord.y), mouseCoord.x, mouseCoord.y, java.awt.Color.white, java.awt.Color.gray);
		}
		
	
		//g.setFont(java.awt.Font.getFont(java.awt.Font.SANS_SERIF));
		//g.setColor(java.awt.Color.red);
		//g.drawString(clickedX + "," + clickedY,50,50);
		
		//super.paint(g);
	}
	
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

	private void zeichneFigur(Graphics g, int figur, int kreisX, int kreisY, java.awt.Color foreground, java.awt.Color background) {
		final int Feldbreite = getFeldbreite(); 
		final int ZweiterKreisOffset = (int)(0.1 * Feldbreite);
		final int kreisD=(int)(Feldbreite * SteinPercentage / 100);

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
		kreisX-=ZweiterKreisOffset;
		kreisY-=ZweiterKreisOffset;
		
		switch (figur) {
		case Spielbrett.SCHWARZ_D:
		case Spielbrett.WEISS_D:
			g.setColor(background);
			g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
			g.setColor(foreground);
			g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
			break;
		}
	}

	private java.awt.Point clickedCoord=new java.awt.Point(-1,-1);
	private java.awt.Point mouseCoord=new java.awt.Point();
	
	protected void processMouseEvent(MouseEvent e) {
		if (e.getID()==MouseEvent.MOUSE_PRESSED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//MouseDown
				clickedCoord = convertControlCoordsToFieldCoords(e.getX(), e.getY());
				mouseCoord = new java.awt.Point(e.getX(), e.getY());

				//final int Feldbreite = getFeldbreite();
				this.repaint();//mouseCoord.x-Feldbreite, mouseCoord.y-Feldbreite, Feldbreite*2, Feldbreite*2);
				
				System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);
			}
		}
		else if (e.getID() == MouseEvent.MOUSE_RELEASED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//MouseUp
				if (clickedCoord.x != -1 && clickedCoord.y != -1) {
					java.awt.Point dest = convertControlCoordsToFieldCoords(e.getX(), e.getY());
					try {
						sb.macheZug(new Zug(clickedCoord.x, clickedCoord.y, dest.x, dest.y));
					}
					catch (Exception ex) {
						System.out.println(ex.toString());
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

	protected void processMouseMotionEvent(MouseEvent e) {
		if (clickedCoord.x != -1 && clickedCoord.y != -1) {
			mouseCoord = new java.awt.Point(e.getX(), e.getY());

			//final int Feldbreite = getFeldbreite();
			this.repaint();//mouseCoord.x-Feldbreite, mouseCoord.y-Feldbreite, Feldbreite*2, Feldbreite*2);
		}
		
		super.processMouseMotionEvent(e);
	}

	private int getFeldbreite() {
		return Math.min(this.getHeight()-topBorder-bottomBorder, this.getWidth()-leftBorder-rightBorder) / 8;
	}
	
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
