package dame;

import javax.swing.JFrame;
import java.awt.HeadlessException;

public class DameFenster extends javax.swing.JFrame {
	public static void main(String[] args) {
		DameFenster main = new DameFenster();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		main.setVisible(true);
    }

	public DameFenster() throws HeadlessException {
		// TODO Auto-generated constructor stub
		super("Dame");

		DameComponent dc = new DameComponent(); 
		this.setSize(200, 200);
		this.add(dc);
	}
}
