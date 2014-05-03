package org;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfigView extends JPanel{
	

	JPanel configPanel;
	
	JButton configAcceptButton;
	JButton configResetButton;
	
	JPanel configPanelArray[];
	
	JComboBox<String> ingredientConfigComboBox[];
	
	JTextField ipConfigTextField;
	JTextField passwordTextField;

	JMenuItem selectDrink;
	JMenuItem createDrink;
	JMenuItem configWindow;
	
	ClientModel model;

	ConfigView(ClientModel model){
		
		//setTitle("Bar-Bot");
		//setMinimumSize(new Dimension(1000, 700));
		//setExtendedState(getExtendedState() | MAXIMIZED_BOTH);
		//setBackground(Color.LIGHT_GRAY);
		//setResizable(false);
		
		/*
		JPanel barbotPanel = new JPanel();
		barbotPanel.setLayout( new BorderLayout());
		/*getContentPane().add(barbotPanel);
		
		configPanel = new JPanel();
		configPanel.setLayout( new BorderLayout());
		barbotPanel.add(configPanel);
		*/
		setLayout(new BorderLayout());
		/*
		 * Accept and Cancel Button Panel
		 */
		
		JPanel configAcceptPanel = new JPanel();
		configAcceptPanel.setLayout(new BorderLayout());
		/*configPanel.*/add(configAcceptPanel, BorderLayout.SOUTH);
		
		configAcceptButton = new JButton("Accept");
		//configAcceptButton.addActionListener(this);
		configAcceptPanel.add(configAcceptButton, BorderLayout.EAST);
		
		configResetButton = new JButton("Reset");
		//configResetButton.addActionListener(this);
		configAcceptPanel.add(configResetButton, BorderLayout.WEST);
		
		this.model = model;
		
		/*
		 * Information Editing Panel that resides on the right side of the screen
		 * Contains the drink's DRINK NAME, INGREDIENTS, and DESCRIPTION 
		 */
		JPanel arduinoConfigPanel = new JPanel();
		arduinoConfigPanel.setLayout(new BorderLayout());
		arduinoConfigPanel.setPreferredSize(new Dimension(400, JFrame.MAXIMIZED_VERT));
		arduinoConfigPanel.setBorder(BorderFactory.createTitledBorder("Arduino Configuration"));
		/*configPanel.*/add(arduinoConfigPanel, BorderLayout.WEST);
		
		
		JPanel ipConfigPanel = new JPanel();
		ipConfigPanel.setLayout(new BoxLayout(ipConfigPanel, BoxLayout.PAGE_AXIS));
		ipConfigPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 0));
		//ipConfigPanel.setPreferredSize(new Dimension(1000, 400));
		
		JPanel ipPanel = new JPanel();
		ipPanel.setLayout(new BorderLayout());
		ipPanel.setBorder( BorderFactory.createEmptyBorder(0, 0, 10, 0));
		JLabel ipConfigLabel = new JLabel("IP Address: ");
		ipConfigTextField = new JTextField(20);
		ipConfigTextField.setBackground(ClientMain.tbgc);
		ipConfigTextField.setForeground(ClientMain.tfgc);
		ipPanel.add(ipConfigLabel, BorderLayout.WEST);
		ipPanel.add(ipConfigTextField, BorderLayout.CENTER);
		
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new BorderLayout());
		JLabel passwordLabel = new JLabel("Password:  ");
		passwordTextField = new JTextField(20);
		passwordTextField.setBackground(ClientMain.tbgc);
		passwordTextField.setForeground(ClientMain.tfgc);
		passwordPanel.add(passwordLabel, BorderLayout.WEST);
		passwordPanel.add(passwordTextField, BorderLayout.CENTER);
		
		ipConfigPanel.add(ipPanel);
		ipConfigPanel.add(passwordPanel);
		
		
		arduinoConfigPanel.add(ipConfigPanel, BorderLayout.NORTH);
		
		
		JPanel ingredientConfigPanel = new JPanel();
		ingredientConfigPanel.setLayout(new BoxLayout(ingredientConfigPanel, BoxLayout.PAGE_AXIS));
		ingredientConfigPanel.setBorder( BorderFactory.createEmptyBorder(10, 0, 0, 10));
		arduinoConfigPanel.add(ingredientConfigPanel, BorderLayout.CENTER);
		
		
		/*
		 * Header text for the Drink creation field
		 */
		configPanelArray = new JPanel[13];
		
		JLabel pumpHeaderLabel = new JLabel("");//Pump #");
		JPanel pumpHeaderPanel = new JPanel();
		pumpHeaderPanel.add(pumpHeaderLabel);
		pumpHeaderLabel.setPreferredSize(new Dimension(150,20));
		JLabel ingredientHeaderLabel = new JLabel("");//Ingredient");
		JPanel ingredientHeaderPanel = new JPanel();
		ingredientHeaderPanel.add(ingredientHeaderLabel);
		
		configPanelArray[12] = new JPanel(new BorderLayout());
		configPanelArray[12].add(pumpHeaderPanel, BorderLayout.WEST);
		configPanelArray[12].add(ingredientHeaderPanel, BorderLayout.CENTER);
		ingredientConfigPanel.add(configPanelArray[12]);
		
		
		/*
		 * Handles the Labels, textfields, and remove buttons
		 * in the Drink creation field
		 */
		JPanel pumpPanelArray[] = new JPanel[12];
		
		JPanel comboPanelArray[] = new JPanel[12];
		ingredientConfigComboBox = new JComboBox[12];
		
		for (int i = 0; i < 12; i++){
			
			configPanelArray[i] = new JPanel(new BorderLayout());
			pumpPanelArray[i] = new JPanel();
			JLabel pumpNumLabel = new JLabel();
			pumpNumLabel.setText("Pump #" + (i+1));
			pumpPanelArray[i].add(pumpNumLabel);
			//ingredientNamePanel[i] = new JPanel();
			//ingredientNameArray[i] = new JTextField(20);
			//ingredientNameArray[i].setText("");
			//ingredientNameArray[i].setEditable(false);
			//ingredientNameArray[i].setVisible(false);
			//ingredientNamePanel[i].add(ingredientNameArray[i]);
			
			ingredientConfigComboBox[i] = new JComboBox<String>();
			ingredientConfigComboBox[i].addItem("N/A");
			
			for(String s:model.getIngredientsList()){
				ingredientConfigComboBox[i].addItem(s);
			}
			
			
			//amountTextArray[i].setVisible(false);
			comboPanelArray[i] = new JPanel();
			comboPanelArray[i].add(ingredientConfigComboBox[i]);
			
			
	
			configPanelArray[i].add(pumpPanelArray[i],BorderLayout.WEST);
			configPanelArray[i].add(comboPanelArray[i],BorderLayout.CENTER);
			//ingredientPanelArray[i].setVisible(false);
			ingredientConfigPanel.add(configPanelArray[i]);
		}
		
		/*for(String s:model.getIngredientsList()){
			if(model.getPumpID(s) >= 0){
				ingredientConfigComboBox[model.getPumpID(s)].setSelectedItem(s);
			}
		}*/
		
		
		/*
		 * Menu Bar
		 */
		/*
		JMenuBar menuBar = new JMenuBar();
		barbotPanel.add(menuBar, BorderLayout.NORTH);
		
		JMenu edit = new JMenu("Edit");
		menuBar.add(edit);
		
		selectDrink = new JMenuItem("Select Drink");
		//selectDrink.addActionListener(this);
		edit.add(selectDrink);
		
		createDrink = new JMenuItem("Create Drink");
		//createDrink.addActionListener(this);
		edit.add(createDrink);
		
		configWindow = new JMenuItem("Config");
		//configWindow.addActionListener(this);
		edit.add(configWindow);
		*/
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	String getIP(){
		return ipConfigTextField.getText();
	}
	
	void setIP(String s){
		ipConfigTextField.setText(s);
	}
	
	String getPassword(){
		return passwordTextField.getText();
	}
	
	void setPassword(String s){
		passwordTextField.setText(s);
	}
	
	String getIngredientConfig(int i){
		return ingredientConfigComboBox[i].getSelectedItem().toString();
	}
	
	void resetIngredientConfig(){
		for(int i = 0; i < 12; i++){
			ingredientConfigComboBox[i].setSelectedIndex(0);
		}
	}
	
	void reInitIngredientConfig(){
		for(int i = 0; i < 12; i++){
			ingredientConfigComboBox[i].removeAllItems();
			ingredientConfigComboBox[i].addItem("N/A");
			for(String s:model.getIngredientsList()){
				ingredientConfigComboBox[i].addItem(s);
			}
		}
	}
	
	
	void addAcceptButtonListener(ActionListener accept){
		configAcceptButton.addActionListener(accept);
	}
	
	void addCancelButtonListener(ActionListener cancel){
		configResetButton.addActionListener(cancel);
	}
	
	void addWindowSwitchListener(ActionListener e){
		selectDrink.addActionListener(e);
		createDrink.addActionListener(e);
		configWindow.addActionListener(e);
	}
	
}
