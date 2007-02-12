package dame;

import javax.swing.*;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;

public class DameFenster extends JFrame implements ZugBeendetListener {
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
	private SpielbrettComponent sc = new SpielbrettComponent();
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
		this.add(sc);

		JPanel status = new JPanel();
		statusText = new JLabel(" ");
		status.setLayout(new java.awt.BorderLayout());
		status.add(statusText, java.awt.BorderLayout.CENTER);
		status.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
		this.add(status, java.awt.BorderLayout.SOUTH);		

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
	
	private void updateGUI() {
		uma.setEnabled(sa.getUndoCount()>0);
		rma.setEnabled(sa.getRedoCount()>0);

		if (sa.getFarbeAmZug() == Spielbrett.SCHWARZ) {
			statusText.setText("Schwarz am Zug");
		}
		else {
			statusText.setText("Weiß am Zug");
		}
	}
	
	public SpielbrettComponent getSpielbrettComponent() {
		return sc;
	}
	
	public void setSpielablauf(Spielablauf wert) {
		if (wert != sa) {
			sa = wert;
			sc.setSpielbrett(sa.getSpielbrett());
			updateGUI();
		}
	}

	public void zugBeendet(ZugBeendetEvent zbe) {
		sa.zugBeendet(zbe);
		
		sc.repaint();
		
       	updateGUI();

       	switch (sa.getSpielbrett().isSpielBeendet()) {
       	case Spielbrett.WEISS:
       		JOptionPane.showMessageDialog(null, "Weiß hat gewonnen!", "Spiel beendet", JOptionPane.INFORMATION_MESSAGE);
       		return;
       	case Spielbrett.SCHWARZ:
           	JOptionPane.showMessageDialog(null, "Schwarz hat gewonnen!", "Spiel beendet", JOptionPane.INFORMATION_MESSAGE);
           	return;
       	default:
       	}
	}

	FileNewAction fna = new FileNewAction();
	private class FileNewAction extends AbstractAction {
		private FileNewAction() {
			super.putValue(NAME, "Neu");			
			super.putValue(SHORT_DESCRIPTION, "Startet ein neues Spiel");			
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("dame/images/new.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			Spielablauf newSa = new Spielablauf();
			LokalerSpieler sw = new LokalerSpieler(DameFenster.this, Spielbrett.SCHWARZ, DameFenster.this);
			LokalerSpieler we = new LokalerSpieler(DameFenster.this, Spielbrett.WEISS, DameFenster.this);
			
			//Spieler gegenseitig verlinken
			sw.addZugBeendetListener(we);
			we.addZugBeendetListener(sw);
			
			//SpielbrettComponent mit den Spielern verknüpfen
			sc.setLokalerSpieler(sw);
			sc.addZugBeendetListener(sw);
			sc.addZugBeendetListener(we);

			//Spiel starten
			newSa.starten(sw, we);
			setSpielablauf(newSa);
			
			statusText.setText("Neues Spiel - Schwarz am Zug");
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
			        	Spielablauf newSa = (Spielablauf)ois.readObject();
			        	newSa.getSpielerWeiss().addZugBeendetListener(DameFenster.this);
			        	newSa.getSpielerSchwarz().addZugBeendetListener(DameFenster.this);

			        	if (newSa.getSpielerWeiss().getClass() == LokalerSpieler.class)
			        		((LokalerSpieler)newSa.getSpielerWeiss()).setDameFenster(DameFenster.this);
			        	if (newSa.getSpielerSchwarz().getClass() == LokalerSpieler.class)
			        		((LokalerSpieler)newSa.getSpielerSchwarz()).setDameFenster(DameFenster.this);
			        	
				        DameFenster.this.setSpielablauf(newSa);
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
			JFileChooser sd = new JFileChooser();

			sd.setDialogTitle("Spielablauf speichern...");
	        sd.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
		            "Dame-Spielablauf (*.dsa)", "dsa"));

