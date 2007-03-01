package brettspiele.schafkopf;

import java.util.ArrayList;

import brettspiele.*;

public abstract class AbstractSpieler implements ISpieler<IZug> {
	protected int eigenePosition;

	public AbstractSpieler(int eigenePosition) {
		if (eigenePosition < 0 || eigenePosition > 3)
			throw new IllegalArgumentException("Die Position des Spielers ist ungültig!");

		this.eigenePosition = eigenePosition;
	}

	public AbstractSpieler(int eigenePosition, ZugBeendetListener<IZug> zbl) {
		this(eigenePosition);

		this.addZugBeendetListener(zbl);
	}
	
	public final int getEigeneFarbe() {
		return eigenePosition;
	}

	public String getName() {
		return "Spieler " + (eigenePosition+1);
	}

	/**
	 * Damit können die Spieler untereinander vernetzt werden. Wenn der andere
	 * Spieler seinen Zug beendet, dann bekommt dieser hier eine Nachricht in
	 * Form dieses Methodenaufrufs.
	 */
	public void zugBeendet(ZugBeendetEvent<IZug> zbe) {

	}

	//For Serialization
	private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
		out.writeInt(eigenePosition);
	}
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		eigenePosition = in.readInt();
		zbls = new ArrayList<ZugBeendetListener<? extends IZug>>();
	}
	private void readObjectNoData() throws java.io.ObjectStreamException {
		eigenePosition = 0;
		zbls = new ArrayList<ZugBeendetListener<? extends IZug>>();
	}
	
	private ArrayList<ZugBeendetListener<? extends IZug>> zbls = new ArrayList<ZugBeendetListener<? extends IZug>>();
	
	@Override
	public void addZugBeendetListener(ZugBeendetListener<IZug> zbl) {
		if (zbl!=null)
			zbls.add(zbl);
	}
	public void removeZugBeendetListener(ZugBeendetListener<IZug> zbl) {
		if (zbl!=null)
			zbls.remove(zbl);
	}
	public void clearZugBeendetListener() {
		zbls.clear();
	}

	final public void startGettingNaechstenZug(ISpielsituation ss) {
		java.util.Date now = new java.util.Date();
		System.out.println("Starte Zug um " + now.toGMTString());
		startGettingNaechstenZug((SchafkopfSpielsituation)ss);
	}
	public abstract void startGettingNaechstenZug(SchafkopfSpielsituation sb);
	public void cancelGettingNaechstenZug() {
		beendeZug(null);
	}
	
	protected final void beendeZug(IZug zug) {
		java.util.Date now = new java.util.Date();
		System.out.println("Beende Zug um " + now.toGMTString());

		for (ZugBeendetListener<IZug> zbl : (Iterable<ZugBeendetListener<IZug>>)zbls.clone()) {
			zbl.zugBeendet(new ZugBeendetEvent<IZug>(this, this, zug));
		}
	}
}
