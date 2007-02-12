package dame;

import java.util.ArrayList;

public abstract class AbstractSpieler implements ISpieler {
	protected int eigeneFarbe;

	public AbstractSpieler(int eigeneFarbe) {
		if (eigeneFarbe != Spielbrett.WEISS && eigeneFarbe != Spielbrett.SCHWARZ)
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

	
	/**
	 * Damit können die Spieler untereinander vernetzt werden. Wenn der andere
	 * Spieler seinen Zug beendet, dann bekommt dieser hier eine Nachricht in
	 * Form dieses Methodenaufrufs.
	 */
	public void zugBeendet(ZugBeendetEvent zbe) {

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
		eigeneFarbe = Spielbrett.SCHWARZ;
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

	public abstract void startGettingNaechstenZug(Spielbrett sb);
	public void cancelGettingNaechstenZug() {
		beendeZug(null);
	}

	/**
	 * Führt den Zug aus, d.h. es leitet die Zugfolge an den Listener weiter.
	 * @param zf
	 */
	protected final void beendeZug(ArrayList<Zug> zf) {
		for (ZugBeendetListener zbl : (Iterable<ZugBeendetListener>)zbls.clone()) {
			zbl.zugBeendet(new ZugBeendetEvent(this, this, zf));
		}
	}
}