			int returnVal = sd.showSaveDialog(DameFenster.this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    	if (sd.getSelectedFile().exists()) {
		    		int response = JOptionPane.showConfirmDialog (null,
		    				"Wollen Sie die vorhandene Datei wirklich überschreiben?","Überschreiben",
		    				JOptionPane.YES_NO_OPTION,
		    				JOptionPane.QUESTION_MESSAGE);
		    		
		             if (response == JOptionPane.NO_OPTION)
		            	 return;
		    		
		    	}
		    	
		    	String filename = sd.getSelectedFile().getAbsolutePath();
				if (sd.getSelectedFile().getName().indexOf(".")==-1)
					filename += ".dsa";

				try {
			        java.io.FileOutputStream fos = new java.io.FileOutputStream(filename, false);
			        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
	
			        Spielablauf newSa = DameFenster.this.sa;
			        try {
			        	if (newSa.getSpielerWeiss().getClass() == LokalerSpieler.class)
			        		((LokalerSpieler)newSa.getSpielerWeiss()).setDameFenster(null);
			        	if (newSa.getSpielerSchwarz().getClass() == LokalerSpieler.class)
			        		((LokalerSpieler)newSa.getSpielerSchwarz()).setDameFenster(null);

			        	oos.writeObject(newSa);
			        }
			        finally {
			        	if (newSa.getSpielerWeiss().getClass() == LokalerSpieler.class)
			        		((LokalerSpieler)newSa.getSpielerWeiss()).setDameFenster(DameFenster.this);
			        	if (newSa.getSpielerSchwarz().getClass() == LokalerSpieler.class)
			        		((LokalerSpieler)newSa.getSpielerSchwarz()).setDameFenster(DameFenster.this);
			        	
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
			
			if (frm.getSocketHandler() != null && frm.getObjectOutputStream() != null) {
				Spielablauf newSa = new Spielablauf();
				
				AbstractSpieler sw, we;
				NetzwerkSpieler ns;
				LokalerSpieler ls;
				if (frm.isServer()) {
					ls = new LokalerSpieler(DameFenster.this, Spielbrett.SCHWARZ, DameFenster.this);
					ns = new NetzwerkSpieler(Spielbrett.WEISS, DameFenster.this);
					we = ns;
					sw = ls;
				}
				else {
					ns = new NetzwerkSpieler(Spielbrett.SCHWARZ, DameFenster.this);
					ls = new LokalerSpieler(DameFenster.this, Spielbrett.WEISS, DameFenster.this);
					sw = ns;
					we = ls;
				}
				ns.setNetwork(frm.getObjectOutputStream(), frm.getSocketHandler());
				
				//Spieler gegenseitig verlinken
				sw.addZugBeendetListener(we);
				we.addZugBeendetListener(sw);
				
				//SpielbrettComponent mit den Spielern verknüpfen
				sc.setLokalerSpieler(ls);
				sc.addZugBeendetListener(ls);

				//Spiel starten
				newSa.starten(sw, we);
				setSpielablauf(newSa);
				
				statusText.setText("Neues Spiel - Schwarz am Zug");
				uma.setEnabled(false);
				rma.setEnabled(false);
				fsa.setEnabled(true);
			}
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
			sc.repaint();
			updateGUI();
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
			sc.repaint();
			updateGUI();
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

class LokalerSpieler extends AbstractSpieler implements ZugBeendetListener {
	private DameFenster df;
	
	public LokalerSpieler(DameFenster df, int eigeneFarbe, ZugBeendetListener zbl) {
		super(eigeneFarbe, zbl);
		
		this.df=df;
	}
	
	public void startGettingNaechstenZug(Spielbrett sb) {
		df.getSpielbrettComponent().setLokalerSpieler(this);
	}

	public void zugBeendet(ZugBeendetEvent zbe) {
		if (zbe.getSpieler()==this) {
			beendeZug(zbe.getZugfolge());
		}
	}

	public DameFenster getDameFenster() {
		return df;
	}

	public void setDameFenster(DameFenster df) {
		this.df = df;
	}
}
