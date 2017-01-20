package ai;

import java.awt.EventQueue;

import ai.ui.gui.InitMainWindow;

/**
 * Responsible for starting the program
 *
 */
public class AI {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					InitMainWindow window = new InitMainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
