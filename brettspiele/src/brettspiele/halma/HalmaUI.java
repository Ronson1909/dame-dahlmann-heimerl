package brettspiele.halma;

import java.awt.event.ActionEvent;

import javax.swing.*;
import brettspiele.*;

public class HalmaUI implements IBrettspielUI {

	public Action createFileNewAction(BrettspieleFenster bsf) {
		return new FileNewAction(bsf);
	}

	private class FileNewAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -6104494243360296644L;
		private BrettspieleFenster bsf;
		
		private FileNewAction(BrettspieleFenster bsf) {
			this.bsf=bsf;
			
			super.putValue(NAME, "Halma");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Halma-Spiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			HalmaSpielbrettComponent sc = new HalmaSpielbrettComponent();
			bsf.setBrettspielComponent(sc);
			
			int res = JOptionPane.showOptionDialog(bsf, "Wollen Sie gegen eine KI spielen?", "KI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			HalmaSpielablauf newSa = new HalmaSpielablauf();
			LokalerSpieler rt = new LokalerSpieler(bsf, HalmaSpielbrett.ROT);

			AbstractSpieler bl;
			if (res==JOptionPane.YES_OPTION)  
				bl = new brettspiele.halma.heimerlKI.HeimerlKI(HalmaSpielbrett.BLAU, (ZugBeendetListener) bsf);
			else {
				bl = new LokalerSpieler(bsf, HalmaSpielbrett.BLAU);
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
	/**
	 * 
	 */
	private static final long serialVersionUID = 2889980403928999051L;
	private BrettspieleFenster sbf;
	
	public LokalerSpieler(BrettspieleFenster sbf, int eigeneFarbe) {
		super(eigeneFarbe, (ZugBeendetListener)sbf);
		
		this.sbf=sbf;
	}
	
	@Override
	public void startGettingNaechstenZug(HalmaSpielbrett sb) {
		((HalmaSpielbrettComponent)sbf.getBrettspielComponent()).setLokalerSpieler(this);
	}

	public void zugBeendet(ZugBeendetEvent<Zug> zbe) {
		if (zbe.getSpieler()==this) {
			beendeZug(zbe.getZug());
		}
	}

	public BrettspieleFenster getBrettspieleFenster() {
		return sbf;
	}

	public void setBrettspieleFenster(BrettspieleFenster sbf) {
		this.sbf = sbf;
	}
}
