package brettspiele.dame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import brettspiele.*;
import brettspiele.SocketHandler.*;


public class NetzwerkSpieler extends AbstractSpieler implements ObjectEmpfangenListener {
    public NetzwerkSpieler(int eigeneFarbe, ZugBeendetListener zbl) {
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
    		throw new IllegalArgumentException("out und sh müssen beide null oder nicht null sein!");
    	
    	if (this.out != out && this.out != null) {
    		//alten Stream schließen
    		
    		try {
				this.out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		
    	}

    	if (this.sh != sh && this.sh != null) {
    		//alten SocketHandler schließen
    		
			this.sh.removeObjectEmpfangenListener(this);
    	}

    	this.out = out;
    	this.sh = sh;
    	
    	if (sh != null) {
    		sh.addObjectEmpfangenListener(this);
    	}
    }
    
    /**
     * Gibt den Netzwerk-Stream zum Senden von Objekten zurück.
     * @return Der Netzwerk-Stream zum Senden von Objekten.
     */
    public ObjectOutputStream getNetworkOutputStream() {
    	return out;
    }
    
    /**
     * Gibt den SocketHandler zum Empfangen von Objekten zurück.
     * @return Der SocketHandler zum Empfangen von Objekten.
     */
    public SocketHandler getNetworkSocketHandler() {
    	return sh;
    }

	public void objectEmpfangen(ObjectEmpfangenEvent oee) {
		//Zug beenden, wenn eine Zugfolge kommt
		Object obj = oee.getEmpfangenesObject();
		
		if (obj.getClass() == java.util.ArrayList.class) {
			java.util.ArrayList al = (java.util.ArrayList)obj;
			
			if (al.size()>0 && al.get(0).getClass() == Zug.class) {
				ZugFolge zf = (ZugFolge)obj;
				
				beendeZug(zf);
			}
		}
	}

	@Override
	public void startGettingNaechstenZug(Spielbrett sb) {
		//kann leer bleiben
	}
	
	@Override
	public void zugBeendet(ZugBeendetEvent<ZugFolge> zbe) {
		//eine Message absenden mit dem vom anderen Spieler durchgeführten Zug
		try {
			out.writeObject(zbe.getZug());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
