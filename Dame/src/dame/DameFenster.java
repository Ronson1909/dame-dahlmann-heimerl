package dame;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class DameFenster extends JFrame {
	public static void main(String[] args) {
		DameFenster main = new DameFenster();
		//main.setSpielbrett(new Spielbrett());
		main.setSize(200, 200);
		main.setVisible(true);
    }

	public DameFenster() throws HeadlessException {
		// TODO Auto-generated constructor stub
	}

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

	
	private Spielbrett sb = new Spielbrett();
	
	public Spielbrett getSpielbrett() {
		return sb;
	}

	public void setSpielbrett(Spielbrett wert) {
		sb=wert;
		this.invalidate();
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());

		if (sb == null)
			return;
		
		final float SteinPercentage = 75;
		final int leftBorder = 5; 
		final int rightBorder = 5; 
		final int topBorder = 25; 
		final int bottomBorder = 5; 
		final int Feldbreite = Math.min(this.getHeight()-topBorder-bottomBorder, this.getWidth()-leftBorder-rightBorder) / 8; 
		final int ZweiterKreisOffset = (int)(0.1 * Feldbreite);
		
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				if ((x+y) % 2 ==0)
					g.setColor(java.awt.Color.white);
				else
					g.setColor(java.awt.Color.black);
					
				g.fillRect(leftBorder + Feldbreite*x, topBorder + y*Feldbreite, Feldbreite, Feldbreite);

				int kreisX=(int)(leftBorder + Feldbreite*x + Feldbreite * (100-SteinPercentage) / 200);
				int kreisY=(int)(topBorder + Feldbreite*y + Feldbreite * (100-SteinPercentage) / 200);
				int kreisD=(int)(Feldbreite * SteinPercentage / 100);

				switch (sb.gibFeld(x,7-y)) {
				case Spielbrett.SCHWARZ:
				case Spielbrett.SCHWARZ_D:
					g.setColor(java.awt.Color.black);
					g.fillOval(kreisX, kreisY, kreisD, kreisD);
					g.setColor(java.awt.Color.white);
					g.drawOval(kreisX, kreisY, kreisD, kreisD);

					g.setColor(java.awt.Color.black);
					g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
					g.setColor(java.awt.Color.white);
					g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
					break;
				case Spielbrett.WEISS:
				case Spielbrett.WEISS_D:
					g.setColor(java.awt.Color.white);
					g.fillOval(kreisX, kreisY, kreisD, kreisD);
					g.setColor(java.awt.Color.black);
					g.drawOval(kreisX, kreisY, kreisD, kreisD);

					g.setColor(java.awt.Color.white);
					g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
					g.setColor(java.awt.Color.black);
					g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
					break;
				}

				kreisX-=ZweiterKreisOffset;
				kreisY-=ZweiterKreisOffset;
				
				switch (sb.gibFeld(x,7-y)) {
				case Spielbrett.SCHWARZ_D:
					g.setColor(java.awt.Color.black);
					g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
					g.setColor(java.awt.Color.white);
					g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);

					break;
				case Spielbrett.WEISS_D:
					g.setColor(java.awt.Color.white);
					g.fillOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);
					g.setColor(java.awt.Color.black);
					g.drawOval(kreisX-ZweiterKreisOffset, kreisY-ZweiterKreisOffset, kreisD, kreisD);

					break;
				}
				
			}
		}
	
		//super.paint(g);
	}
}
