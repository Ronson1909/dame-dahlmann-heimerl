package dame;

import javax.swing.*;

import dame.DameComponent.ZugBeendetEvent;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

public class DameFenster extends JFrame {
	public static void main(String[] args) {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (IllegalAccessException ex) {
        }
        catch (InstantiationException ex) {
        }
        catch (ClassNotFoundException ex) {
        }
        catch (UnsupportedLookAndFeelException e) {
		}
		
		DameFenster main = new DameFenster();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
    }

	private Spielablauf sa;
	private DameComponent dc = new DameComponent();
	private JMenuBar mnMain = new JMenuBar();
	private JMenu mnFile = new JMenu();
	private JMenu mnEdit = new JMenu();
	private JMenu mnHelp = new JMenu();
	
	private JToolBar tbMain = new JToolBar();

	private JLabel statusText;
	
	public DameFenster() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super("Dame");
		this.setSize(400, 400);
		this.add(dc);

		JPanel status = new JPanel();
		statusText = new JLabel(" ");
		status.setLayout(new java.awt.BorderLayout());
		status.add(statusText, java.awt.BorderLayout.CENTER);
		status.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
		this.add(status, java.awt.BorderLayout.SOUTH);		

		//setzeSpielablauf(new Spielablauf());
		
		dc.addZugBeendetListener(dc.new ZugBeendetAdapter() {
			public void zugBeendet(ZugBeendetEvent zbe) {
				sa.macheZug(zbe.getZugfolge());
				
				uma.setEnabled(true);
				rma.setEnabled(false);

	           	switch (sa.getSpielbrett().isSpielBeendet()) {
	           	case Spielbrett.WEISS:
	           		JOptionPane.showMessageDialog(null, "Weiß hat gewonnen!", "Spiel beendet", JOptionPane.INFORMATION_MESSAGE);
	           		return;
	           	case Spielbrett.SCHWARZ:
	               	JOptionPane.showMessageDialog(null, "Schwarz hat gewonnen!", "Spiel beendet", JOptionPane.INFORMATION_MESSAGE);
	               	return;
	           	default:
	           	}

	           	updateStatusBar();
			}
		});
		
		mnFile.setText("Datei");
		mnFile.add(fna);
		mnFile.add(foa);
		mnFile.add(fsa);
		mnFile.add(network_a);
		mnFile.add(new JSeparator());
		mnFile.add(cla);
		mnMain.add(mnFile);

		mnEdit.setText("Bearbeiten");
		mnEdit.add(uma);
		mnEdit.add(rma);
		mnMain.add(mnEdit);

		mnHelp.setText("?");
		mnHelp.add(about_a);
		mnMain.add(mnHelp);

		this.setJMenuBar(mnMain);
		
