package dame;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.HeadlessException;
//import java.awt.event.ActionEvent;
import java.awt.event.ActionEvent;

public class UeberDialog extends JDialog {
	public UeberDialog(java.awt.Window owner) throws HeadlessException {
		super(owner, "Über Dame", java.awt.Dialog.ModalityType.APPLICATION_MODAL);
		//this.setSize(400, 100);
		this.setLayout(new GridLayout(4,1,10,10));

		JLabel lbl = new JLabel("Dame", JLabel.CENTER);
		lbl.setFont(new java.awt.Font(lbl.getFont().getName(), java.awt.Font.PLAIN, 20));
		this.add(lbl);
		this.add(new JLabel("Programmiert von André Dahlmann und Christian Heimerl", JLabel.CENTER));//, java.awt.BorderLayout.NORTH);
		this.add(new JLabel("(c) 2007", JLabel.CENTER));//, java.awt.BorderLayout.NORTH);
		
		JButton btn = new JButton("Schließen");
		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UeberDialog.this.setVisible(false);
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);
		
		this.pack();
		super.setLocationRelativeTo(owner);
	}
}
