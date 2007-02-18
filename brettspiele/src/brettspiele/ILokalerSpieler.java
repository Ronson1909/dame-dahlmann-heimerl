package brettspiele;

public interface ILokalerSpieler<Z extends IZug> extends ISpieler<Z> {
	public BrettspieleFenster getBrettspieleFenster();
	public void setBrettspieleFenster(BrettspieleFenster bsf);
}
