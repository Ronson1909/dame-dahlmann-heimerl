package brettspiele.dame.heimerlKI;

import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Insets;

public class StandardbewerterEinstellungenFenster extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JLabel jLabelWertStein = null;

	private JLabel jLabelWertDame = null;

	private JTextField jTextFieldWertStein = null;

	private JTextField jTextFieldWertDame = null;

	private JLabel jLabelWertLinkerRechterRand = null;

	private JLabel jLabelWertObererUntererRand = null;

	private JTextField jTextFieldWertLinkerRechterRand = null;

	private JTextField jTextFieldWertObererUntererRand = null;

	private JPanel jPanelButtons = null;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;

	/**
	 * @param owner
	 */
	public StandardbewerterEinstellungenFenster(java.awt.Window owner, StandardBewerter sb) {
		super(owner);
		initialize();
		
		jTextFieldWertDame.setText(Integer.valueOf(sb.getWertDame()).toString());
		jTextFieldWertStein.setText(Integer.valueOf(sb.getWertStein()).toString());
		jTextFieldWertLinkerRechterRand.setText(Integer.valueOf(sb.getWertLinkerRechterRand()).toString());
		jTextFieldWertObererUntererRand.setText(Integer.valueOf(sb.getWertObererUntererRand()).toString());

		this.setLocationRelativeTo(owner);
	}

	private int result = JOptionPane.CANCEL_OPTION;
	public int getResult() {
		return result;
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 150);
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("Standardbewerter");
		this.setContentPane(getJContentPane());
	}

	public void updateStandardbewerter(StandardBewerter sb) {
		sb.setWertDame(Integer.parseInt(jTextFieldWertDame.getText()));
		sb.setWertStein(Integer.parseInt(jTextFieldWertStein.getText()));
		sb.setWertLinkerRechterRand(Integer.parseInt(jTextFieldWertLinkerRechterRand.getText()));
		sb.setWertObererUntererRand(Integer.parseInt(jTextFieldWertObererUntererRand.getText()));
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints8.gridy = 4;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 3;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridy = 2;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridy = 3;
			jLabelWertObererUntererRand = new JLabel();
			jLabelWertObererUntererRand.setText("Wert oberer/unterer Rand:");
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 2;
			jLabelWertLinkerRechterRand = new JLabel();
			jLabelWertLinkerRechterRand.setText("Wert linker/rechter Rand:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			jLabelWertDame = new JLabel();
			jLabelWertDame.setText("Wert Dame:");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridy = 0;
			jLabelWertStein = new JLabel();
			jLabelWertStein.setText("Wert Stein:");
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelWertStein, gridBagConstraints);
			jContentPane.add(jLabelWertDame, gridBagConstraints1);
			jContentPane.add(getJTextFieldWertStein(), gridBagConstraints2);
			jContentPane.add(getJTextFieldWertDame(), gridBagConstraints3);
			jContentPane.add(jLabelWertLinkerRechterRand, gridBagConstraints4);
			jContentPane.add(jLabelWertObererUntererRand, gridBagConstraints5);
			jContentPane.add(getJTextFieldWertLinkerRechterRand(), gridBagConstraints6);
			jContentPane.add(getJTextFieldWertObererUntererRand(), gridBagConstraints7);
			jContentPane.add(getJPanelButtons(), gridBagConstraints8);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextFieldWertStein	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldWertStein() {
		if (jTextFieldWertStein == null) {
			jTextFieldWertStein = new JTextField();
		}
		return jTextFieldWertStein;
	}

	/**
	 * This method initializes jTextFieldWertDame	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldWertDame() {
		if (jTextFieldWertDame == null) {
			jTextFieldWertDame = new JTextField();
		}
		return jTextFieldWertDame;
	}

	/**
	 * This method initializes jTextFieldWertLinkerRechterRand	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldWertLinkerRechterRand() {
		if (jTextFieldWertLinkerRechterRand == null) {
			jTextFieldWertLinkerRechterRand = new JTextField();
		}
		return jTextFieldWertLinkerRechterRand;
	}

	/**
	 * This method initializes jTextFieldWertObererUntererRand	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldWertObererUntererRand() {
		if (jTextFieldWertObererUntererRand == null) {
			jTextFieldWertObererUntererRand = new JTextField();
		}
		return jTextFieldWertObererUntererRand;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = -1;
			gridBagConstraints9.gridy = -1;
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonOK(), gridBagConstraints9);
			jPanelButtons.add(getJButtonCancel(), new GridBagConstraints());
		}
		return jPanelButtons;
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
					result = JOptionPane.OK_OPTION;
					StandardbewerterEinstellungenFenster.this.setVisible(false);
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
					result = JOptionPane.CANCEL_OPTION;
					StandardbewerterEinstellungenFenster.this.setVisible(false);
				}
			});
		}
		return jButtonCancel;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
