package brettspiele.dame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import brettspiele.BrettspieleFenster;
import brettspiele.IBrettspielUI;
import brettspiele.ILokalerSpieler;
import brettspiele.ZugBeendetListener;

public class DameUI implements IBrettspielUI {
	public Action createFileNewAction(BrettspieleFenster bsf) {
		return new FileNewAction(bsf);
	}

	public String getName() {
		return "Dame";
	}

	public String getDefaultExtension() {
		return "dsa";
	}
	
	private class FileNewAction extends AbstractAction {
		private BrettspieleFenster bsf;
		
		private FileNewAction(BrettspieleFenster bsf) {
			this.bsf=bsf;
			
			super.putValue(NAME, "Dame...");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Dame-Spiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			//int res = JOptionPane.showOptionDialog(bsf, "Wollen Sie gegen eine KI spielen?", "KI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

			EinstellungenFenster ef = new EinstellungenFenster(bsf);
			ef.setVisible(true);
			
			if (ef.getResult()!=JOptionPane.OK_OPTION) {
				return;
			}

			SpielbrettComponent sc = new SpielbrettComponent();
			bsf.setBrettspielComponent(sc);

			Spielablauf newSa = new Spielablauf();
			AbstractSpieler sw = ef.getPlayer1(); //new LokalerSpieler(bsf, Spielbrett.SCHWARZ, bsf);
			if (sw instanceof LokalerSpieler) {
				sc.addZugBeendetListener(sw);
				sc.setLokalerSpieler(sw);
			}
			
			AbstractSpieler we = ef.getPlayer2();
			if (we instanceof LokalerSpieler) {
				sc.addZugBeendetListener(we);
				sc.setLokalerSpieler(we);
			}

			//Spieler gegenseitig verlinken
			sw.addZugBeendetListener(we);
			we.addZugBeendetListener(sw);
			
			//SpielbrettComponent mit den Spielern verkn�pfen
			//sc.setLokalerSpieler(sw);
			//sc.addZugBeendetListener(sw);

			//Spiel starten
			newSa.starten(sw, we);
			bsf.setSpielablauf(newSa);
			
			//bsf.updateGUI();
		}
	}
}

class LokalerSpieler extends AbstractSpieler implements ZugBeendetListener<ZugFolge>, ILokalerSpieler<ZugFolge> {
	private BrettspieleFenster df;
	
	public LokalerSpieler(BrettspieleFenster df, int eigeneFarbe, ZugBeendetListener<ZugFolge> zbl) {
		super(eigeneFarbe, zbl);
		
		this.df=df;
	}
	
	@Override
	public void startGettingNaechstenZug(Spielbrett sb) {
		df.getBrettspielComponent().setLokalerSpieler(this);
	}

	public void zugBeendet(brettspiele.ZugBeendetEvent<ZugFolge> zbe) {
		if (zbe.getSpieler()==this) {
			beendeZug(zbe.getZug());
		}
	}

	public BrettspieleFenster getBrettspieleFenster() {
		return df;
	}

	public void setBrettspieleFenster(BrettspieleFenster df) {
		this.df = df;
	}
}
