package brettspiele.dame;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class SocketHandler extends Thread {
    Socket incoming;

    public SocketHandler(Socket incoming) {
    	this.incoming = incoming;
    }

    public void run() {
		try {
			ObjectInputStream reader =	new ObjectInputStream(incoming.getInputStream());
		    
		    try {
			    while (true) {
					Object obj = reader.readObject();
					objectEmpfangen(obj);
			    }
		    } catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    finally {
			    incoming.close();
		    }
		    
		} catch (java.io.IOException e) {
		    e.printStackTrace();
		}
    }
    
	public class ObjectEmpfangenEvent extends java.util.EventObject {
		private Object obj;
		
		private ObjectEmpfangenEvent(Object source, Object obj) {
			super(source);
			this.obj=obj;
		}
		
		public Object getEmpfangenesObject() {
			return obj;
		}
	}
	
	public interface ObjectEmpfangenListener extends java.util.EventListener {
		public void objectEmpfangen(ObjectEmpfangenEvent oee);
	}

	
	public class ObjectEmpfangenAdapter implements ObjectEmpfangenListener {
		public void objectEmpfangen(ObjectEmpfangenEvent oee) {
			
		}
	}

	private ArrayList<ObjectEmpfangenListener> oels = new ArrayList<ObjectEmpfangenListener>();
	public void addObjectEmpfangenListener(ObjectEmpfangenListener oel) {
		if (oel!=null)
			oels.add(oel);
	}

	public void removeObjectEmpfangenListener(ObjectEmpfangenListener oel) {
		if (oel!=null)
			oels.remove(oel);
	}
	
	protected void objectEmpfangen(Object obj) {
		ObjectEmpfangenEvent oee = new ObjectEmpfangenEvent(this, obj);
		
		for (ObjectEmpfangenListener oel : oels) {
			oel.objectEmpfangen(oee);
		}
	}
    
}
