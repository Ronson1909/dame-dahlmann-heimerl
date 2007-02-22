package brettspiele;

import javax.swing.Action;

public interface IBrettspielUI {
	public Action createFileNewAction(BrettspieleFenster bsf);
	public String getName();
	public String getDefaultExtension();
}
