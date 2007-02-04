package dame;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

public class DameFenster extends JFrame {
	public static void main(String[] args) {
		DameFenster main = new DameFenster();
		main.setSpielbrett(new Spielbrett());
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

	
	private Spielbrett sb;
	
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
		final float SteinPercentage = 100;
		
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		int Feldbreite = Math.min(this.getHeight(), this.getWidth()) / 8; 
		
		for (int x=0;x<8;x++) {
			for (int y=0;y<8;y++) {
				if ((x+y) % 2 ==0)
					g.setColor(java.awt.Color.black);
				else
					g.setColor(java.awt.Color.white);
					
				g.fillRect(Feldbreite*x, y*Feldbreite, Feldbreite, Feldbreite);

				int kreisX=(int)(Feldbreite*x + Feldbreite * SteinPercentage / 200);
				int kreisY=(int)(Feldbreite*y + Feldbreite * SteinPercentage / 200);
				int kreisD=(int)(Feldbreite * SteinPercentage / 100);

				switch (sb.gibFeld) {
				case Spielbrett.SCHWARZ:
					g.setColor(java.awt.Color.white);
					g.drawOval(kreisX, kreisY, kreisD, kreisD);
					g.setColor(java.awt.Color.black);
					g.fillOval(kreisX, kreisY, kreisD, kreisD);
					break;
				case Spielbrett.WEISS:
					g.setColor(java.awt.Color.black);
					g.drawOval(kreisX, kreisY, kreisD, kreisD);
					g.setColor(java.awt.Color.white);
					g.fillOval(kreisX, kreisY, kreisD, kreisD);
					break;
				}
				
			}
		}
	
		//super.paint(g);
	}
}
