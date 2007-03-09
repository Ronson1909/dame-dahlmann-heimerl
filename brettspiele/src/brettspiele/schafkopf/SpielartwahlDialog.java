package brettspiele.schafkopf;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JButton;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class SpielartwahlDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1571967848664310307L;
	private JPanel jContentPane = null;
	private JList jListSpielarten = null;
	private JPanel jPanelButtons = null;
	private JButton jButtonSpielen = null;
	private JButton jButtonWeiter = null;
	private JLabel jLabelBeschreibung = null;
	
	private ISpielart res;
	private JScrollPane jScrollPaneSpielarten = null;
	
	/**
	 * This method initializes 
	 * 
	 */
	public SpielartwahlDialog(Window owner) {
		super(owner);
		initialize();
	}

	public void init(ISpielart[] verfuegbareSpiele, boolean erzwingeSpielen) {
		jListSpielarten.setListData(verfuegbareSpiele);
		jButtonWeiter.setEnabled(!erzwingeSpielen);
		res=null;
	}
	
	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(246, 276));
        this.setMinimumSize(new Dimension(256, 276));
        this.setModal(true);
        this.setTitle("Spielart wählen");
        this.setContentPane(getJContentPane());
        this.addWindowListener(new java.awt.event.WindowAdapter() {
        	public void windowClosing(java.awt.event.WindowEvent e) {
        		if (!jButtonWeiter.isEnabled()) {
        			SpielartwahlDialog.this.setVisible(true);
        		}
        	}
        });
			
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelBeschreibung = new JLabel();
			jLabelBeschreibung.setText("<html>Wählen Sie das Spiel aus, das Sie spielen wollen oder klicken Sie auf Weiter, um nicht zu spielen:</html>");
			jLabelBeschreibung.setPreferredSize(new Dimension(459, 28));
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(jLabelBeschreibung, BorderLayout.NORTH);
			jContentPane.add(getJScrollPaneSpielarten(), BorderLayout.CENTER);
			jContentPane.add(getJPanelButtons(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jListSpielarten	
	 * 	
	 * @return javax.swing.JList	
	 */
	public JList getJListSpielarten() {
		if (jListSpielarten == null) {
			jListSpielarten = new JList();
			jListSpielarten.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return jListSpielarten;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonSpielen(), new GridBagConstraints());
			jPanelButtons.add(getJButtonWeiter(), new GridBagConstraints());
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jButtonSpielen	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSpielen() {
		if (jButtonSpielen == null) {
			jButtonSpielen = new JButton();
			jButtonSpielen.setText("Spielen");
			jButtonSpielen.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (jListSpielarten.getSelectedValue()==null) {
						JOptionPane.showMessageDialog(SpielartwahlDialog.this, "Sie müssen ein Spiel auswählen!", "Fehler", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					SpielartwahlDialog.this.res = (ISpielart) jListSpielarten.getSelectedValue();
					SpielartwahlDialog.this.setVisible(false);
				}
			});
		}
		return jButtonSpielen;
	}

	/**
	 * This method initializes jButtonWeiter	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonWeiter() {
		if (jButtonWeiter == null) {
			jButtonWeiter = new JButton();
			jButtonWeiter.setText("Weiter");
			jButtonWeiter.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					SpielartwahlDialog.this.res = null;
					SpielartwahlDialog.this.setVisible(false);
				}
			});
		}
		return jButtonWeiter;
	}

	/**
	 * @return the res
	 */
	public ISpielart getGewaehlteSpielart() {
		return res;
	}

	/**
	 * This method initializes jScrollPaneSpielarten	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneSpielarten() {
		if (jScrollPaneSpielarten == null) {
			jScrollPaneSpielarten = new JScrollPane();
			jScrollPaneSpielarten.setViewportView(getJListSpielarten());
		}
		return jScrollPaneSpielarten;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
