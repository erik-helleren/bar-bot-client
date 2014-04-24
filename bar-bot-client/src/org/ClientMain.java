package org;

import javax.swing.*;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClientModel model = new ClientModel();
		SelectView s_view  = new SelectView(model);
		EditView e_view = new EditView(model);
		ConfigView c_view = new ConfigView(model);
		
		ClientController controller = new ClientController(model, s_view, e_view, c_view);
		
		s_view.setVisible(true);

	}

}
