package dame;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.HeadlessException;
//import java.awt.event.ActionEvent;
import java.awt.event.ActionEvent;

public class NetzwerkDialog extends JDialog {
	private JTextField ip = new JTextField("127.0.0.1");
	private JTextField port = new JTextField("8900");
	
	public NetzwerkDialog(java.awt.Window owner) throws HeadlessException {
		super(owner, "Netzwerkspiel", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		//this.setSize(400, 100);
		this.setLayout(new GridLayout(4,2,0,0));

		this.add(new JLabel("IP-Adresse:"));//, java.awt.BorderLayout.NORTH);
		this.add(ip);
		
		this.add(new JLabel("Port:"));//, java.awt.BorderLayout.NORTH);
		this.add(port);

		JButton btn;
		
		btn = new JButton("Verbinden");
		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.net.Socket sock = new java.net.Socket();
				
				NetzwerkDialog.this.setVisible(false);
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);

		btn = new JButton("Auf Verbindung warten");
		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				java.net.Socket sock = new java.net.Socket();
				
				
				NetzwerkDialog.this.setVisible(false);
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);

		btn = new JButton("Schlieﬂen");
		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NetzwerkDialog.this.setVisible(false);
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);
		
		this.pack();
		super.setLocationRelativeTo(owner);
	}
}
