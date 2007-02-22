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
			
			int res = JOptionPane.showOptionDialog(bsf, "Wollen Sie gegen eine KI spielen?", "KI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			HalmaSpielablauf newSa = new HalmaSpielablauf();
			LokalerSpieler rt = new LokalerSpieler(bsf, HalmaSpielbrett.ROT, bsf);

			ISpieler bl;
			if (res==JOptionPane.YES_OPTION)  
				bl = new brettspiele.halma.heimerlKI.HeimerlKI(HalmaSpielbrett.BLAU, bsf);
			else {
				bl = new LokalerSpieler(bsf, HalmaSpielbrett.BLAU, bsf);
				sc.addZugBeendetListener(bl);
			}
				
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

	public String getDefaultExtension() {
		return "hsa";
	}

	public String getName() {
		return "Halma";
	}
}

class LokalerSpieler extends AbstractSpieler implements ZugBeendetListener<Zug>, ILokalerSpieler<Zug> {
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
