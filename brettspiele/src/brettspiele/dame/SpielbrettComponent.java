package brettspiele.dame;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import brettspiele.*;

public class SpielbrettComponent extends SpielbrettReadOnlyComponent implements IBrettspielComponent<ZugFolge> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6402184737019169647L;
	public SpielbrettComponent() throws HeadlessException {
		super();
		
		// Damit auch ohne Listener die process... Methoden funktionieren. 
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	protected ISpieler<? extends ZugFolge> lokalerSpieler;
	public void setLokalerSpieler(ISpieler<? extends ZugFolge> lokalerSpieler) {
		this.lokalerSpieler = lokalerSpieler;
	}
	
	public ISpieler<? extends ZugFolge> getLokalerSpieler() {
		return lokalerSpieler;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

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
			
			//Point pnt = getPaintCoordinateFromMouse(mouseCoord, feld==sb.getEigeneDame());
			int ZweiterKreisOffset = (int)(feldbreite * ZweiterKreisOffsetPercentage / 100);
			
			if (feld==sb.getEigeneDame()) {
				ZweiterKreisOffset*=2;
			}

			int paintX = (int) (mouseCoord.x-feldbreite*SteinPercentage/200+ZweiterKreisOffset);
			int paintY = (int) (mouseCoord.y-feldbreite*SteinPercentage/200+ZweiterKreisOffset);
			zeichneFigur(g, feld, paintX, paintY, java.awt.Color.white, java.awt.Color.gray);
		}
	}

	protected Point getPaintCoordinateFromMouse(Point mc, boolean dame) {
		int ZweiterKreisOffset = (int)(feldbreite * ZweiterKreisOffsetPercentage / 100);
		
		if (dame) {
			ZweiterKreisOffset*=2;
		}

		Point pnt = new Point();
		pnt.x = (int) (mc.x-feldbreite*SteinPercentage/100/2+ZweiterKreisOffset);
		pnt.y = (int) (mc.y-feldbreite*SteinPercentage/100/2+ZweiterKreisOffset);
		return pnt;
	}

	protected Point getTopLeftCoordinateFromMouse(Point mc) {
		Point pnt = new Point();
		pnt.x = (int) (mc.x-feldbreite*SteinPercentage/100/2);
		pnt.y = (int) (mc.y-feldbreite*SteinPercentage/100/2);
		return pnt;
	}

	/**
	 * Überschreibt die Basismethode. Zeichnet den Stein entweder auf dem passenden Feld
	 * oder der temporären Stelle.
	 */
	protected void zeichneFigur(Graphics g, int x, int y) {
		int kreisX=(int)(leftBorder + feldbreite*x + feldbreite * (100-SteinPercentage) / 200);
		int kreisY=(int)(topBorder + feldbreite*y + feldbreite * (100-SteinPercentage) / 200);

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
				kreisX=(int)(leftBorder + feldbreite*tmpX + feldbreite * (100-SteinPercentage) / 200);
				kreisY=(int)(topBorder + feldbreite*tmpY + feldbreite * (100-SteinPercentage) / 200);
				
				zeichneFigur(g, sb.getFeld(x,7-y), kreisX, kreisY, java.awt.Color.white, java.awt.Color.gray );
			}
		}
		else {
			if (clickedCoord.x==x && clickedCoord.y==y) {
				//Wenn der Stein gerade gezogen (Drag'n'Drop)
				//wird, dann diesen Stein nicht normal zeichnen,
				//sondern unten grau.
			}
			else
				zeichneFigur(g, sb.getFeld(x,7-y), kreisX, kreisY);
		}
	}

	private java.awt.Point clickedCoord=new java.awt.Point(-1,-1);
	private java.awt.Point mouseCoord=new java.awt.Point();
	
	private ZugFolge tempZugfolge = new ZugFolge();
	
	protected void processMouseEvent(MouseEvent e) {
		if (sb == null)
			return;
		
		if (e.getID()==MouseEvent.MOUSE_PRESSED) {
			if (e.getButton()==MouseEvent.BUTTON1) {
				//Left Button Down
				clickedCoord = convertControlCoordsToFieldCoords(e.getX(), e.getY());
				
				if (clickedCoord.x!=-1 && clickedCoord.y!=-1) {
					if (sb.getFeld(clickedCoord.x, 7-clickedCoord.y) == lokalerSpieler.getEigeneFarbe() || sb.getFeld(clickedCoord.x, 7-clickedCoord.y) == lokalerSpieler.getEigeneFarbe() + 2) { //(sb.isEigener(clickedCoord.x, 7-clickedCoord.y)) {
						System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);

						mouseCoord = e.getPoint();
						//processMouseMotionEvent(e);
						this.repaint();
					}
					else if (tempZugfolge != null && 
							 tempZugfolge.size()>0 && 
							 tempZugfolge.get(tempZugfolge.size()-1).gibEndeX()==clickedCoord.x && 
							 tempZugfolge.get(tempZugfolge.size()-1).gibEndeY()==7-clickedCoord.y) {

						System.out.println("Clicked " + clickedCoord.x + "," + clickedCoord.y);

						mouseCoord = e.getPoint();
						//processMouseMotionEvent(e);
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

					Zug z = new Zug(clickedCoord.x, 7-clickedCoord.y, dest.x, 7-dest.y);

					//ist das ein Fortsetzungszug?
					if (tempZugfolge.size()==0) {
						//-->kein Fortsetzungszug
						ZugFolge zugfolge = new ZugFolge();
						zugfolge.add(z);

						//ist das ein vollständiger Zug?
						if (sb.zugIstGueltig(zugfolge, true)) {
							beendeZug(zugfolge);
						}
						//oder zumindest ein erlaubter Beginn eines komplexeren Sprungs
						else if (sb.isSprung(z) && sb.zugIstGueltig(zugfolge, false)) {
							tempZugfolge.add(z);
						}
					}
					else {
						//-->Fortsetzungszug
						tempZugfolge.add(z);
						
						//ist das ein vollständiger Zug?
						if (sb.zugIstGueltig(tempZugfolge, true)) {
							//committen
							beendeZug(tempZugfolge);
							tempZugfolge = new ZugFolge();
						}
						//oder zumindest die erlaubte Fortsetzung eines komplexeren Sprungs
						else if (sb.zugIstGueltig(tempZugfolge, false)) {
							
						}
						else {
							//ungültiger Teilsprung, diesen wieder zurücknehmen
							tempZugfolge.remove(tempZugfolge.size()-1);
						}
					}
				}
				
				clickedCoord.x=-1;
				clickedCoord.y=-1;
				
				this.repaint();
			}
		}

		super.processMouseEvent(e);
	}
	
	private ArrayList<ZugBeendetListener<? extends ZugFolge>> zbls = new ArrayList<ZugBeendetListener<? extends ZugFolge>>();
	public void addZugBeendetListener(ZugBeendetListener<? extends ZugFolge> zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}

	public void removeZugBeendetListener(ZugBeendetListener<? extends ZugFolge> zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}

	public void clearZugBeendetListeners() {
		zbls.clear();
	}

	protected void beendeZug(ZugFolge zugfolge) {
		ZugBeendetEvent<ZugFolge> zbe = new ZugBeendetEvent<ZugFolge>(this, lokalerSpieler, zugfolge);
		
		for (ZugBeendetListener<ZugFolge> zbl : (Iterable<ZugBeendetListener<ZugFolge>>)zbls.clone()) {
			zbl.zugBeendet(zbe);
		}
	}
	
	protected void processMouseMotionEvent(MouseEvent e) {
		if (clickedCoord.x != -1 && clickedCoord.y != -1) {
			Rectangle clip = new Rectangle(0,0,-1,-1);
			
			Point pnt = getTopLeftCoordinateFromMouse(mouseCoord);
			clip.add(pnt);
			clip.add(pnt.x + feldbreite, pnt.y + feldbreite);

			mouseCoord = e.getPoint();

			pnt = getTopLeftCoordinateFromMouse(mouseCoord);
			clip.add(pnt);
			clip.add(pnt.x + feldbreite, pnt.y + feldbreite);
			
			this.repaint(clip.x, clip.y, clip.width, clip.height);
		}
		
		super.processMouseMotionEvent(e);
	}
}
