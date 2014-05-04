package org;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

public class ClientMain {
	public static Color bgc = new Color(180,200,255);
	public static Color cbgc = new Color(210,230,255);
	public static Color bbgc = Color.black;
	public static Color fgc = Color.LIGHT_GRAY;
	public static Color tbgc = Color.white;
	public static Color tfgc = Color.black;
	public static Color tgbgc = Color.LIGHT_GRAY;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (UnsupportedLookAndFeelException e) {}
		 catch (IllegalAccessException e) {} 
		 catch (ClassNotFoundException e) {} 
		 catch (InstantiationException e) {}*/
		
		UIDefaults uiDefaults = UIManager.getDefaults();
		for (Enumeration e = uiDefaults.keys(); e.hasMoreElements(); ) {
			Object obj = e.nextElement();
			if (obj instanceof String) {
				if ( ((String)obj).matches(".*\\.background$") && uiDefaults.get(obj) instanceof Color ) {
					uiDefaults.put(obj, bbgc);
				}
				if ( ((String)obj).matches(".*\\.foreground$") && uiDefaults.get(obj) instanceof Color ) {
					uiDefaults.put(obj, fgc);
				}
			}
		}
		
		ClientModel model = new ClientModel();
		JFrame overhead = new JFrame("Bar Bot Interface");
		overhead.setSize(1000, 700);
		overhead.setExtendedState(overhead.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		SelectView s_view  = new SelectView(model);
		//s_view.setVisible(false);
		EditView e_view = new EditView(model);
		//e_view.setVisible(false);
		ConfigView c_view = new ConfigView(model);
		//c_view.setVisible(false);
		JPanel backing = new JPanel();
		backing.setLayout(new BorderLayout());
		overhead.getContentPane().add(backing);
		JTabbedPane panel = new JTabbedPane();
		backing.setBackground(bgc);
		panel.setBackground(bgc);
		s_view.setBackground(bgc);
		c_view.setBackground(bgc);
		e_view.setBackground(bgc);
		s_view.setForeground(fgc);
		e_view.setForeground(fgc);
		c_view.setForeground(fgc);
		backing.setForeground(fgc);
		panel.setForeground(tfgc);
		panel.addTab("Select Drink", s_view);
		panel.addTab("Edit Drinks", e_view);
		panel.addTab("Configuration", c_view);
		
		//overhead.getContentPane().add(s_view);
		//overhead.getContentPane().add(e_view);
		backing.add(panel, BorderLayout.CENTER);
		overhead.pack();
		
		overhead.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		overhead.setVisible(true);
		overhead.pack();
		
		ClientController controller = new ClientController(model, s_view, e_view, c_view);
		
		//s_view.setVisible(true);

	}

}
