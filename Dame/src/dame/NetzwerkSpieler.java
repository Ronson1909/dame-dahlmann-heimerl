package dame;

import java.io.IOException;
import java.io.ObjectOutputStream;

import dame.SocketHandler.*;

public class NetzwerkSpieler implements ISpieler, ObjectEmpfangenListener {
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
				
	}
}
