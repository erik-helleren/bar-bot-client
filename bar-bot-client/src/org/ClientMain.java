package org;

import javax.swing.*;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientModel model = new ClientModel();
		JFrame overhead = new JFrame("Bar Bot Interface");
		overhead.setSize(1000, 700);
		overhead.setExtendedState(overhead.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		SelectView s_view  = new SelectView(model);
		s_view.setVisible(false);
		EditView e_view = new EditView(model);
		e_view.setVisible(false);
		ConfigView c_view = new ConfigView(model);
		c_view.setVisible(false);
		
		JTabbedPane panel = new JTabbedPane();
		panel.addTab("Select Drink", s_view);
		panel.addTab("Edit Drinks", e_view);
		panel.addTab("Configuration", c_view);
		
		//overhead.getContentPane().add(s_view);
		//overhead.getContentPane().add(e_view);
		overhead.getContentPane().add(panel);
		
		overhead.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		overhead.setVisible(true);
		
		ClientController controller = new ClientController(model, s_view, e_view, c_view);
		
		s_view.setVisible(true);

	}

}
