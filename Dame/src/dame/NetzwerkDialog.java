package dame;

import javax.swing.*;

import dame.SocketHandler.ObjectEmpfangenEvent;

import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.*;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

public class NetzwerkDialog extends JDialog {
	private JTextField ip = new JTextField("127.0.0.1");
	private JTextField port = new JTextField("8900");
	
	private Socket sock;
	private ObjectOutputStream out;
	private SocketHandler sh;
	private boolean server=false;
	
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
				try {
					sock = new Socket(ip.getText(), Integer.parseInt(port.getText()));
					out=new ObjectOutputStream(sock.getOutputStream());
					
					sh = new SocketHandler(sock);
					sh.start();

					server=false;

					NetzwerkDialog.this.setVisible(false);
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
//				} catch (ClassNotFoundException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
				}
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);

		btn = new JButton("Auf Verbindung warten");
		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerSocket ss;
				Socket sock;
				
				try {
					ss = new ServerSocket(Integer.parseInt(port.getText()));
					ss.setSoTimeout(10000);
					sock = ss.accept();
					
					out=new ObjectOutputStream(sock.getOutputStream());

					sh = new SocketHandler(sock);
					sh.start();
					
					server=true;
					NetzwerkDialog.this.setVisible(false);
				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);

		btn = new JButton("Schlieﬂen");
		btn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server=false;
				out=null;
				sh=null;

				NetzwerkDialog.this.setVisible(false);
			}
		});
		this.add(btn);//, java.awt.BorderLayout.SOUTH);
		
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
		
		this.pack();
		super.setLocationRelativeTo(owner);
	}

	public ObjectOutputStream getObjectOutputStream() {
		return out;
	}

	public SocketHandler getSocketHandler() {
		return sh;
	}

	public boolean isServer() {
		return server;
	}
}
