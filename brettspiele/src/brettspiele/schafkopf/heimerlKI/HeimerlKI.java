package brettspiele.schafkopf.heimerlKI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import brettspiele.ZugBeendetListener;
import brettspiele.schafkopf.AbstractKI;
import brettspiele.schafkopf.Ausspielvorgang;
import brettspiele.schafkopf.Geier;
import brettspiele.schafkopf.ISpielart;
import brettspiele.schafkopf.Sauspiel;
import brettspiele.schafkopf.Spielartwahl;
import brettspiele.schafkopf.Spielen;
import brettspiele.schafkopf.Stichbestaetigung;
import brettspiele.schafkopf.Wenz;
import brettspiele.schafkopf.SchafkopfSpielsituation.Farben;
import brettspiele.schafkopf.SchafkopfSpielsituation.Spielkarten;
import brettspiele.schafkopf.SchafkopfSpielsituation.Status;

public class HeimerlKI extends AbstractKI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2389766114324885200L;

	public HeimerlKI(int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
	}

	@Override
	public void run() {
		List<Spielkarten> eigeneKarten = this.sss.getSpielerkarten();
		
		switch (this.sss.getStatus()) {
		case WARTE_AUF_SPIELEN_JA_NEIN:
			ArrayList<Spielkarten> sauspielTruempfe = Sauspiel.getTruempfeStatic(eigeneKarten);
			
			if (this.sss.getSpielerMitSpielabsicht().size()==0 && sauspielTruempfe.size()>=5) {
				beendeZug( new Spielen(true) );
				return;
			}
			else {
//				for (Farben farbe : Farben.values()) {
//					Wenz w = new Wenz(farbe);
//					if (w.getTr�mpfe(eigeneKarten).size()>=6) {
//						beendeZug( new Spielen(true) );
//						return;
//					}
//
//					Geier g = new Geier(farbe);
//					if (g.getTr�mpfe(eigeneKarten).size()>=6) {
//						beendeZug( new Spielen(true) );
//						return;
//					}
//				}
				
			}
			beendeZug( new Spielen(false) );
			
			break;
		case WARTE_AUF_SPIELARTWAHL:
			List<ISpielart> sas = this.sss.getMoeglicheSpiele();

			beendeZug( new Spielartwahl(sas.get(0)) );

//			ISpielart bestesSpiel=null; 
//			for (ISpielart sa : sas) {
//				if (sa instanceof Sauspiel) {
//					if (sa.getTr�mpfe(eigeneKarten).size()>=5)
//						bestesSpiel=sa;
//				}
//			}
//			
//			beendeZug( new Spielartwahl( bestesSpiel ) );
			break;
		case WARTE_AUF_STICHBESTAETIGUNG:
			beendeZug( new Stichbestaetigung() );
			break;
		case WARTE_AUF_AUSSPIELEN:
			ArrayList<Spielkarten> gueltigeKarten = new ArrayList<Spielkarten>(eigeneKarten.size());
			
			for (Spielkarten karte : eigeneKarten) {
				
				try {
					sss.getAktuellesSpiel().pruefeAufGueltigeKarte(this.sss, karte);
					
					gueltigeKarten.add(karte);
				}
				catch (Exception ex) {
				}
			}

			List<Spielkarten> stich = this.sss.getStich();
			
			//Bei erster Karte im Stich, einfach erstbeste Karte w�hlen
			if (stich.size()==0 || stich.size()==4) {
				beendeZug(new Ausspielvorgang(gueltigeKarten.get(0)));
			}
			else {
				//sonst h�chste Karte im Stich mit eigener h�chster vergleichen
				ISpielart spiel = this.sss.getAktuellesSpiel();
				
				Spielkarten maxStich = Collections.max(stich, spiel);
				Spielkarten maxEigene = Collections.max(gueltigeKarten, spiel);
				
				if (spiel.compare(maxStich, maxEigene)>0) {
					beendeZug(new Ausspielvorgang( Collections.min(gueltigeKarten, spiel) ));
				}
				else {
					beendeZug(new Ausspielvorgang( maxEigene ));
				}
			}
			break;
		}
	}
}
