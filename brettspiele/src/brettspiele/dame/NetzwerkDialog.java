package brettspiele.dame;

import javax.swing.*;

import brettspiele.SocketHandler;
import brettspiele.SocketHandler.ObjectEmpfangenEvent;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Insets;

public class NetzwerkDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -747439235176665445L;
	
	private JTextField jTextFieldIP = null;
	private JTextField jTextFieldPort = null;
	
	private Socket sock;
	private ObjectOutputStream out;  //  @jve:decl-index=0:
	private SocketHandler sh;
	
	private JPanel jContentPane = null;
	private JLabel jLabelIP = null;
	private JLabel jLabelPort = null;
	private JPanel jPanelButtons = null;
	private JButton jButtonConnect = null;
	private JButton jButtonWaitForConnection = null;
	private JButton jButtonClose = null;
	
	public NetzwerkDialog(java.awt.Window owner, ObjectOutputStream out, SocketHandler sh, boolean isServer) throws HeadlessException {
		super(owner);
		
		this.sh=sh;
		this.out=out;

		initialize();

		jButtonConnect.setEnabled(!isServer);
		jTextFieldIP.setEnabled(!isServer);
		
		jButtonWaitForConnection.setEnabled(isServer);
		
//		btn = new JButton("Senden");
//		btn.addActionListener(new java.awt.event.ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				//Serialize to Net
//				if (NetzwerkDialog.this.out == null)
//					return;
//				
//				try {
//					NetzwerkDialog.this.out.writeObject(new Zug());
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});
//		this.add(btn);
		
		super.setLocationRelativeTo(owner);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
        this.setSize(new Dimension(324, 104));
        this.setResizable(false);
        this.setContentPane(getJContentPane());
        this.setModal(true);
        this.setTitle("Netzwerkspiel");
			
	}

	public ObjectOutputStream getObjectOutputStream() {
		return out;
	}

	public void setObjectOutputStream(ObjectOutputStream out) {
		this.out = out;
	}

	public SocketHandler getSocketHandler() {
		return sh;
	}

	public void setSocketHandler(SocketHandler sh) {
		this.sh = sh;
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridwidth = 2;
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints3.gridy = 1;
			jLabelPort = new JLabel();
			jLabelPort.setText("Port:");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(0, 5, 0, 5);
			gridBagConstraints2.gridy = 0;
			jLabelIP = new JLabel();
			jLabelIP.setText("IP-Adresse:");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridx = 1;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.gridx = 1;

			jTextFieldIP = new JTextField();
	        jTextFieldIP.setText("127.0.0.1");
	        
	        jTextFieldPort = new JTextField();
	        jTextFieldPort.setText("8900");

	        jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jTextFieldIP, gridBagConstraints);
			jContentPane.add(jTextFieldPort, gridBagConstraints1);
			jContentPane.add(jLabelIP, gridBagConstraints2);
			jContentPane.add(jLabelPort, gridBagConstraints3);
			jContentPane.add(getJPanelButtons(), gridBagConstraints4);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonConnect(), gridBagConstraints5);
			jPanelButtons.add(getJButtonWaitForConnection(), new GridBagConstraints());
			jPanelButtons.add(getJButtonClose(), new GridBagConstraints());
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jButtonConnect	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonConnect() {
		if (jButtonConnect == null) {
			jButtonConnect = new JButton();
			jButtonConnect.setText("Verbinden");
			jButtonConnect.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						sock = new Socket(jTextFieldIP.getText(), Integer.parseInt(jTextFieldPort.getText()));
						out=new ObjectOutputStream(sock.getOutputStream());
						
						sh = new SocketHandler(sock);
						sh.start();

						NetzwerkDialog.this.setVisible(false);
					} catch (NumberFormatException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(NetzwerkDialog.this, "Der Port muss eine Zahl sein!", "Fehlerhafte Portnummer", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(NetzwerkDialog.this, "Die IP-Adresse bzw. der Hostname ist unbekannt!", "Unbekannte Adresse", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(NetzwerkDialog.this, "Es trat ein Ein-/Ausgabefehler auf!", "Fehler", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
		}
		return jButtonConnect;
	}

	/**
	 * This method initializes jButtonWaitForConnection	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonWaitForConnection() {
		if (jButtonWaitForConnection == null) {
			jButtonWaitForConnection = new JButton();
			jButtonWaitForConnection.setText("Auf Verbindung warten");
			jButtonWaitForConnection.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ServerSocket ss;
					Socket sock;
					
					try {
						ss = new ServerSocket(Integer.parseInt(jTextFieldPort.getText()));
						ss.setSoTimeout(10000);
						sock = ss.accept();
						
						out=new ObjectOutputStream(sock.getOutputStream());

						sh = new SocketHandler(sock);
						sh.start();
						
						NetzwerkDialog.this.setVisible(false);
					} catch (SocketException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(NetzwerkDialog.this, "Es trat ein Verbindungsfehler auf!", "Verbindungsfehler", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(NetzwerkDialog.this, "Es trat ein Ein-/Ausgabefehler auf! Vermutlich hat sich kein anderer Spieler mit Ihnen verbunden.", "Fehler", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			});
		}
		return jButtonWaitForConnection;
	}

	/**
	 * This method initializes jButtonClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonClose() {
		if (jButtonClose == null) {
			jButtonClose = new JButton();
			jButtonClose.setText("Schlieﬂen");
			jButtonClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
//					out=null;
//					sh=null;

					NetzwerkDialog.this.setVisible(false);
				}
			});
		}
		return jButtonClose;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
