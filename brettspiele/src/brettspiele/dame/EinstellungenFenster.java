package brettspiele.dame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Dimension;
import javax.swing.JButton;

import brettspiele.BrettspieleFenster;
import brettspiele.ZugBeendetListener;
import brettspiele.dame.heimerlKI.HeimerlKI;

import java.awt.Insets;

public class EinstellungenFenster extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3681194095381414650L;

	private JPanel jContentPane = null;  //  @jve:decl-index=0:visual-constraint="10,10"

	private JLabel jLabelP1 = null;

	private JComboBox jComboBoxPlayer1 = null;

	private JLabel jLabelP2 = null;

	private JComboBox jComboBoxPlayer2 = null;

	private JButton jButtonCancel = null;

	private JButton jButtonOK = null;

	private JPanel jPanelButtons = null;

	private int result = JOptionPane.CANCEL_OPTION;
	private BrettspieleFenster bsf;

	private JButton jButtonPlayer1Settings = null;

	private JButton jButtonPlayer2Settings = null;

	/**
	 * @param owner
	 */
	public EinstellungenFenster(BrettspieleFenster bsf) {
		this(bsf, null, null);
	}

	/**
	 * @param owner
	 */
	public EinstellungenFenster(BrettspieleFenster bsf, AbstractSpieler p1, AbstractSpieler p2) {
		super(bsf);
		this.bsf=bsf;
		initialize();
		
		addSpieler(bsf, jComboBoxPlayer1, Spielbrett.SCHWARZ, p1);
		addSpieler(bsf, jComboBoxPlayer2, Spielbrett.WEISS, p2);
		
		//this.pack();
		super.setLocationRelativeTo(bsf);
	}

	private class SpielerComboBoxItem {
		String text;
		AbstractSpieler sp;
		
		public SpielerComboBoxItem(String text, AbstractSpieler sp) {
			this.text=text;
			this.sp=sp;
		}

		@Override
		public String toString() {
			return text;
		}

		public AbstractSpieler getSpieler() {
			return sp;
		}

		public String getText() {
			return text;
		}
	}
	
	private void addSpieler(BrettspieleFenster bsf, JComboBox cmb, int farbe, AbstractSpieler p) {
		if (p instanceof LokalerSpieler)
			cmb.addItem(new SpielerComboBoxItem("Lokaler Spieler", p));
		else
			cmb.addItem(new SpielerComboBoxItem("Lokaler Spieler", new LokalerSpieler(bsf, farbe)));
		
		if (p instanceof HeimerlKI)
			cmb.addItem(new SpielerComboBoxItem("Heimerls Killer KI", p));
		else
			cmb.addItem(new SpielerComboBoxItem("Heimerls Killer KI", new HeimerlKI(farbe, (ZugBeendetListener)bsf)));
		
		if (p instanceof NetzwerkSpieler)
			cmb.addItem(new SpielerComboBoxItem("Netzwerkspieler", p));
		else
			cmb.addItem(new SpielerComboBoxItem("Netzwerkspieler", new NetzwerkSpieler(farbe, (ZugBeendetListener)bsf)));
	}

	public int getResult() {
		return result;
	}

	public AbstractSpieler getPlayer1() {
		return ((SpielerComboBoxItem)jComboBoxPlayer1.getSelectedItem()).getSpieler();
	}

	public AbstractSpieler getPlayer2() {
		return ((SpielerComboBoxItem)jComboBoxPlayer2.getSelectedItem()).getSpieler();
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(373, 123);
		this.setResizable(false);
		this.setPreferredSize(new Dimension(373, 123));
		this.setModal(true);
		this.setTitle("Spieleinstellungen");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 2;
			gridBagConstraints21.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 2;
			gridBagConstraints11.gridy = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridwidth = 3;
			gridBagConstraints6.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints6.anchor = GridBagConstraints.EAST;
			gridBagConstraints6.gridy = 4;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 2;
			jLabelP2 = new JLabel();
			jLabelP2.setText("Spieler 2 (weiﬂ):");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.fill = GridBagConstraints.NONE;
			gridBagConstraints.gridy = 1;
			jLabelP1 = new JLabel();
			jLabelP1.setText("Spieler 1 (schwarz):");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setSize(new Dimension(251, 102));
			jContentPane.add(jLabelP1, gridBagConstraints);
			jContentPane.add(getJComboBoxPlayer1(), gridBagConstraints1);
			jContentPane.add(jLabelP2, gridBagConstraints2);
			jContentPane.add(getJComboBoxPlayer2(), gridBagConstraints3);
			jContentPane.add(getJPanelButtons(), gridBagConstraints6);
			jContentPane.add(getJButtonPlayer1Settings(), gridBagConstraints11);
			jContentPane.add(getJButtonPlayer2Settings(), gridBagConstraints21);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jComboBoxPlayer1	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxPlayer1() {
		if (jComboBoxPlayer1 == null) {
			jComboBoxPlayer1 = new JComboBox();
			jComboBoxPlayer1.setPreferredSize(new Dimension(80, 22));
			jComboBoxPlayer1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jButtonPlayer1Settings.setEnabled( getPlayer1().isConfigurable() );
				}
			});
		}
		return jComboBoxPlayer1;
	}

	/**
	 * This method initializes jComboBoxPlayer2	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxPlayer2() {
		if (jComboBoxPlayer2 == null) {
			jComboBoxPlayer2 = new JComboBox();
			jComboBoxPlayer2.setPreferredSize(new Dimension(80, 22));
			jComboBoxPlayer2.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jButtonPlayer2Settings.setEnabled( getPlayer2().isConfigurable() );
				}
			});
		}
		return jComboBoxPlayer2;
	}

	/**
	 * This method initializes jButtonOK	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonOK() {
		if (jButtonOK == null) {
			jButtonOK = new JButton();
			jButtonOK.setText("OK");
			jButtonOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					result=JOptionPane.OK_OPTION;
					EinstellungenFenster.this.setVisible(false);
				}
			});
		}
		return jButtonOK;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Abbrechen");
			jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					result=JOptionPane.CANCEL_OPTION;
					EinstellungenFenster.this.setVisible(false);
				}
			});
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = -1;
			gridBagConstraints4.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints4.gridy = -1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = -1;
			gridBagConstraints5.ipadx = 0;
			gridBagConstraints5.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints5.gridy = -1;
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonOK(), gridBagConstraints5);
			jPanelButtons.add(getJButtonCancel(), gridBagConstraints4);
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jButtonPlayer1Settings	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonPlayer1Settings() {
		if (jButtonPlayer1Settings == null) {
			jButtonPlayer1Settings = new JButton();
			jButtonPlayer1Settings.setText("Einstellungen...");
			jButtonPlayer1Settings.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getPlayer1().configure(EinstellungenFenster.this, 1);
				}
			});
		}
		return jButtonPlayer1Settings;
	}

	/**
	 * This method initializes jButtonPlayer2Settings	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonPlayer2Settings() {
		if (jButtonPlayer2Settings == null) {
			jButtonPlayer2Settings = new JButton();
			jButtonPlayer2Settings.setText("Einstellungen...");
			jButtonPlayer2Settings.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getPlayer2().configure(EinstellungenFenster.this, 2);
				}
			});
		}
		return jButtonPlayer2Settings;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
