package brettspiele.dame.heimerlKI;

import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;

import javax.swing.JTextField;

public class TiefensucheEinstellungenFenster extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel jContentPane = null;

	private JComboBox jComboBoxBewerter = null;

	private JButton jButtonOK = null;

	private JButton jButtonCancel = null;

	private JLabel jLabelBewerter = null;

	private JButton jButtonEinstellungen = null;

	private JPanel jPanelButtons = null;
	
	private int result = JOptionPane.CANCEL_OPTION;

	private JLabel jLabelSuchtiefe = null;

	private JTextField jTextFieldSuchtiefe = null;

	/**
	 * @param owner
	 */
	public TiefensucheEinstellungenFenster(java.awt.Window owner, Tiefensuche ts) {
		super(owner);
		initialize();
		
		if (ts.getBewerter() instanceof StandardBewerter)
			jComboBoxBewerter.addItem(new BewerterComboBoxItem("Standard-Bewerter", ts.getBewerter()));
		else
			jComboBoxBewerter.addItem(new BewerterComboBoxItem("Standard-Bewerter", new StandardBewerter()));
		
		jTextFieldSuchtiefe.setText(Integer.valueOf(ts.getSearchDepth()).toString());
		
		this.setLocationRelativeTo(owner);
	}

	private class BewerterComboBoxItem {
		String text;
		IBewerter bew;
		
		public BewerterComboBoxItem(String text, IBewerter bew) {
			this.text=text;
			this.bew=bew;
		}

		@Override
		public String toString() {
			return text;
		}

		public IBewerter getBewerter() {
			return bew;
		}

		public String getText() {
			return text;
		}
	}
	
	public IBewerter getBewerter() {
		return ((BewerterComboBoxItem)jComboBoxBewerter.getSelectedItem()).getBewerter();
	}

	public int getSuchtiefe() {
		return Integer.parseInt(jTextFieldSuchtiefe.getText());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(315, 131);
		this.setResizable(false);
		this.setModal(true);
		this.setTitle("KI-Einstellungen");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			jLabelSuchtiefe = new JLabel();
			jLabelSuchtiefe.setText("Suchtiefe:");
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridwidth = 4;
			gridBagConstraints5.insets = new Insets(5, 0, 5, 0);
			gridBagConstraints5.anchor = GridBagConstraints.EAST;
			gridBagConstraints5.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 2;
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			jLabelBewerter = new JLabel();
			jLabelBewerter.setText("Bewerter:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 1;
			gridBagConstraints.ipadx = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJComboBoxBewerter(), gridBagConstraints);
			jContentPane.add(jLabelBewerter, gridBagConstraints3);
			jContentPane.add(getJButtonEinstellungen(), gridBagConstraints4);
			jContentPane.add(getJPanelButtons(), gridBagConstraints5);
			jContentPane.add(jLabelSuchtiefe, gridBagConstraints6);
			jContentPane.add(getJTextFieldSuchtiefe(), gridBagConstraints7);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jComboBoxBewerter	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxBewerter() {
		if (jComboBoxBewerter == null) {
			jComboBoxBewerter = new JComboBox();
		}
		return jComboBoxBewerter;
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
					try {
						if (Integer.parseInt(jTextFieldSuchtiefe.getText())<=0) {
							JOptionPane.showMessageDialog(jButtonOK, "Die Suchtiefe muss größer als 0 sein!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(jButtonOK, "Die Suchtiefe muss eine Zahl sein!", "Eingabefehler", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					result=JOptionPane.OK_OPTION;
					TiefensucheEinstellungenFenster.this.setVisible(false);
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
					TiefensucheEinstellungenFenster.this.setVisible(false);
				}
			});
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jButtonEinstellungen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonEinstellungen() {
		if (jButtonEinstellungen == null) {
			jButtonEinstellungen = new JButton();
			jButtonEinstellungen.setText("Einstellungen...");
			jButtonEinstellungen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getBewerter().configure(TiefensucheEinstellungenFenster.this);
				}
			});
		}
		return jButtonEinstellungen;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.add(getJButtonOK());
			jPanelButtons.add(getJButtonCancel());
		}
		return jPanelButtons;
	}

	public int getResult() {
		return result;
	}

	/**
	 * This method initializes jTextFieldSuchtiefe	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldSuchtiefe() {
		if (jTextFieldSuchtiefe == null) {
			jTextFieldSuchtiefe = new JTextField();
			jTextFieldSuchtiefe.setText("8");
		}
		return jTextFieldSuchtiefe;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
