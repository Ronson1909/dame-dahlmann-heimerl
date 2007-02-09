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
	
	public DameFenster() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super("Dame");
		this.setSize(200, 200);
		this.add(dc);

		setzeSpielablauf(new Spielablauf());
		
		dc.addZugBeendetListener(dc.new ZugBeendetAdapter() {
			public void zugBeendet(ZugBeendetEvent zbe) {
				sa.macheZug(zbe.getZugfolge());
			}
		});
		
		mnFile.setText("Datei");
		mnMain.add(mnFile);
		
		mnFile.add(foa);
		mnFile.add(fsa);
		
		this.setJMenuBar(mnMain);
	}
	
	public void setzeSpielablauf(Spielablauf wert) {
		sa = wert;
		dc.setSpielbrett(sa.getSpielbrett());
	}
	
	FileOpenAction foa = new FileOpenAction();
	private class FileOpenAction extends AbstractAction {
		private FileOpenAction() {
			super.putValue(NAME, "Öffnen...");			
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
			super.putValue(NAME, "Speichern unter...");			
		}
		
		public void actionPerformed(ActionEvent e) {
			JFileChooser od = new JFileChooser();

			od.setDialogTitle("Spielablauf öffnen...");
	        od.addChoosableFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
		            "Dame-Spielablauf (*.dsa)", "dsa"));

			int returnVal = od.showSaveDialog(DameFenster.this);
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
				String filename = od.getSelectedFile().getAbsolutePath();

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
}
