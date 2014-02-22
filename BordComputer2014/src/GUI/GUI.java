package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GUI {
	private JFrame frame;

	private GUI() {
		frame = new JFrame();
		frame.setTitle("Voiture");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(0, 0, (int) (screenSize.getWidth() / 4.0),
				(int) (screenSize.getHeight() / 3.0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new GUI();
	}
}
