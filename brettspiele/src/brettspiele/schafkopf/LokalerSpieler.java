package brettspiele.schafkopf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import brettspiele.BrettspieleFenster;
import brettspiele.ILokalerSpieler;
import brettspiele.IZug;
import brettspiele.ZugBeendetEvent;
import brettspiele.ZugBeendetListener;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;

class LokalerSpieler extends AbstractSpieler implements ZugBeendetListener<IZug>, ILokalerSpieler<IZug> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8963559813713308326L;
	private BrettspieleFenster sbf;
	private SpielartwahlDialog dlg=null;
	
	public LokalerSpieler(BrettspieleFenster sbf, int eigenePosition) {
		super(eigenePosition, (ZugBeendetListener)sbf);
		
		this.sbf=sbf;

		dlg = new SpielartwahlDialog(sbf);
	}
	
	@Override
	public void startGettingNaechstenZug(final SchafkopfSpielsituation ss) {
		switch (ss.getStatus()) {
		case WARTE_AUF_SPIELEN_JA_NEIN: {
			List<ISpielart> moeglSpiele = ss.getMoeglicheSpiele();

			dlg.init(moeglSpiele.toArray(new ISpielart[0]), false);
			dlg.getJListSpielarten().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						JList lst = (JList)e.getSource();
						ss.sortSpielerkarten((ISpielart)lst.getSelectedValue());
						sbf.getBrettspielComponent().repaint();
					}
				}
			});
			if (moeglSpiele.size()>0)
				dlg.getJListSpielarten().setSelectedIndex(0);
			
			dlg.setLocationRelativeTo(sbf);
			dlg.setVisible(true);
			
			
			ISpielart res = dlg.getGewaehlteSpielart(); 
			if (res!=null) {
				beendeZug( new Spielen(true) );
			}
			else {
				beendeZug( new Spielen(false) );
			}
			
			
//			if (moeglSpiele.size()>0) {
//				int res = JOptionPane.showConfirmDialog(sbf, "Wollen Sie spielen?", "Spielen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//				beendeZug( new Spielen(res==JOptionPane.YES_OPTION) );
//			}

			break;
		}
		case WARTE_AUF_SPIELARTWAHL: {
			List<ISpielart> moeglSpiele = ss.getMoeglicheSpiele();

			boolean forceSpiel = (ss.getAktuellesSpiel() == null || ss.getAktuellesSpiel() instanceof Sauspiel);
			
			ISpielart akt = dlg.getGewaehlteSpielart();
			for (ISpielart sp : moeglSpiele) {
				if (akt.equals(sp)) {
					akt = sp;
					break;
				}
			}
			
			dlg.init(moeglSpiele.toArray(new ISpielart[0]), forceSpiel);
			dlg.getJListSpielarten().addListSelectionListener(new ListSelectionListener() {
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						JList lst = (JList)e.getSource();
						ss.sortSpielerkarten((ISpielart)lst.getSelectedValue());
						sbf.getBrettspielComponent().repaint();
					}
				}
			});
			dlg.getJListSpielarten().setSelectedIndex( moeglSpiele.indexOf(akt) );

			dlg.setLocationRelativeTo(sbf);
			dlg.setVisible(true);
			
			ISpielart res = dlg.getGewaehlteSpielart(); 
			if (res!=null) {
				beendeZug( new Spielartwahl( res ) );
			}
			else {
				beendeZug( new Spielartwahl( res ) );
			}
		}
		default:
			if (ss.getSpielerkarten().size()==8) {
				ss.sortSpielerkarten(ss.getAktuellesSpiel());
			}
		
			((SchafkopfSpielbrettComponent)sbf.getBrettspielComponent()).setLokalerSpieler(this);
		}
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
