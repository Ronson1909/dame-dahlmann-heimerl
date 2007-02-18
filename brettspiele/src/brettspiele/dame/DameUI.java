package brettspiele.dame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import brettspiele.BrettspieleFenster;
import brettspiele.IBrettspielComponent;
import brettspiele.IBrettspielUI;
import brettspiele.ILokalerSpieler;
import brettspiele.ISpieler;
import brettspiele.ZugBeendetListener;

public class DameUI implements IBrettspielUI {
	public Action createFileNewAction(BrettspieleFenster bsf) {
		return new FileNewAction(bsf);
	}

	public Action createFileNewNetworkAction(BrettspieleFenster bsf) {
		return new NetworkAction(bsf);
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
			
			super.putValue(NAME, "Dame");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Dame-Spiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			IBrettspielComponent sc = new SpielbrettComponent();
			bsf.setBrettspielComponent(sc);
			
			int res = JOptionPane.showOptionDialog(bsf, "Wollen Sie gegen eine KI spielen?", "KI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			Spielablauf newSa = new Spielablauf();
			LokalerSpieler sw = new LokalerSpieler(bsf, Spielbrett.SCHWARZ, bsf);

			ISpieler we;
			if (res==JOptionPane.YES_OPTION)  
				we = new brettspiele.dame.heimerlKI.HeimerlKI(Spielbrett.WEISS, bsf);
			else {
				we = new LokalerSpieler(bsf, Spielbrett.WEISS, bsf);
				sc.addZugBeendetListener(we);
			}
				
			//Spieler gegenseitig verlinken
			sw.addZugBeendetListener(we);
			we.addZugBeendetListener(sw);
			
			//SpielbrettComponent mit den Spielern verknüpfen
			sc.setLokalerSpieler(sw);
			sc.addZugBeendetListener(sw);

			//Spiel starten
			newSa.starten(sw, we);
			bsf.setSpielablauf(newSa);
			
			bsf.updateGUI();
		}
	}
	
	private class NetworkAction extends AbstractAction {
		private BrettspieleFenster bsf;

		private NetworkAction(BrettspieleFenster bsf) {
			this.bsf=bsf;

			super.putValue(NAME, "Dame...");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein Netzwerkspiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/save.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			NetzwerkDialog frm = new NetzwerkDialog(bsf);
			frm.setVisible(true);
			
			if (frm.getSocketHandler() != null && frm.getObjectOutputStream() != null) {
				IBrettspielComponent sc = bsf.getBrettspielComponent();
				Spielablauf newSa = new Spielablauf();
				
				AbstractSpieler sw, we;
				NetzwerkSpieler ns;
				LokalerSpieler ls;
				if (frm.isServer()) {
					ls = new LokalerSpieler(bsf, Spielbrett.SCHWARZ, bsf);
					ns = new NetzwerkSpieler(Spielbrett.WEISS, bsf);
					we = ns;
					sw = ls;
				}
				else {
					ns = new NetzwerkSpieler(Spielbrett.SCHWARZ, bsf);
					ls = new LokalerSpieler(bsf, Spielbrett.WEISS, bsf);
					sw = ns;
					we = ls;
				}
				ns.setNetwork(frm.getObjectOutputStream(), frm.getSocketHandler());
				
				//Spieler gegenseitig verlinken
				sw.addZugBeendetListener(we);
				we.addZugBeendetListener(sw);
				
				//SpielbrettComponent mit den Spielern verknüpfen
				sc.setLokalerSpieler(ls);
				sc.addZugBeendetListener(ls);

				//Spiel starten
				newSa.starten(sw, we);
				bsf.setSpielablauf(newSa);
				bsf.updateGUI();
			}
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
