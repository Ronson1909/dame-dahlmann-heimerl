package brettspiele.halma;

import brettspiele.IZug;

public class Zug implements IZug, Cloneable {
    public Zug() {
    }

    public Zug(int startX, int startY, int endeX, int endeY) {
    	setCoordinates(startX, startY, endeX, endeY);
    }
    
    public Zug clone() {
    	return new Zug(startX, startY, endeX, endeY);
    }

    public void setCoordinates(int startX, int startY, int endeX, int endeY) {
    	this.startX=startX;
    	this.startY=startY;
    	this.endeX=endeX;
    	this.endeY=endeY;
    }
    
    private int startX;
    private int startY;
    private int endeX;
    private int endeY;

    @Override
    public boolean equals(Object o) {
    	if (o instanceof Zug) {
    		Zug z = (Zug)o;
    		
    		return (z.startX==startX && z.startY==startY && z.endeX==endeX && z.endeY==endeY);
    	}
    	else
    		return false;
    }
    
    public Zug transform(int fromSpieler, int spielerzahl) {
    	Zug z=this.clone();
    	int[] trans1 = HalmaSpielbrett.transformKoordinate(startX, startY, fromSpieler, spielerzahl);
    	int[] trans2 = HalmaSpielbrett.transformKoordinate(endeX, endeY, fromSpieler, spielerzahl);
    	z.setCoordinates(trans1[0], trans1[1], trans2[0], trans2[1]);
    	return z;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int wert) {
        startX=wert;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int wert) {
        startY=wert;
    }

    public int getEndeX() {
        return endeX;
    }

    public void setEndeX(int wert) {
        endeX=wert;
    }

    public int getEndeY() {
        return endeY;
    }

    public void setEndeY(int wert) {
        endeY=wert;
    }

    public Zug createUmgekehrtenZug() {
    	return new Zug(endeX, endeY, startX, startY);
    }
    
    public boolean hatGueltigeKoordinaten() {
    	return (HalmaSpielbrett.isGueltigeKoordinate(startX, startY) 
    			&& HalmaSpielbrett.isGueltigeKoordinate(endeX, endeY));
    }

	public String toString() {
		return "(" + getStartX() + "," + getStartY() + ")-(" + getEndeX() + "," + getEndeY() + ")";
	}
}
