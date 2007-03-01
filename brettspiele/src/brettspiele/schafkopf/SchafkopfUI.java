package brettspiele.schafkopf;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import brettspiele.BrettspieleFenster;
import brettspiele.IBrettspielUI;
import brettspiele.ILokalerSpieler;
import brettspiele.IZug;
import brettspiele.ZugBeendetEvent;
import brettspiele.ZugBeendetListener;
import brettspiele.schafkopf.heimerlKI.HeimerlKI;

public class SchafkopfUI implements IBrettspielUI {

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
			
			super.putValue(NAME, "Schafkopf");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Schafkopf-Spiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			SchafkopfSpielbrettComponent sc = new SchafkopfSpielbrettComponent();
			bsf.setBrettspielComponent(sc);
			
			//int res = JOptionPane.showOptionDialog(bsf, "Wollen Sie gegen eine KI spielen?", "KI", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			SchafkopfSpielablauf newSa = new SchafkopfSpielablauf();
			LokalerSpieler s1 = new LokalerSpieler(bsf, 0);
			HeimerlKI s2 = new HeimerlKI(1, bsf);
			HeimerlKI s3 = new HeimerlKI(2, bsf);
			HeimerlKI s4 = new HeimerlKI(3, bsf);
			
//
//			AbstractSpieler bl;
//			if (res==JOptionPane.YES_OPTION)  
//				bl = new brettspiele.halma.heimerlKI.HeimerlKI(HalmaSpielbrett.BLAU, (ZugBeendetListener) bsf);
//			else {
//				bl = new LokalerSpieler(bsf, HalmaSpielbrett.BLAU);
//				sc.addZugBeendetListener(bl);
//			}
//				
			//Spieler gegenseitig verlinken
			s1.addZugBeendetListener(s2);
			s2.addZugBeendetListener(s3);
			s3.addZugBeendetListener(s4);
			s4.addZugBeendetListener(s1);

//			//SpielbrettComponent mit den Spielern verknüpfen
//			//sc.setLokalerSpieler(rt);
			sc.addZugBeendetListener(s1);
//
//			//Spiel starten
			newSa.setSpieler(0,s1);
			newSa.setSpieler(1,s2);
			newSa.setSpieler(2,s3);
			newSa.setSpieler(3,s4);
			
			bsf.setSpielablauf(newSa);
			bsf.updateGUI();
		}
	}

	public String getDefaultExtension() {
		return "ssa";
	}

	public String getName() {
		return "Schafkopf";
	}

}

class LokalerSpieler extends AbstractSpieler implements ZugBeendetListener<IZug>, ILokalerSpieler<IZug> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8963559813713308326L;
	private BrettspieleFenster sbf;
	
	public LokalerSpieler(BrettspieleFenster sbf, int eigenePosition) {
		super(eigenePosition, (ZugBeendetListener)sbf);
		
		this.sbf=sbf;
	}
	
	@Override
	public void startGettingNaechstenZug(SchafkopfSpielsituation ss) {
		((SchafkopfSpielbrettComponent)sbf.getBrettspielComponent()).setLokalerSpieler(this);
	}

	public void zugBeendet(ZugBeendetEvent<IZug> zbe) {
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
