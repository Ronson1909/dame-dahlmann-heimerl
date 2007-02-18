package brettspiele.halma;

import java.awt.event.ActionEvent;

import javax.swing.*;
import brettspiele.*;

public class HalmaUI implements IBrettspielUI {

	public Action createFileNewAction(BrettspieleFenster bsf) {
		return new FileNewAction(bsf);
	}

	private class FileNewAction extends AbstractAction {
		private BrettspieleFenster bsf;
		
		private FileNewAction(BrettspieleFenster bsf) {
			this.bsf=bsf;
			
			super.putValue(NAME, "Halma");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Halma-Spiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			IBrettspielComponent sc = new HalmaSpielbrettComponent();
			bsf.setBrettspielComponent(sc);
			
			//int res = JOptionPane.showOptionDialog(bsf, "Wollen Sie gegen eine KI spielen?", "KI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			HalmaSpielablauf newSa = new HalmaSpielablauf();
			LokalerSpieler rt = new LokalerSpieler(bsf, HalmaSpielbrett.ROT, bsf);

			ISpieler bl;
			//if (res==JOptionPane.YES_OPTION)  
				//we = new brettspiele.halma.heimerlKI.HeimerlKI(Spielbrett.WEISS, bsf);
			//else {
				bl = new LokalerSpieler(bsf, HalmaSpielbrett.BLAU, bsf);
				sc.addZugBeendetListener(bl);
			//}
				
			//Spieler gegenseitig verlinken
			rt.addZugBeendetListener(bl);
			bl.addZugBeendetListener(rt);
			
			//SpielbrettComponent mit den Spielern verknüpfen
			//sc.setLokalerSpieler(rt);
			sc.addZugBeendetListener(rt);

			//Spiel starten
			newSa.setSpieler(0,rt);
			newSa.setSpieler(1,bl);
			
			bsf.setSpielablauf(newSa);
			bsf.updateGUI();
		}
	}

	public Action createFileNewNetworkAction(BrettspieleFenster bsf) {
		return new NetworkAction(bsf);
	}

	public String getDefaultExtension() {
		return "hsa";
	}

	public String getName() {
		return "Halma";
	}

	private class NetworkAction extends AbstractAction {
		private BrettspieleFenster bsf;

		private NetworkAction(BrettspieleFenster bsf) {
			this.bsf=bsf;

			super.putValue(NAME, "Halma...");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein Netzwerkspiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/save.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
//			NetzwerkDialog frm = new NetzwerkDialog(bsf);
//			frm.setVisible(true);
//			
//			if (frm.getSocketHandler() != null && frm.getObjectOutputStream() != null) {
//				IBrettspielComponent sc = bsf.getBrettspielComponent();
//				Spielablauf newSa = new Spielablauf();
//				
//				AbstractSpieler sw, we;
//				NetzwerkSpieler ns;
//				LokalerSpieler ls;
//				if (frm.isServer()) {
//					ls = new LokalerSpieler(bsf, Spielbrett.SCHWARZ, bsf);
//					ns = new NetzwerkSpieler(Spielbrett.WEISS, bsf);
//					we = ns;
//					sw = ls;
//				}
//				else {
//					ns = new NetzwerkSpieler(Spielbrett.SCHWARZ, bsf);
//					ls = new LokalerSpieler(bsf, Spielbrett.WEISS, bsf);
//					sw = ns;
//					we = ls;
//				}
//				ns.setNetwork(frm.getObjectOutputStream(), frm.getSocketHandler());
//				
//				//Spieler gegenseitig verlinken
//				sw.addZugBeendetListener(we);
//				we.addZugBeendetListener(sw);
//				
//				//SpielbrettComponent mit den Spielern verknüpfen
//				sc.setLokalerSpieler(ls);
//				sc.addZugBeendetListener(ls);
//
//				//Spiel starten
//				newSa.starten(sw, we);
//				bsf.setSpielablauf(newSa);
//				bsf.updateGUI();
//			}
		}
	}
}

class LokalerSpieler extends AbstractSpieler implements ZugBeendetListener, ILokalerSpieler {
	private BrettspieleFenster df;
	
	public LokalerSpieler(BrettspieleFenster df, int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
		
		this.df=df;
	}
	
	@Override
	public void startGettingNaechstenZug(HalmaSpielbrett sb) {
		df.getBrettspielComponent().setLokalerSpieler(this);
	}

	public void zugBeendet(ZugBeendetEvent zbe) {
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
