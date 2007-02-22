package brettspiele.dame;

import java.util.ArrayList;

import javax.swing.JFrame;

import brettspiele.*;

public abstract class AbstractSpieler implements ISpieler<ZugFolge> {
	protected int eigeneFarbe;

	public AbstractSpieler(int eigeneFarbe) {
		if (eigeneFarbe != Spielbrett.WEISS && eigeneFarbe != Spielbrett.SCHWARZ)
			throw new IllegalArgumentException("Die Farbe des Spielers ist ungültig!");

		this.eigeneFarbe = eigeneFarbe;
	}

	public AbstractSpieler(int eigeneFarbe, ZugBeendetListener<ZugFolge> zbl) {
		this(eigeneFarbe);

		this.addZugBeendetListener(zbl);
	}
	
	public boolean isConfigurable() {
		return false;
	}
	
	public void configure(java.awt.Window owner, Object info) {
		
	}
	
	public final int getEigeneFarbe() {
		return eigeneFarbe;
	}

	public String getName() {
		switch (eigeneFarbe) {
		case Spielbrett.WEISS:
			return "Weiß";
		case Spielbrett.SCHWARZ:
			return "Schwarz";
		}
		
		return "";
	}
	
	/**
	 * Damit können die Spieler untereinander vernetzt werden. Wenn der andere
	 * Spieler seinen Zug beendet, dann bekommt dieser hier eine Nachricht in
	 * Form dieses Methodenaufrufs.
	 */
	public void zugBeendet(ZugBeendetEvent<ZugFolge> zbe) {

	}

	//For Serialization
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.writeInt(eigeneFarbe);
	}
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		eigeneFarbe = in.readInt();
		zbls = new ArrayList<ZugBeendetListener<ZugFolge>>();
	}
	private void readObjectNoData() throws java.io.ObjectStreamException {
		eigeneFarbe = Spielbrett.SCHWARZ;
		zbls = new ArrayList<ZugBeendetListener<ZugFolge>>();
	}
	
	private ArrayList<ZugBeendetListener<ZugFolge>> zbls = new ArrayList<ZugBeendetListener<ZugFolge>>();
	public void addZugBeendetListener(ZugBeendetListener<ZugFolge> zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}
	public void removeZugBeendetListener(ZugBeendetListener<ZugFolge> zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}
	public void clearZugBeendetListener() {
		zbls.clear();
	}

	final public void startGettingNaechstenZug(ISpielsituation ss) {
		startGettingNaechstenZug((Spielbrett)ss);
	}
	public abstract void startGettingNaechstenZug(Spielbrett sb);
	public void cancelGettingNaechstenZug() {
		beendeZug(null);
	}

	/**
	 * Führt den Zug aus, d.h. es leitet die Zugfolge an den Listener weiter.
	 * @param zf
	 */
	protected final void beendeZug(ZugFolge zf) {
		for (ZugBeendetListener zbl : (Iterable<ZugBeendetListener<ZugFolge>>)zbls.clone()) {
			zbl.zugBeendet(new ZugBeendetEvent<ZugFolge>(this, this, zf));
		}
	}
}
