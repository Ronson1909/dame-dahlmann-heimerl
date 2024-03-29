package brettspiele.dame;

import java.util.ArrayList;

public class ZugFolge extends ArrayList<Zug> implements brettspiele.IZug {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4540760736931068678L;

	public ZugFolge() {
		super(3);
	}
	
	public String toString() {
		String tmp="";

		for (int i=0;i<this.size();i++) {
			Zug z = this.get(i);
			
			if (i==0)
				tmp = "(" + z.gibStartX() + "," + z.gibStartY() + ")";
				
			tmp += "-(" + z.gibEndeX() + "," + z.gibEndeY() + ")";
		}
		
		return tmp;
	}
}
