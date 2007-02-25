package brettspiele.dame;

import java.io.IOException;
import java.io.ObjectOutputStream;

import brettspiele.*;
import brettspiele.SocketHandler.*;


public class NetzwerkSpieler extends AbstractSpieler implements ObjectEmpfangenListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2824430740565737816L;

	public NetzwerkSpieler(int eigeneFarbe, ZugBeendetListener<ZugFolge> zbl) {
		super(eigeneFarbe, zbl);
	}

	private ObjectOutputStream out;
    private SocketHandler sh;
    
    /**
     * Setzt den Netzwerk-Stream zum Senden von Objekten.
     * @param out Der zu verwendende Netzwerk-Stream.
     */
    public void setNetwork(ObjectOutputStream out, SocketHandler sh) {
    	if (out==null ^ sh==null)
    		throw new IllegalArgumentException("out und sh m�ssen beide null oder nicht null sein!");
    	
    	if (this.out != out && this.out != null) {
    		//alten Stream schlie�en
    		
    		try {
				this.out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    	}

    	if (this.sh != sh && this.sh != null) {
    		//alten SocketHandler schlie�en
    		
			this.sh.removeObjectEmpfangenListener(this);
    	}

    	this.out = out;
    	this.sh = sh;
    	
    	if (sh != null) {
    		sh.addObjectEmpfangenListener(this);
    	}
    }
    
    /**
     * Gibt den Netzwerk-Stream zum Senden von Objekten zur�ck.
     * @return Der Netzwerk-Stream zum Senden von Objekten.
     */
    public ObjectOutputStream getNetworkOutputStream() {
    	return out;
    }
    
    /**
     * Gibt den SocketHandler zum Empfangen von Objekten zur�ck.
     * @return Der SocketHandler zum Empfangen von Objekten.
     */
    public SocketHandler getNetworkSocketHandler() {
    	return sh;
    }

	public void objectEmpfangen(ObjectEmpfangenEvent oee) {
		//Zug beenden, wenn eine Zugfolge kommt
		Object obj = oee.getEmpfangenesObject();
		
		if (obj instanceof ZugFolge) {
			ZugFolge zf = (ZugFolge)obj;
			beendeZug(zf);
		}
	}

	@Override
	public void startGettingNaechstenZug(Spielbrett sb) {
		//kann leer bleiben
	}
	
	@Override
	public void zugBeendet(ZugBeendetEvent<ZugFolge> zbe) {
		//eine Message absenden mit dem vom anderen Spieler durchgef�hrten Zug
		try {
			out.writeObject(zbe.getZug());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void configure(java.awt.Window owner, Object info) {
		NetzwerkDialog nwd = new NetzwerkDialog(owner, out, sh, (Integer)info == 1);
		
		nwd.setVisible(true);
		this.setNetwork(nwd.getObjectOutputStream(), nwd.getSocketHandler()); 
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}
}
