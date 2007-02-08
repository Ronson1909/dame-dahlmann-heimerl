package dame;

import javax.swing.JFrame;

import dame.DameComponent.ZugBeendetEvent;

import java.awt.HeadlessException;

public class DameFenster extends javax.swing.JFrame {
	public static void main(String[] args) {
		DameFenster main = new DameFenster();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		main.setVisible(true);
    }

	private Spielablauf sa = new Spielablauf();
	private DameComponent dc = new DameComponent();
	
	public DameFenster() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super("Dame");

		dc.setSpielbrett(sa.getSpielbrett());
		this.setSize(200, 200);
		this.add(dc);
		
		dc.addZugBeendetListener(dc.new ZugBeendetAdapter() {
			public void zugBeendet(ZugBeendetEvent zbe) {
				sa.macheZug(zbe.getZugfolge());
			}
		}
		);
	}
}
