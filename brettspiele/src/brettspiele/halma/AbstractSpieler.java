package brettspiele.halma;

import java.util.ArrayList;

import brettspiele.*;

public abstract class AbstractSpieler implements ISpieler {
	protected int eigeneFarbe;

	public AbstractSpieler(int eigeneFarbe) {
		if (eigeneFarbe != HalmaSpielbrett.ROT && eigeneFarbe != HalmaSpielbrett.BLAU  && eigeneFarbe != HalmaSpielbrett.GRUEN)
			throw new IllegalArgumentException("Die Farbe des Spielers ist ungültig!");

		this.eigeneFarbe = eigeneFarbe;
	}

	public AbstractSpieler(int eigeneFarbe, ZugBeendetListener zbl) {
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
	
	//For Serialization
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.writeInt(eigeneFarbe);
	}
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		eigeneFarbe = in.readInt();
		zbls = new ArrayList<ZugBeendetListener>();
	}
	private void readObjectNoData() throws java.io.ObjectStreamException {
		eigeneFarbe = HalmaSpielbrett.ROT;
		zbls = new ArrayList<ZugBeendetListener>();
	}
	
	private ArrayList<ZugBeendetListener> zbls = new ArrayList<ZugBeendetListener>();
	public void addZugBeendetListener(ZugBeendetListener zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}
	public void removeZugBeendetListener(ZugBeendetListener zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}
	public void clearZugBeendetListener() {
		zbls.clear();
	}

	final public void startGettingNaechstenZug(ISpielsituation ss) {
		startGettingNaechstenZug((HalmaSpielbrett)ss);
	}
	public abstract void startGettingNaechstenZug(HalmaSpielbrett sb);
	public void cancelGettingNaechstenZug() {
		beendeZug(null);
	}
	
	protected final void beendeZug(IZug zug) {
		for (ZugBeendetListener zbl : (Iterable<ZugBeendetListener>)zbls.clone()) {
			zbl.zugBeendet(new ZugBeendetEvent(this, this, zug));
		}
	}
}
