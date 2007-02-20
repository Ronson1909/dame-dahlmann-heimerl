package brettspiele;

import brettspiele.dame.DameUI;
import brettspiele.halma.HalmaUI;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BrettspieleFenster extends JFrame implements ZugBeendetListener {
	private static IBrettspielUI[] brettspiele = new IBrettspielUI[] {new DameUI(), new HalmaUI()};
	
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
		
        BrettspieleFenster main = new BrettspieleFenster();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
    }

	private ISpielablauf sa;
	private IBrettspielComponent sc;
	private JMenuBar mnMain = new JMenuBar();
	private JMenu mnFile = new JMenu();
		private JMenu mnNeu = new JMenu();
		private JMenu mnNeuNetzwerk = new JMenu();
		private JPopupMenu pumNeu = new JPopupMenu();
	private JMenu mnEdit = new JMenu();
	private JMenu mnHelp = new JMenu();
	
	private JToolBar tbMain = new JToolBar();

	private JLabel statusText;
	
	public BrettspieleFenster() throws HeadlessException {
		super("Brettspiele");
		this.setSize(400, 400);
		
		//setBrettspielComponent(brettspiele[0].createBrettspielComponent());
		
		JPanel status = new JPanel();
		statusText = new JLabel(" ");
		status.setLayout(new java.awt.BorderLayout());
		status.add(statusText, java.awt.BorderLayout.CENTER);
		status.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
		this.add(status, java.awt.BorderLayout.SOUTH);		

		mnFile.setText("Datei");
		
		mnNeu.setText("Neues lokales Spiel");
		mnNeu.setIcon(new ImageIcon(ClassLoader.getSystemResource("brettspiele/images/new.gif")));
		mnFile.add(mnNeu);

		mnNeuNetzwerk.setText("Neues Netzwerkspiel");
		//mnNeuNetzwerk.setIcon(new ImageIcon(ClassLoader.getSystemResource("brettspiele/dame/images/new.gif")));
		mnFile.add(mnNeuNetzwerk);
		
		for (IBrettspielUI bui : brettspiele) {
			mnNeu.add(bui.createFileNewAction(this));
			mnNeuNetzwerk.add(bui.createFileNewNetworkAction(this));
			pumNeu.add(bui.createFileNewAction(this));
		}
		
		mnFile.add(foa);
		mnFile.add(fsa);
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
		
		JButton btnNew = new JButton();
		btnNew.setIcon(new ImageIcon(ClassLoader.getSystemResource("brettspiele/images/new.gif")));
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JButton src = (JButton)arg0.getSource();
				pumNeu.show(src, 0, src.getHeight());
				//pum.setVisible(true);
			}
		});
		
		tbMain.add(btnNew);
		
		//tbMain.add(fn_Dame_action);
		tbMain.add(foa);
		tbMain.add(fsa);
		tbMain.add(uma);
		tbMain.add(rma);
		this.add(tbMain, java.awt.BorderLayout.NORTH);
		
	}
	
	public void updateGUI() {
		boolean lokSp = sa.getSpielerAmZug() instanceof ILokalerSpieler;
		
		fsa.setEnabled(sa.getUndoCount()>0);// && lokSp);
		uma.setEnabled(sa.getUndoCount()>0 && lokSp);
		rma.setEnabled(sa.getRedoCount()>0 && lokSp);
		
		statusText.setText(sa.getSpielerAmZug().getName() + " am Zug");
	}
	
	public IBrettspielComponent getBrettspielComponent() {
		return sc;
	}

	public void setBrettspielComponent(IBrettspielComponent sc) {
		if (this.sc!=sc && this.sc!=null) {
			this.remove((JComponent)this.sc);
		}
		
		this.sc=sc;

		this.add((JComponent)this.sc);
		this.validate();
	}

	public void setSpielablauf(ISpielablauf wert) {
		if (wert != sa) {
			sa = wert;
			sc.setSpielsituation(sa.getSpielsituation());

			updateGUI();
			
			sa.getSpielerAmZug().startGettingNaechstenZug(sa.getSpielsituation().clone());
		}
	}

	public void zugBeendet(ZugBeendetEvent zbe) {
		sa.zugBeendet(zbe);
		sc.repaint();

       	int tmp = sa.getSpielsituation().isSpielBeendet();
       	if (tmp!=-1) {
       		JOptionPane.showMessageDialog(this, "Das Spiel ist beendet!", "Spiel beendet", JOptionPane.INFORMATION_MESSAGE);
       		return;
       	}

       	updateGUI();
	}

	FileOpenAction foa = new FileOpenAction();
	private class FileOpenAction extends AbstractAction {
		private FileOpenAction() {
			super.putValue(NAME, "Öffnen...");			
			super.putValue(SHORT_DESCRIPTION, "Öffnet einen Spielstand");			
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/images/open.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser od = new JFileChooser();

			od.setDialogTitle("Spielstand öffnen...");
			
			for (IBrettspielUI bsui : brettspiele) {
		        od.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
			            bsui.getName() + "-Spiel (*." + bsui.getDefaultExtension() + ")", bsui.getDefaultExtension()));
			}
			
			int returnVal = od.showOpenDialog(BrettspieleFenster.this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filename = od.getSelectedFile().getAbsolutePath();

				try {
			        java.io.FileInputStream fis = new java.io.FileInputStream(filename);
			        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(fis);
	
			        try {
			        	ISpielablauf newSa = (ISpielablauf)ois.readObject();
			        	setBrettspielComponent(newSa.createBrettspielComponent());

			        	for (ISpieler sp : newSa.getSpieler()) {
			        		sp.addZugBeendetListener(BrettspieleFenster.this);
			        		
			        		if (sp instanceof ILokalerSpieler) {
				        		((ILokalerSpieler)sp).setBrettspieleFenster(BrettspieleFenster.this);
				    			sc.addZugBeendetListener(sp);
			        		}
			        	}

				        BrettspieleFenster.this.setSpielablauf(newSa);
			        }
			        finally {
			        	ois.close();
			        	fis.close();
			        }
				}
				catch (java.io.FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(BrettspieleFenster.this, "Datei konnte nicht gefunden werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
				catch (java.io.IOException ioe) {
					JOptionPane.showMessageDialog(BrettspieleFenster.this, "Lesefehler: " + ioe.toString(), "Fehler", JOptionPane.ERROR_MESSAGE);
				}
				catch (ClassNotFoundException cnfe) {
					JOptionPane.showMessageDialog(BrettspieleFenster.this, "Klasse nicht gefunden: " + cnfe.toString(), "Fehler", JOptionPane.ERROR_MESSAGE);
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
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/images/save.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser sd = new JFileChooser();

			sd.setDialogTitle("Spielstand speichern...");

			for (IBrettspielUI bsui : brettspiele) {
				sd.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
			            bsui.getName() + "-Spiel (*." + bsui.getDefaultExtension() + ")", bsui.getDefaultExtension()));
			}

			int returnVal = sd.showSaveDialog(BrettspieleFenster.this);
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
				if (sd.getSelectedFile().getName().indexOf(".")==-1) {
					filename += "." + ((javax.swing.filechooser.FileNameExtensionFilter)sd.getFileFilter()).getExtensions()[0];
				}

				try {
			        java.io.FileOutputStream fos = new java.io.FileOutputStream(filename, false);
			        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(fos);
	
			        ISpielablauf newSa = BrettspieleFenster.this.sa;
			        try {
			        	for (ISpieler sp : newSa.getSpieler()) {
			        		if (sp instanceof ILokalerSpieler) {
			        			((ILokalerSpieler)sp).setBrettspieleFenster(null);
			        		}
			        	}
			        	
			        	oos.writeObject(newSa);
			        }
			        finally {
			        	for (ISpieler sp : newSa.getSpieler()) {
			        		if (sp instanceof ILokalerSpieler) {
			        			((ILokalerSpieler)sp).setBrettspieleFenster(BrettspieleFenster.this);
			        		}
			        	}

			        	oos.close();
			        	fos.close();
			        }
				}
				catch (java.io.FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(BrettspieleFenster.this, "Datei konnte nicht geschrieben werden!", "Fehler", JOptionPane.ERROR_MESSAGE);
				}
				catch (java.io.IOException ioe) {
					JOptionPane.showMessageDialog(BrettspieleFenster.this, "Schreibfehler: " + ioe.toString(), "Fehler", JOptionPane.ERROR_MESSAGE);
				}
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
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/images/undo.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			int currSpielerIdx = -1;
			
			for (int i=0;i<sa.getSpieler().length;i++) {
				currSpielerIdx = i;
				
				if (sa.getSpieler()[i] == sa.getSpielerAmZug()) {
					break;
				}
			}
			assert(currSpielerIdx!=-1);
			
			int undoZüge = 1;
			
			for (int i=currSpielerIdx-1;i>=0;i--) {
				if (sa.getSpieler()[i] instanceof ILokalerSpieler) {
					sa.undoZug(undoZüge);
					sc.repaint();
					updateGUI();
					return;
				}
				undoZüge++;
			}

			for (int i=sa.getSpieler().length-1;i>=currSpielerIdx;i--) {
				if (sa.getSpieler()[i] instanceof ILokalerSpieler) {
					sa.undoZug(undoZüge);
					sc.repaint();
					updateGUI();
					return;
				}
				undoZüge++;
			}

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
			super.putValue(SMALL_ICON, new ImageIcon(ClassLoader.getSystemResource("brettspiele/images/redo.gif")));
		}
		
		public void actionPerformed(ActionEvent e) {
			int currSpielerIdx = -1;
			
			for (int i=0;i<sa.getSpieler().length;i++) {
				currSpielerIdx = i;
				
				if (sa.getSpieler()[i] == sa.getSpielerAmZug()) {
					break;
				}
			}
			assert(currSpielerIdx!=-1);
			
			int redoZüge = 1;
			
			for (int i=currSpielerIdx+1;i<sa.getSpieler().length;i++) {
				if (sa.getSpieler()[i] instanceof ILokalerSpieler) {
					sa.redoZug(redoZüge);
					sc.repaint();
					updateGUI();
					return;
				}
				redoZüge++;
			}

			for (int i=0;i<=currSpielerIdx;i++) {
				if (sa.getSpieler()[i] instanceof ILokalerSpieler) {
					sa.redoZug(redoZüge);
					sc.repaint();
					updateGUI();
					return;
				}
				redoZüge++;
			}

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
			BrettspieleFenster.this.setVisible(false);
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
			UeberDialog frm = new UeberDialog(BrettspieleFenster.this);
			frm.setVisible(true);
		}
	}
}
