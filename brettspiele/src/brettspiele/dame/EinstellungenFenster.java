package brettspiele.dame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import brettspiele.BrettspieleFenster;
import brettspiele.dame.heimerlKI.HeimerlKI;

public class EinstellungenFenster extends JDialog {
	private JComboBox p1 = new JComboBox();
	private JComboBox p2 = new JComboBox();
	private JButton btnOk = new JButton("OK");
	private JButton btnCancel = new JButton("Abbrechen");
	private int result = JOptionPane.CANCEL_OPTION;
	
	public EinstellungenFenster(BrettspieleFenster bsf) {
		super(bsf, "Einstellungen", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		this.setLayout(new GridLayout(3,2,0,0));

		this.add(new JLabel("Spieler 1 (schwarz):"));
		addSpieler(bsf, p1, Spielbrett.SCHWARZ);
		this.add(p1);

		this.add(new JLabel("Spieler 2 (weiﬂ):"));
		addSpieler(bsf, p2, Spielbrett.WEISS);
		this.add(p2);
		
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result=JOptionPane.OK_OPTION;
				EinstellungenFenster.this.setVisible(false);
			}
		});
		this.add(btnOk);

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result=JOptionPane.CANCEL_OPTION;
				EinstellungenFenster.this.setVisible(false);
			}
		});
		this.add(btnCancel);

		this.pack();
		super.setLocationRelativeTo(bsf);
	}
	
	private static void addSpieler(BrettspieleFenster bsf, JComboBox cmb, int farbe) {
		cmb.addItem(new LokalerSpieler(bsf, farbe, bsf));
		cmb.addItem(new HeimerlKI(farbe, bsf));
	}

	public int getResult() {
		return result;
	}

	public AbstractSpieler getPlayer1() {
		return (AbstractSpieler)p1.getSelectedItem();
	}

	public AbstractSpieler getPlayer2() {
		return (AbstractSpieler)p2.getSelectedItem();
	}
}
