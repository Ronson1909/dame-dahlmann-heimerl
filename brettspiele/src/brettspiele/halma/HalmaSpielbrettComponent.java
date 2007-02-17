package brettspiele.halma;

import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JComponent;

import brettspiele.*;
import brettspiele.dame.ZugFolge;
import brettspiele.dame.ZugFolgeBeendetEvent;

public class HalmaSpielbrettComponent extends JComponent implements	IBrettspielComponent {
	public HalmaSpielbrettComponent() {
		super();
		
		// Damit auch ohne Listener die process... Methoden funktionieren. 
		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
	}

	protected ISpieler lokalerSpieler;
	public void setLokalerSpieler(ISpieler lokalerSpieler) {
		this.lokalerSpieler = lokalerSpieler;
	}

	public ISpieler getLokalerSpieler() {
		return lokalerSpieler;
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

	public void clearZugBeendetListeners() {
		zbls.clear();
	}

	protected void beendeZug(ZugFolge zugfolge) {
		ZugFolgeBeendetEvent zbe = new ZugFolgeBeendetEvent(this, lokalerSpieler, zugfolge);
		
		for (ZugBeendetListener zbl : (Iterable<ZugBeendetListener>)zbls.clone()) {
			zbl.zugBeendet(zbe);
		}
	}

	//Das Spielbrett das gezeichnet wird
	protected HalmaSpielbrett sb;

	public ISpielsituation getSpielsituation() {
		return sb;
	}

	public void setSpielsituation(ISpielsituation wert) {
		sb=(HalmaSpielbrett)wert;
		this.repaint();
	}
	
	private enum UpDownEnum {
		Up,
		Down
	}

	private int width;
	private int height;

	@Override
	public void paint(Graphics g) {
    	width = this.getWidth() / 26;
    	height = this.getHeight() / 18;
    	
	    //Linien von oben links nach unten rechts schwarz
		g.setColor(Color.BLACK);
		
	    for (int i=0;i<=3;i++)
	      g.drawLine((6 + i) * width, (8 - i) * height, (11 + 2 * i) * width, 13 * height);

	    for (int i=0;i<=2;i++)
	      g.drawLine((11 + 2 * i) * width, 5 * height, (18 + i) * width, (12 - i) * height);

	      //Linien von oben rechts nach unten links schwarz
	    for (int i=0;i<=3;i++)
	      g.drawLine((20 - i) * width, (8 - i) * height, (15 - 2 * i) * width, 13 * height);

	    for (int i=0;i<=2;i++)
	      g.drawLine((15 - 2 * i) * width, 5 * height, (8 - i) * width, (12 - i) * height);

	      //Waagrechte Linien
	    for (int i=0;i<=3;i++)
	      g.drawLine((8 - i) * width, (6 + i) * height, (18 + i) * width, (6 + i) * height);

	    for (int i=0;i<=2;i++)
	      g.drawLine((6 + i) * width, (10 + i) * height, (20 - i) * width, (10 + i) * height);

	    paintArrow(g, UpDownEnum.Up, Color.red, 13, 1);
	    paintArrow(g, UpDownEnum.Up, Color.blue, 5, 9);
	    paintArrow(g, UpDownEnum.Up, Color.green, 21, 9);
	    paintArrow(g, UpDownEnum.Down, Color.red, 13, 17);
	    paintArrow(g, UpDownEnum.Down, Color.green, 5, 9);
	    paintArrow(g, UpDownEnum.Down, Color.blue, 21, 9);
		
	}

	private void paintArrow(Graphics g, UpDownEnum ud, Color col, int x, int y) {
		g.setColor(col);

		int UpDown = 1; 
		if (ud==UpDownEnum.Down)
			UpDown=-1;
		
		for (int i=0;i<=3;i++) {
			g.drawLine((x - i) * width, (y + i * UpDown) * height, (x + 4 - 2 * i) * width, (y + 4 * UpDown) * height);
			g.drawLine((x + i) * width, (y + i * UpDown) * height, (x - 4 + 2 * i) * width, (y + 4 * UpDown) * height);
			g.drawLine((x - (i + 1)) * width, (y + (i + 1) * UpDown) * height, (x + (i + 1)) * width, (y + (i + 1) * UpDown) * height);
		}
	}
}
