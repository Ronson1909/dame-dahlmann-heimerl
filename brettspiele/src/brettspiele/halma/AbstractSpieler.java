package brettspiele.halma;

import java.util.ArrayList;

import brettspiele.*;

public abstract class AbstractSpieler implements ISpieler<Zug> {
	protected int eigeneFarbe;

	public AbstractSpieler(int eigeneFarbe) {
		if (eigeneFarbe != HalmaSpielbrett.ROT && eigeneFarbe != HalmaSpielbrett.BLAU  && eigeneFarbe != HalmaSpielbrett.GRUEN)
			throw new IllegalArgumentException("Die Farbe des Spielers ist ungültig!");

		this.eigeneFarbe = eigeneFarbe;
	}

	public AbstractSpieler(int eigeneFarbe, ZugBeendetListener<Zug> zbl) {
		this(eigeneFarbe);

		this.addZugBeendetListener(zbl);
	}
	
	public final int getEigeneFarbe() {
		return eigeneFarbe;
	}

	public String getName() {
		switch (eigeneFarbe) {
		case HalmaSpielbrett.ROT:
			return "Rot";
		case HalmaSpielbrett.BLAU:
			return "Blau";
		case HalmaSpielbrett.GRUEN:
			return "Grün";
		}
		
		return "";
	}

	/**
	 * Damit können die Spieler untereinander vernetzt werden. Wenn der andere
	 * Spieler seinen Zug beendet, dann bekommt dieser hier eine Nachricht in
	 * Form dieses Methodenaufrufs.
	 */
	public void zugBeendet(ZugBeendetEvent<Zug> zbe) {

	}

	//For Serialization
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.writeInt(eigeneFarbe);
	}
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		eigeneFarbe = in.readInt();
		zbls = new ArrayList<ZugBeendetListener<? extends Zug>>();
	}
	private void readObjectNoData() throws java.io.ObjectStreamException {
		eigeneFarbe = HalmaSpielbrett.ROT;
		zbls = new ArrayList<ZugBeendetListener<? extends Zug>>();
	}
	
	private ArrayList<ZugBeendetListener<? extends Zug>> zbls = new ArrayList<ZugBeendetListener<? extends Zug>>();
	
	@Override
	public void addZugBeendetListener(ZugBeendetListener<Zug> zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}
	public void removeZugBeendetListener(ZugBeendetListener<Zug> zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}
	public void clearZugBeendetListener() {
		zbls.clear();
	}

	final public void startGettingNaechstenZug(ISpielsituation ss) {
		java.util.Date now = new java.util.Date();
		System.out.println("Starte Zug um " + now.toGMTString());
		startGettingNaechstenZug((HalmaSpielbrett)ss);
	}
	public abstract void startGettingNaechstenZug(HalmaSpielbrett sb);
	public void cancelGettingNaechstenZug() {
		beendeZug(null);
	}
	
	protected final void beendeZug(Zug zug) {
		java.util.Date now = new java.util.Date();
		System.out.println("Beende Zug um " + now.toGMTString());

		for (ZugBeendetListener<Zug> zbl : (Iterable<ZugBeendetListener<Zug>>)zbls.clone()) {
			zbl.zugBeendet(new ZugBeendetEvent<Zug>(this, this, zug));
		}
	}
}