		tbMain.add(fna);
		tbMain.add(foa);
		tbMain.add(fsa);
		tbMain.add(uma);
		tbMain.add(rma);
		this.add(tbMain, java.awt.BorderLayout.NORTH);
		
	}
	
	private void updateStatusBar() {
		if (sa.getFarbeAmZug() == Spielbrett.SCHWARZ) {
			statusText.setText("Schwarz am Zug");
		}
		else {
			statusText.setText("Weiß am Zug");
		}
	}
	
	public void setzeSpielablauf(Spielablauf wert) {
		sa = wert;
		dc.setSpielbrett(sa.getSpielbrett());
		statusText.setText("Neues Spiel - Schwarz am Zug");
	}

	FileNewAction fna = new FileNewAction();
	private class FileNewAction extends AbstractAction {
		private FileNewAction() {
			super.putValue(NAME, "Neu");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Spiel");			
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser od = new JFileChooser();

			setzeSpielablauf(new Spielablauf());

			uma.setEnabled(false);
			rma.setEnabled(false);
			fsa.setEnabled(true);
		}
	}
	

	FileOpenAction foa = new FileOpenAction();
	private class FileOpenAction extends AbstractAction {
		private FileOpenAction() {
			super.putValue(NAME, "Öffnen...");			
			super.putValue(SHORT_DESCRIPTION, "Öffnet einen Spielstand");			
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/open.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser od = new JFileChooser();

			od.setDialogTitle("Spielablauf öffnen...");
	        od.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
		            "Dame-Spielablauf (*.dsa)", "dsa"));

			int returnVal = od.showOpenDialog(DameFenster.this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filename = od.getSelectedFile().getAbsolutePath();

				try {
			        java.io.FileInputStream fis = new java.io.FileInputStream(filename);
			        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
	
			        try {
				        DameFenster.this.setzeSpielablauf((Spielablauf)ois.readObject());
			        }
			        finally {
			        	ois.close();
			        	fis.close();
			        }
				}
				catch (java.io.FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(DameFenster.this, "Datei konnte nicht gefunden werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
				catch (java.io.IOException ioe) {
					JOptionPane.showMessageDialog(DameFenster.this, "Lesefehler: " + ioe.toString(), "Fehler", JOptionPane.ERROR_MESSAGE);
				}
				catch (ClassNotFoundException cnfe) {
					JOptionPane.showMessageDialog(DameFenster.this, "Klasse nicht gefunden: " + cnfe.toString(), "Fehler", JOptionPane.ERROR_MESSAGE);
				}
	        }
		}
	}

	FileSaveAction fsa = new FileSaveAction();
	private class FileSaveAction extends AbstractAction {
		private FileSaveAction() {
			this.setEnabled(false);

			super.putValue(NAME, "Speichern unter...");			
			super.putValue(SHORT_DESCRIPTION, "Speichert den aktuellen Spielstand");			
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/save.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser od = new JFileChooser();

			od.setDialogTitle("Spielablauf speichern...");
	        od.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
		            "Dame-Spielablauf (*.dsa)", "dsa"));

			int returnVal = od.showSaveDialog(DameFenster.this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    	String filename = od.getSelectedFile().getAbsolutePath();
				if (od.getSelectedFile().getName().indexOf(".")==-1)
					filename += ".dsa";

				try {
			        java.io.FileOutputStream fos = new java.io.FileOutputStream(filename, false);
			        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
	
			        try {
				        oos.writeObject(DameFenster.this.sa);
			        }
			        finally {
			        	oos.close();
			        	fos.close();
			        }
				}
				catch (java.io.FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(DameFenster.this, "Datei konnte nicht geschrieben werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
				catch (java.io.IOException ioe) {
					JOptionPane.showMessageDialog(DameFenster.this, "Schreibfehler: " + ioe.toString(), "Fehler", JOptionPane.ERROR_MESSAGE);
				}
		    }
		}
	}

	NetworkAction network_a = new NetworkAction();
	private class NetworkAction extends AbstractAction {
		private NetworkAction() {
			super.putValue(NAME, "Netzwerk...");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein Netzwerkspiel");			
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/save.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			NetzwerkDialog frm = new NetzwerkDialog(DameFenster.this);
			frm.setVisible(true);
		}
	}

	UndoMoveAction uma = new UndoMoveAction();
	private class UndoMoveAction extends AbstractAction {
		private UndoMoveAction() {
			this.setEnabled(false);

			super.putValue(NAME, "Zug rückgängig");			
			super.putValue(SHORT_DESCRIPTION, "Macht den letzten Zug rückgängig");			

			KeyStroke accKey = KeyStroke.getKeyStroke("ctrl Z");
			super.putValue(ACCELERATOR_KEY, accKey);		
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/undo.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			sa.undoZug();
			dc.repaint();
			updateStatusBar();
			
			this.setEnabled(sa.getUndoCount()>0);
			rma.setEnabled(sa.getRedoCount()>0);
			//JOptionPane.showMessageDialog(DameFenster.this, "Noch nicht implementiert!", "Fehlt noch", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	RedoMoveAction rma = new RedoMoveAction();
	private class RedoMoveAction extends AbstractAction {
		private RedoMoveAction() {
			this.setEnabled(false);

			super.putValue(NAME, "Zug wiederherstellen");			
			super.putValue(SHORT_DESCRIPTION, "Stellt den letzten rückgängig gemachten Zug wieder her");			

			KeyStroke accKey = KeyStroke.getKeyStroke("ctrl Y");
			super.putValue(ACCELERATOR_KEY, accKey);		
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/redo.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			sa.redoZug();
			dc.repaint();
			updateStatusBar();
			
			uma.setEnabled(sa.getUndoCount()>0);
			this.setEnabled(sa.getRedoCount()>0);
			//JOptionPane.showMessageDialog(DameFenster.this, "Noch nicht implementiert!", "Fehlt noch", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	CloseAction cla = new CloseAction();
	private class CloseAction extends AbstractAction {
		private CloseAction() {
			super.putValue(NAME, "Beenden");			
			super.putValue(SHORT_DESCRIPTION, "Beendet das Programm");			

			KeyStroke accKey = KeyStroke.getKeyStroke("alt F4");
			super.putValue(ACCELERATOR_KEY, accKey);		
			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/redo.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			DameFenster.this.setVisible(false);
		}
	}

	AboutAction about_a = new AboutAction();
	private class AboutAction extends AbstractAction {
		private AboutAction() {
			super.putValue(NAME, "Über...");			
			super.putValue(SHORT_DESCRIPTION, "Informationen zu diesem Programm");			

			//super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/redo.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			UeberDialog frm = new UeberDialog(DameFenster.this);
			frm.setVisible(true);
		}
	}
}
