package brettspiele.dame.heimerlKI;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import brettspiele.dame.Spielbrett;
import brettspiele.dame.Zug;
import brettspiele.dame.ZugFolge;

public class StandardBewerter implements IBewerter {
	int wertDame = 15;
	int wertStein = 5;
	int wertLinkerRechterRand = 1;
	int wertObererUntererRand = 1;

	/**
	 * @param wertDame
	 * @param wertStein
	 * @param wertLinkerRechterRand
	 * @param wertObererUntererRand
	 */
	public StandardBewerter(int wertDame, int wertStein, int wertLinkerRechterRand, int wertObererUntererRand) {
		super();
		this.wertDame = wertDame;
		this.wertStein = wertStein;
		this.wertLinkerRechterRand = wertLinkerRechterRand;
		this.wertObererUntererRand = wertObererUntererRand;
	}

	public StandardBewerter() {
		
	}

	public int bewerteSpielbrett(Spielbrett sb) {
		int bewertung=0;
		
		for (int y=0;y<=7;y++) {
			for (int x=y%2;x<=7;x+=2) {
				int tmp = sb.getFeld(x, y);
				
				if (tmp==sb.getEigenerStein()) {
					bewertung+=wertStein;
					
					if (x==0 || x==7) {
						bewertung+=wertLinkerRechterRand;
					}

					if (y==0 || y==7) {
						bewertung+=wertObererUntererRand;
					}
				}

				if (tmp==sb.getEigeneDame()) {
					bewertung+=wertDame;
					
					if (x==0 || x==7) {
						bewertung+=wertLinkerRechterRand;
					}

					if (y==0 || y==7) {
						bewertung+=wertObererUntererRand;
					}
				}
				
				if (tmp==sb.getGegnerStein()) {
					bewertung-=wertStein;

					if (x==0 || x==7) {
						bewertung-=wertLinkerRechterRand;
					}

					if (y==0 || y==7) {
						bewertung-=wertObererUntererRand;
					}
				}

				if (tmp==sb.getGegnerDame()) {
					bewertung-=wertDame;
					
					if (x==0 || x==7) {
						bewertung-=wertLinkerRechterRand;
					}

					if (y==0 || y==7) {
						bewertung-=wertObererUntererRand;
					}
				}
			}
		}
		
		return bewertung;
	}

	public int bewerteZugfolge(ZugFolge zf, Spielbrett sb) {
		int bewertung=0;
		
		for (Zug zug : zf) {
			switch (zug.getUebersprungenerSteinTyp()) {
			case Spielbrett.SCHWARZ_D:
			case Spielbrett.WEISS_D:
				bewertung += wertDame; 
			case Spielbrett.SCHWARZ:
			case Spielbrett.WEISS:
				bewertung += wertStein; 
			default:
			}
		}
		bewertung += (zf.get(zf.size()-1).getZurDameGeworden() ? wertDame : 0);
		
		return bewertung;
	}

	public int getWertDame() {
		return wertDame;
	}

	public void setWertDame(int wertDame) {
		this.wertDame = wertDame;
	}

	public int getWertLinkerRechterRand() {
		return wertLinkerRechterRand;
	}

	public void setWertLinkerRechterRand(int wertLinkerRechterRand) {
		this.wertLinkerRechterRand = wertLinkerRechterRand;
	}

	public int getWertObererUntererRand() {
		return wertObererUntererRand;
	}

	public void setWertObererUntererRand(int wertObererUntererRand) {
		this.wertObererUntererRand = wertObererUntererRand;
	}

	public int getWertStein() {
		return wertStein;
	}

	public void setWertStein(int wertStein) {
		this.wertStein = wertStein;
	}

	public void configure(Window owner) {
		final JDialog dialog = new JDialog(owner, "Passwort");
		
		JPasswordField pwdFld = new JPasswordField();
		pwdFld.setPreferredSize(new java.awt.Dimension(80,20));
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		
		final JOptionPane optionPane = new JOptionPane(
                "Bitte geben Sie das Konfigurationspasswort ein!",
                JOptionPane.QUESTION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                new Object[] {pwdFld, btnOK},
                btnOK);

		dialog.setContentPane(optionPane);
		
		dialog.pack();
		dialog.setLocationRelativeTo(owner);
		dialog.setModal(true);
		dialog.setVisible(true);
		
		String pwd = pwdFld.getText();
		//String pwd = JOptionPane.showInputDialog(owner, "Geben Sie das Konfigurationspasswort ein:");
		if (!pwd.equals("ch1909"))
			return;
		
		StandardbewerterEinstellungenFenster sbef = new StandardbewerterEinstellungenFenster(owner, this);
		sbef.setVisible(true);
		
		if (sbef.getResult()==JOptionPane.OK_OPTION)
			sbef.updateStandardbewerter(this);		
	}

}
